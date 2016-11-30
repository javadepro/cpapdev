function setupAjaxForm(formid) {
	var form = "#"+formid;
	// setup loading message
	$(form).on('submit', function() {
		$.ajax({ // create an AJAX call...
			data : $(this).serialize(), // get the form data
			type : $(this).attr('method'), // GET or POST
			url : $(this).attr('action'), // the file to call
			dataType : "text",
			success : function(data) {
				var scripts = $(data).find("script").text();
				$(form).replaceWith($(data));
				eval(scripts);
			}
		});
		return false; // cancel original event to prevent form submitting
	});
}
function setupAjaxFormReplace(formid, divid) {
	var form = "#"+formid;
	var div = "#"+divid;
	// setup loading message
	$(form).on('submit', function() {
		$.ajax({ // create an AJAX call...
			data : $(this).serialize(), // get the form data
			type : $(this).attr('method'), // GET or POST
			url : $(this).attr('action'), // the file to call
			dataType : "text",
			success : function(data) {
				var scripts = $(data).find("script").text();
				$(div).replaceWith($(data));
				eval(scripts);
			}
		});
		return false; // cancel original event to prevent form submitting
	});
}
function setupAjaxFormReplaceWithAttachment(formid, divid) {
	var form = "#"+formid;
	var div = "#"+divid;		
	
	$(form).on('submit', function() {
		var formData = new FormData($(this)[0]);
		$.ajax({ // create an AJAX call...			
			data : formData,
			type : "POST",
			url : $(this).attr('action'), // the file to call
			processData : false,
			contentType : false,
			success : function(data) {
				var scripts = $(data).find("script").text();
				$(div).replaceWith($(data));
				eval(scripts);
			}
		});
		return false; // cancel original event to prevent form submitting
	});
}

(function( $ ) {
	$.widget( "ui.combobox", {
		_create: function() {
			var input,
				self = this,
				select = this.element.hide(),
				selected = select.children( ":selected" ),
				value = selected.val() ? selected.text() : "",
				wrapper = $( "<span>" )
					.addClass( "ui-combobox" )
					.insertAfter( select );

			input = $( "<input>" )
				.appendTo( wrapper )
				.val( value )
				.addClass( "ui-state-default" )
				.autocomplete({
					delay: 0,
					minLength: 0,
					source: function( request, response ) {
						var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
						response( select.children( "option" ).map(function() {
							var text = $( this ).text();
							if ( this.value && ( !request.term || matcher.test(text) ) )
								return {
									label: text.replace(
										new RegExp(
											"(?![^&;]+;)(?!<[^<>]*)(" +
											$.ui.autocomplete.escapeRegex(request.term) +
											")(?![^<>]*>)(?![^&;]+;)", "gi"
										), "<strong>$1</strong>" ),
									value: text,
									option: this
								};
						}) );
					},
					select: function( event, ui ) {
						ui.item.option.selected = true;
						self._trigger( "selected", event, {
							item: ui.item.option
						});
					},
					change: function( event, ui ) {
						if ( !ui.item ) {
							var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( $(this).val() ) + "$", "i" ),
								valid = false;
							select.children( "option" ).each(function() {
								if ( $( this ).text().match( matcher ) ) {
									this.selected = valid = true;
									return false;
								}
							});
							if ( !valid ) {
								// remove invalid value, as it didn't match anything
								$( this ).val( "" );
								select.val( "" );
								input.data( "autocomplete" ).term = "";
								return false;
							}
						}
					}
				})
				.addClass( "ui-widget ui-widget-content ui-corner-left" );

			input.data( "autocomplete" )._renderItem = function( ul, item ) {
				return $( "<li></li>" )
					.data( "item.autocomplete", item )
					.append( "<a>" + item.label + "</a>" )
					.appendTo( ul );
			};

			$( "<a>" )
				.attr( "tabIndex", -1 )
				.attr( "title", "Show All Items" )
				.appendTo( wrapper )
				.button({
					icons: {
						primary: "ui-icon-triangle-1-s"
					},
					text: false
				})
				.removeClass( "ui-corner-all" )
				.addClass( "ui-corner-right ui-button-icon" )
				.click(function() {
					// close if already visible
					if ( input.autocomplete( "widget" ).is( ":visible" ) ) {
						input.autocomplete( "close" );
						return;
					}

					// work around a bug (likely same cause as #5265)
					$( this ).blur();

					// pass empty string as value to search for, displaying all results
					input.autocomplete( "search", "" );
					input.focus();
				});
		},

		destroy: function() {
			this.wrapper.remove();
			this.element.show();
			$.Widget.prototype.destroy.call( this );
		}
	});
})( jQuery );

jQuery.fn.inputHints = function () {
    function showHints(el) {
        if ($(el).val() == '')
            $(el).val($(el).attr('title'))
                .addClass('auto-hint');
    };

    function hideHints(el) {
        if ($(el).val() == $(el).attr('title'))
            $(el).val('').removeClass('auto-hint');
    };

    // hides the input display text stored in the title on focus
    // and sets it on blur if the user hasn't changed it.

    var el = $(this);

    // show the display text on empty elements
    el.each(function () {
        showHints(this);
    });

    // clear the hints on form submit
    el.closest('form').submit(function () {
        el.each(function () {
            hideHints(this);
        });
        return true;
    });

    // hook up the blur & focus
    return el.focus(function () {
        hideHints(this);
    }).blur(function () {
        showHints(this);
    });
};