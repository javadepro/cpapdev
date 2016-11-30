<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<form:form id="invoiceSearchForm" commandName="invoiceSearchForm" action="/pos/invoice-search" method="POST" >

<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Invoice Search</span></h1>
</div>
<div style="clear:both" />
<p class="pageDesc">Use this page to search for invoice.  The best way to search is by using the invoice number.</p>
<h2>Search Criteria</h2>
<form:errors path="*" cssClass="errorblock"/>
<ul>
<li><label>Invoice Number:</label>&nbsp;<form:input path="invoiceNumber" /></li>
<li>or&nbsp;</li><br/>
<li><label>Period:</label>&nbsp;<form:select path="numMths">
			<form:options items="${periodFilter}" />
		</form:select>&nbsp;
</li>
</ul>
<br />
<input type="submit" value="Search" class="bluebtn"  id="refreshbtn" />

<c:choose>
<c:when test="${invoiceResults!=null}">
<h2>Search Result</h2>
<table class="listing" width="1000px;">
	<tr>
	<!-- 	<th  class="minorinfo">Id</th> -->
		<th>Invoice Date</th>
		<th>Invoice Number</th>
		<th>Customer Name</th>
		<th>Clinician</th>
		<th>Shop Name</th>
		<th>Invoice Type</th>
		<th>Status</th>
		<th style="width: 190px;">Action</th>
	</tr>
	<c:forEach var="invoice" items="${invoiceResults}">
		<tr>
			<td><fmt:formatDate pattern="dd/MM/yyyy" value="${invoice.invoiceDate}" /></td>

			<td><c:out value="${invoice.invoiceNumber}" /></td>
			<td><c:out value="${invoice.customerName }"/></td>
			<td><c:out value="${invoice.userInitial}"/></td>
			<td><c:out value="${invoice.shopName }"/></td>
			<td><c:out value="${invoice.invoiceType}"/></td>
			<td><c:out value="${invoice.status }"/></td>
			<td>
				<a href="/pos/invoice-view?invoiceNumber=<c:out value="${invoice.invoiceNumber}" />" style="color:#333;text-decoration:none;" title="View"><img src="/images/view.png" alt="View" />View</a>
				<sec:authorize access="hasRole('ROLE_POS_ADMIN')">
				<c:if test="${invoice.status !='VOID' }">
				&nbsp;|&nbsp;<a href="/pos/invoice-void-form?invoiceNumber=<c:out value="${invoice.invoiceNumber}"/>" class="void" style="color:#333;text-decoration:none;" title="Void"><img src="/images/cancel-icon.png" width="16px" height="16px" alt="View" />Void</a>
				</c:if>
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_ADP_RECEIVER')">
				<c:if test="${invoice.invoiceType =='STANDARD' }">
				&nbsp;|&nbsp;<a href="/pos/invoice-adp-pymt?invoiceNumber=<c:out value="${invoice.invoiceNumber}"/>" style="color:#333;text-decoration:none;" title="ADP Pay"><img src="/images/view.png" width="16px" height="16px" alt="View" />ADP Pay</a>
				</c:if>
				</sec:authorize>				
			</td> 
			
		</tr>
	</c:forEach>
	
</table>
<br />
</c:when>
<c:when test="${invoiceResults==null}">
<p>No Result!</p>
</c:when>
</c:choose>
</form:form>

<script>

$(".open-on-tab").click(function(e) {
	e.preventDefault();
	var url = $(this).attr('href');
	var title = $(this).attr('title');
	$('#mainbody').load(url);
	return false;
});
setupAjaxForm("search-form");

$(function (){
  $('a.void').click(function() {
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
	$(".refreshbtn").submit();
}
</script>

