<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<form:form id="invoiceAdpBulkForm" commandName="invoiceAdpBulkForm" action="/pos/invoice-adpbulk-formsubmit" method="POST" >

<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Invoice Bulk ADP Processing</span></h1>
</div>
<div style="clear:both" />
<p class="pageDesc">Use this page to search for bulk process adp payments</p>

	<form:errors path="*" cssClass="errorblock"/>
	<c:if test="${message!=null}">
		<div class="messageblock" style="width: 1070px;">${message}</div>
	</c:if>
<div class="invoicesetup-block" style="width: 1070px; height: 165px">
	<div>Search Criteria</div>
	<div class="invoiceSetupColWrapper">
		<ul>
		<li><label>From:</label>&nbsp;<form:select path="fromNumMths"><form:options items="${periodFilter}" /></form:select>&nbsp;&nbsp;</li>
		<li><label>To:</label>&nbsp;<form:select path="toNumMths"><form:options items="${periodFilter}" /></form:select>&nbsp;&nbsp;</li>
		<li><label>Invoice ADP Status:</label>&nbsp;
			<form:select path="adpStatus">
				<form:option label="All" value="" />
				<form:options items="${adpStatusList}" />
			</form:select>
		</li>
		</ul>
		<br />
		<input type="submit" name="actionType"  value="search" class="bluebtn"  id="refreshbtn" />
	</div>
</div>

<div class="invoicesetup-block" style="width: 1070px; height: 120px">
	<div>Apply Payment Settings</div>
	<div class="invoiceSetupColWrapper">
	<ul>
		<li><label>Payment Date:</label><span><form:input path="processedDate" class="datepicker" placeholer="dd/MM/YYYY" /></span></li>	
	</ul>
	<br />
	<input type="submit" name="actionType"  value="Apply Full Payment" class="bluebtn"  id="refreshbtn" />
	</div>
</div>



<c:choose>
<c:when test="${invoiceResults!=null}">
<h2>Search Result</h2>
<table class="listing" width="1070px;">
	<tr>
	<!-- 	<th  class="minorinfo">Id</th> -->
		<th  style="width: 40px;">Select</th>
		<th>Invoice Date</th>
		<th>Invoice Number</th>
		<th>Customer Name</th>
		<th style="width: 60px;">Shop Name</th>
		<th>ADP Status</th>
		<th style="width: 100px;">ADP Initial Balance</th>
		<th style="width: 100px;">ADP Remaining Balance</th> 
		<th style="width: 100px;">Action</th>
	</tr>
	<c:forEach var="invoice" items="${invoiceResults}" varStatus="status">
		<tr>
			<td style="text-align: center;"><form:checkbox path="invoiceSelected[${status.index}]" value="${invoice.invoiceNumber}"  /></td>
			<td><fmt:formatDate pattern="dd/MM/yyyy" value="${invoice.invoiceDate}" /></td>

			<td><a href="/pos/invoice-view?invoiceNumber=<c:out value="${invoice.invoiceNumber}" />" style="color:#333;	" title="View"><c:out value="${invoice.invoiceNumber}" /></a></td>
			<td><c:out value="${invoice.customerName }"/></td>
			<td><c:out value="${invoice.shopName }"/></td>
			<td><c:out value="${invoice.adpStatus }"/></td>
			<td><c:out value="${invoice.adpPortion}"/></td>
			<td><c:out value="${invoice.adpBalanceRemaining}"/></td>
			<td>
				<sec:authorize access="hasRole('ROLE_ADP_RECEIVER')">
				<c:if test="${invoice.invoiceType =='STANDARD' }">
				<a href="/pos/invoice-adp-pymt?invoiceNumber=<c:out value="${invoice.invoiceNumber}"/>" style="color:#333;text-decoration:none;" title="Void"><img src="/images/view.png" width="16px" height="16px" alt="View" />ADP Pay</a>
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

$(".datepicker").datepicker({
		dateFormat : "dd/mm/yy",
		changeMonth: true,
		changeYear: true,
		yearRange: "-1:+0",
		defaultDate: new Date()
});
</script>


