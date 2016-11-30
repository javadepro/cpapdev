<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<div id="event-view">
<h2>Note / Client Activity History&nbsp;&nbsp;</h2>
<form:form id="event-filter" action="/customer/tabview/view-event" method="GET" commandName="eventSearchForm">
<form:select path="lastXDays">
	<form:options items="${periodFilter}"  />
</form:select>
<form:select path="eventSubType">

<option value="">ALL</option>
				<c:forEach var="eventType" items="${eventTypeMap}" varStatus="x">
					<optgroup label="${eventType.key.type}">
						<form:options items="${eventType.value}" itemLabel="type" /> 
					</optgroup>
				</c:forEach>
			</form:select>

<input type="hidden" name="customerId" value="${customerId}" />
<a href="/customer/tabview/view-event?customerId=${customerId}" id="refreshBtn" class="reload-event-page"><img src="/images/reload_icon.png" /></a>

</form:form>


<p>This section is meant you to track and trace a complete interaction history with this customer.  For example, You can indicate when you made attempts to contact the client, and write notes you wanted to take as a result.  By looking at these entries, you should be able to see the client's entire activity history with cpapdirect.
<br><br>Certain note types like appointment notes will automatically create future dated alerts for this customer.</p>


<table class="listing">
<tr><th>id</th><th>Date</th><th>Type</th><th style="width:600px;">Message</th>
<sec:authorize access="hasRole('ROLE_ADMIN')">
<th>Action</th>
</sec:authorize>
</tr>
<c:forEach var="event" items="${events}" >
<tr>
<td><c:out value="${event.id}" /></td>  
<td><fmt:formatDate value="${event.date}" type="DATE" pattern="dd/MM/yyyy"/></td>
<td style="color:<c:out value="${eventType[eventSubTypeMap[event.eventSubType].parentType].color}" />"><c:out value="${eventType[eventSubTypeMap[event.eventSubType].parentType].type}" /> - <c:out value="${eventSubTypeMap[event.eventSubType].type}" /></td>  
<td><c:out value="${event.details}" escapeXml="false"/></td>
<sec:authorize access="hasRole('ROLE_ADMIN')">
<td><a href="/customer/event/dismiss?id=${event.id}&customerId=${customerId}&eventSubType=<spring:bind path="eventSearchForm.eventSubType"><c:out value="${status.value}" /></spring:bind>&lastXDays=<spring:bind path="eventSearchForm.lastXDays"><c:out value="${status.value}" /></spring:bind>" class="reload-event-page" style="color:#333">Dismiss</a></td>
</sec:authorize>
</tr>
</c:forEach>
</table>
</br>
<a href="/customer/event/form?customerId=<c:out value="${customerId}" />" id="eventmodal" title="Add a Event History/Note" class="bluebtn" style="color:#FFF;">Add a note</a>

<script> 
jQuery.fn.log = function (msg) {
	  console.log("%s: %o", msg, this);
	  return this;
	};
$("#refreshBtn").click(function(){
	$("#event-filter").submit();
});
setupAjaxFormReplace("event-filter","event-view");

$(function (){
	$('#eventmodal,a.modal').click(function() {
        var url = this.href;
        var title = this.title;
        // show a spinner or something via css
        var dialog = $('<div class="loading">&nbsp;</div>').appendTo('body');
        // open the dialog
        dialog.dialog({
            // add a close listener to prevent adding multiple divs to the document
            close: function(event, ui) {
                // remove div with all data and events
                dialog.log("close");
                url = "/customer/tabview/view-event?customerId=${customerId}";
                $('#event-view').load(url);
                dialog.remove();
            },
            modal: true,
            //position: center,
            title: title,
            width: "auto",
            height: "auto",
        });
        // load remote content
        dialog.load(
            url, 
         	 // omit this param object to issue a GET request instead a POST request, otherwise you may provide post parameters within the object
            function (responseText, textStatus, XMLHttpRequest) {
                // remove the loading class
                dialog.removeClass('loading');
            }
        )
       
        //prevent the browser to follow the link
        return false;
    });
});

$(".reload-event-page").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	var title = $(this).attr('title');
	$('#event-view').load(url);
	return false;
});

</script>
</div>