<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div id="alert-view">
<h2>Scheduled Alerts&nbsp;&nbsp;</h2>
<form:form id="alert-filter" commandName="customerAlertSearch" action="/customer/tabview/view-alert" method="GET" >
<form:hidden path="customerId" />
<form:select path="numDays">
	<form:options items="${periodFilter}"  />
</form:select>
<form:select path="alertSubType">
<option value="">ALL</option>
				<c:forEach var="alertType" items="${alertTypeMap}" varStatus="x">
					<optgroup label="${alertType.key.type}">
						<form:options items="${alertType.value}" itemLabel="type" /> 
					</optgroup>
				</c:forEach>
			</form:select>

<a href="#" id="refreshbtn" class="reload-alert-page">
<img src="/images/reload_icon.png" /></a>
</form:form>
<p class="pageDesc">Alerts are can be set to remind you or your colleagues when to follow up with this particular patient only.  To edit or delay a reminder, you can use "Reschedule".  To remove an alert after you have already actioned on it, use "dimiss".</p>

<table class="listing">
<tr><th class="minorinfo">id</th><th>Date</th><th>Type</th><th>Message</th><th>Action</th> </tr>
<c:forEach var="alert" items="${alerts}" >
<tr>
<td class="minorinfo"><c:out value="${alert.id}" /></td>  
<td><fmt:formatDate value="${alert.alertDate}" type="DATE" pattern="dd/MM/yyyy"/></td>
<td style="color:<c:out value="${alertType[alertSubType[alert.alertSubType].parentType].color}" />"><c:out value="${alertType[alertSubType[alert.alertSubType].parentType].type}" /> - <c:out value="${alertSubType[alert.alertSubType].type}" /></td>  
<td><c:out value="${alert.message}" /></td>
<td>
<a href="/customer/alert/form?id=${alert.id}&customerId=${customerId}" class="modal" style="color:#333">Resched</a>
&nbsp;|&nbsp;
<a href="/customer/alert/dismiss?id=${alert.id}&customerId=${customerId}&alertSubType=<spring:bind path="customerAlertSearch.alertSubType"><c:out value="${status.value}" /></spring:bind>&numDays=<spring:bind path="customerAlertSearch.numDays"><c:out value="${status.value}" /></spring:bind>" class="dismiss" style="color:#333">Dismiss</a>
</td>
 </tr>
</c:forEach>
</table>
</br>
<a href="/customer/alert/form?customerId=<c:out value="${customerId}" />" id="alertmodal" title="Add a new alert" class="bluebtn" style="color:#FFF;">Add a new alert</a>
<script> 
jQuery.fn.log = function (msg) {
	  console.log("%s: %o", msg, this);
	  return this;
	};
	
$(function (){
    $('#alertmodal,a.modal').click(function() {
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
                refreshList();
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
function refreshList(){
	$("#alert-filter").submit();
}
$(".reload-alert-page").click(function(e){
	refreshList();
});
setupAjaxFormReplace("alert-filter","alert-view");

$(".dismiss").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	var title = $(this).attr('title');
	$('#alert-view').load(url);
	return false;
});



</script>
</div>