<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%--<form:form commandName="worksIn" action="formsubmit.do" method="POST">
<label>Clinic</label>
<form:input id="clinicName" path="name" />
</form:form>--%>
<form id="worksIn">
	<input id="clinicName" name="name" />
	<input id="clinicId" value="" />
	<textarea></textarea>
</form>
<script>
$.ajax({
	type : "GET",
	url : "/refdata/clinic/listAsJson",
	dataType : "json",
	data : {},
	success : function(data) {
		var source = $.map(data, function(item) {
			return {
				label : item.name,
				value : item.name,
				id : item.id
			};
		});
		$("#clinic-input").autocomplete({
			source : source,
			minLength : 2,
			select : function(event, ui) {
				$("#clinic-input").val(ui.item.value);
				$("#clinic-input-hidden").val(ui.item.value);
				return false;
			},
			focus : function(event, ui) {
				$("#clinic-input").val(ui.item.value);
				return false;
			},
		});
	}
});
</script>