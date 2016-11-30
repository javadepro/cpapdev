<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="alert-list">
	<div id="title">
		<h1><div id="icon-bubble" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Alerts</span></h1>
	</div>
	<div style="clear:both" />
	<h2>Customer Alert</h2>
	<br />
	<form:form id="alert-filter" commandName="customerAlertSearch"
		action="/alert/customer" method="GET">
		<b>Period:</b>&nbsp;<form:select path="numDays">
			<form:options items="${periodFilter}" />
		</form:select>&nbsp;
	<b>Assigned To:</b>&nbsp;<form:select path="clinician">
			<form:option value="" label="ALL" />
			<form:options items="${clinicians}" itemLabel="name" />
		</form:select>&nbsp;

	<b>Alert Type</b>&nbsp;
		<form:select path="alertSubType">
			<option value="">ALL</option>
			<c:forEach var="alertType" items="${alertTypeMap}" varStatus="x">
				<optgroup label="${alertType.key.type}">
					<form:options items="${alertType.value}" itemLabel="type" />
				</optgroup>
			</c:forEach>
		</form:select>		
		
		<input type="submit" value="Filter" id="refresh-btn" class="bluebtn" />
	</form:form>

	<br /><br />
<table class="listing">
<tr><th style="width:60px;">Date</th><th>Customer</th><th style="width:100px;">Type</th><th>Message</th><th>Assigned To</th><th style="width:200px;">Action</th></tr>
<c:forEach var="alert" items="${alerts}" >
<tr>
<td><fmt:formatDate value="${alert.alertDate}" type="DATE" pattern="dd/MM/yyyy"/></td>
<td><a href="/customer/tabview?id=${customers[alert.customer].id}" class="open-on-tab" style="color:#333;"><c:out value="${customers[alert.customer].name}" /></a></td>
<td style="color:<c:out value="${alertType[alertSubType[alert.alertSubType].parentType].color}" />"><c:out value="${alertType[alertSubType[alert.alertSubType].parentType].type}" /> - <c:out value="${alertSubType[alert.alertSubType].type}" /></td>  
<td><c:out value="${alert.message}" /></td>
<td><c:out value="${clinicians[alert.clinician].name}" /></td>
<td>
	<a href="/customer/alert/form?id=${alert.id}&customerId=${customers[alert.customer].id}" class="reschedule" style="color:#333">Resched</a>
	&nbsp;|&nbsp;
	
	<a href="/alert/customer/dismiss?alertId=${alert.id}&customerId=${customers[alert.customer].id}&alertSubType=<spring:bind path="customerAlertSearch.alertSubType"><c:out value="${status.value}" /></spring:bind>&numDays=<spring:bind path="customerAlertSearch.numDays"><c:out value="${status.value}" /></spring:bind>&clinician=<spring:bind path="customerAlertSearch.clinician"><c:out value="${status.value}" /></spring:bind>" class="reload-alert-page" style="color:#333">Dismiss</a>
	&nbsp;|&nbsp;

	<a href="/customer/event/formForAlertDismiss?alertId=${alert.id}&customerId=${customers[alert.customer].id}" class="add-note-dismiss" style="color:#333">Note & Dismiss</a>
	&nbsp;|&nbsp;
	<a href="/customer/label/addressInfo?customerId=${customers[alert.customer].id}" class="addr" style="color:#333">Addr</a>
	
</td>
</tr>
</c:forEach>
</table>
<br />
<%--
<input type="button" value="Dismiss" class="bluebtn" /><input type="button" value="Email" class="bluebtn" />
 --%>
<br />
<br />
<div id="access-deneid" title="Access Denied">
	<p>You do not have access to view or edit this customer. Please ask reception/admin to add to the exception list</p>
</div>
<script>
	$("#access-deneid").dialog({ autoOpen: false,modal: true });
	jQuery.fn.log = function (msg) {
	  console.log("%s: %o", msg, this);
	  return this;
	};
function refreshList(){
	$("#refresh-btn").click();
}
$(function (){
  $('a.reschedule, a.addr').click(function() {
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

$(function (){
	  $('a.add-note-dismiss').click(function() {
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

//setupAjaxFormReplace("alert-filter","alert-list");
$(".open-on-tab").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	$('#mainbody').load(
			url,function(response, status, xhr) {
		  if (status == "error") {
			  	if(xhr.status==403){
			  		//alert($("#access-deneid"));
			  		$("#access-deneid").dialog("open");
			  	}
			  }
			});
	return false;
});
</script>
</div><!-- End div:id=alert-list -->
