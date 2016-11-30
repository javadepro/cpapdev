<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="alert-product-list">
	<div id="title">
		<h1><div id="icon-bubble" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Alerts</span></h1>
	</div>
	<div style="clear:both" />
	<h2>Product Alert</h2>
	<br />
	<form:form id="alert-filter" commandName="productAlertSearch" action="/alert/product/" method="GET">
		<b>Period:</b>&nbsp;<form:select path="numDays">
			<form:options items="${periodFilter}" />
		</form:select>&nbsp;
		
		<b>Manufacturer</b>&nbsp;
		<form:select path="manufacturer">
			<option value="">ALL</option>
			 <form:options items="${manufacturers}" itemLabel="name" />		
		</form:select>		

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
<tr>
	<th class="minorinfo">id</th>
	<th>Date</th>
	<th>Manufacturer</th>
	<th>Product</th>
	<th>Ref Number</th>
	<th>Type</th>
	<th>Action</th></tr>
<c:forEach var="alert" items="${alerts}" >
<tr>
<td class="minorinfo"><c:out value="${alert.id}" /></td>  
<td><fmt:formatDate value="${alert.alertDate}" type="DATE" pattern="dd/MM/yyyy"/></td>
<td><c:out value="${manufacturers[productMap[alert.product].manufacturer].name}" /></td>
<td><a href="/product/view?id=${productMap[alert.product].id}"  style="color:#333;"><c:out value="${productMap[alert.product].name}" /></a></td>
<td><c:out value="${productMap[alert.product].referenceNumber}"/></td>
<td style="color:<c:out value="${alertType[alertSubType[alert.alertSubType].parentType].color}" />"><c:out value="${alertType[alertSubType[alert.alertSubType].parentType].type}" /> - <c:out value="${alertSubType[alert.alertSubType].type}" /></td>  

<td>
	<a href="/alert/product/dismiss?alertId=${alert.id}&productId=${productMap[alert.product].id}&alertSubType=<spring:bind path="productAlertSearch.alertSubType"><c:out value="${status.value}" /></spring:bind>&numDays=<spring:bind path="productAlertSearch.numDays"><c:out value="${status.value}" /></spring:bind>&manufacturer=<spring:bind path="productAlertSearch.manufacturer"><c:out value="${status.value}" /></spring:bind>" class="reload-alert-page" style="color:#333">Dismiss</a>
	&nbsp; 

	
</td>
</tr>
</c:forEach>
</table>
<br />
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
</div> 
