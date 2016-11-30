<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div id="invoicePageWrapper">
<div id="invoiceTitle">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Invoice ADP Payment History</span></h1>
</div>
<div style="clear:both; width: 100%;" />

			<form:form id="invoicePymtForm" commandName="invoicePymtForm"
				action="/pos/invoice-adp-pymt-formsubmit" method="POST">
<form:errors path="*" cssClass="errorblock" style="width: 1050px;"></form:errors>
	<c:if test="${message!=null}">
		<div class="messageblock" style="width: 1070px;">${message}</div>
	</c:if>
	
	<sec:authorize access="hasRole('ROLE_ADP_RECEIVER')">
		<div class="invoice-summary-block" style="width: 1070px;">
		<div>Invoice ADP Payment</div>

				
				<form:hidden path="invoiceNumber" />
				<div class="invoiceSetupCol" style="width: 40%;">
				<ul>
					<li><label style="width: 200px; padding-left: 10px;">Remaining Balance:</label>
						<div id="balanceRemaining"><c:out value="${invoice.adpBalanceRemaining}" /></div>			
					</li>		
					<li><label style="width: 200px; padding-left: 10px;">Payment Amount:</label>
						<form:input path="paymentAmount" class="numeric" />
					</li>	
					<li><label style="width: 200px; padding-left: 10px;">Description:</label>
						 <form:select path="description">
							<form:options items="${paymentDescriptions}" itemLabel="name" itemValue="value" />
						</form:select></li>
					</li>								
					
				</ul>
				</div>
				<div class="invoiceSetupCol" style="width: 8%">
				<br/><br/><br/><br/>
					<input type="submit" name="submit" class="bluebtn" />
				</div>
				<br>
				<br>
		</div>
		</sec:authorize>
	</form:form>
			
	<div class="invoice-block" style="width: 1050px;  padding: 10px; ">	
	
		<div style="height: 130px; width: 100%;">
			<div class="invoice-logo-block">
			<img src="/images/logo_${invoice.company}.png" class="invoice-logo-img" />
			</div>
			<div class="invoice-addr-block">
				<c:out value="${invoice.shopAddress.line1}"/><br/>
				<c:if test="${invoice.shopAddress.line2 != ''}">
					<c:out value="${invoice.shopAddress.line2}"/><br/>
				</c:if>
				<c:out value="${invoice.shopAddress.city}"/>,  
				<c:out value="${invoice.shopAddress.province}"/>&nbsp;
				<c:out value="${invoice.shopAddress.postalCode}"/><br/>
				
				P: <c:out value="${invoice.shopPhone}"/>&nbsp;&nbsp;&nbsp;
				F: <c:out value="${invoice.shopFax}"/><br/>
				<c:if test="${invoice.company == 'CPAPDIRECT' }">
				www.cpapdirect.ca
				</c:if>
			</div>
			<div class="invoice-meta-block">
				<font style="font-weight: bold;font-size:16px;">INVOICE #:<c:out value="${invoice.invoiceNumber}"/></font><br/>
				
				<c:if test="${invoice.company == 'CPAPDIRECT' }">
				ADP Vendor #:<c:out value="${invoice.adpVendorNumber }"/><br/>
				</c:if>
				HST #:<c:out value="${invoice.hstNumber }"/><br/>			
				<br/><br/>	
				Date: <fmt:formatDate pattern="dd MMM yyyy" value="${invoice.invoiceDate}" />
				<br/>
				<br/><br/>			
			</div>
		</div>
		
		<div style="height: 80px; width: 100%;">
			<div class="invoice-cust-block">
				<c:out value="${invoice.customerName}"/><br/>
				<c:out value="${invoice.customerAddress.line1}"/><br/>
				<c:if test="${invoice.customerAddress.line2 != ''}">
					<c:out value="${invoice.customerAddress.line2}"/><br/>
				</c:if>
				<c:out value="${invoice.customerAddress.city}"/>&nbsp;
				<c:out value="${invoice.customerAddress.province}"/>&nbsp;
				<c:out value="${invoice.customerAddress.postalCode}"/><br/>
				<c:out value="${invoice.customerAddress.country}"/><br/>
				
			</div>
			
			<div class="invoice-gov-block">
				HEALTH CARD #:<c:out value="${invoice.healthCardNumber }"/><br/>
				BENEFIT CODE&nbsp;:<c:out value="${invoice.benefitCode}"/><br/>
				MEMBER ID #&nbsp;&nbsp;:<c:out value="${invoice.memberId}"/><br/>
				<c:if test="${invoice.company != 'CPAPDIRECT' }">
				REFERENCE #:<c:out value="${invoice.referenceNumber}"/>
				</c:if>
			</div>
		</div>
	   
		<div class="invoice-item-block" >
			
			<table style="width: 100%;">
				<tr style="width: 100%;border:1px solid #444;">
					<td class="invoice-item-heading" ">INSERT DATETIME</td>
					<td class="invoice-item-heading" ">PAYMENT DATE</td>					
					<td class="invoice-item-heading" >DESCRIPTION</td>
					<td class="invoice-item-heading" style="width: 200px;">PAYMENT METHOD</td>
					<td class="invoice-item-heading" style="width: 100px;">USER</td>
					<td class="invoice-item-heading" style="width: 100px;text-align: right;">AMT</td>
					<td class="invoice-item-heading" style="width: 100px;text-align:right;" >BALANCE</td>
					<td class="invoice-item-heading" style="width: 30px;text-align:right;" >&nbsp;</td>						
				</tr>
				<!-- original invoice balance -->
				<tr>
 						<td style="padding: 5px;">
 							<fmt:timeZone value="Canada/Eastern"> 						
 								<fmt:formatDate type="both" value="${invoice.insertDateTime}" />
 							</fmt:timeZone>   						
 						</td>
						<td style="padding: 5px;">
 							<fmt:formatDate type="date" value="${invoice.invoiceDate}" />   						
 						</td> 		 						
 						<td>
 							Amount Owed
 						</td>
 						<td  >
 							&nbsp;   					
 						</td>
 						<td>
 							<c:out value="${invoice.userName}"/>
 						</td>
 						<td class="invoice-item-numeric">
 							&nbsp;
 						</td>
 						<td class="invoice-item-numeric">
 							<c:out value="${invoice.adpPortion}"/>
 						</td>
 						<td></td>
 					</tr>				
				
				<c:forEach  begin="0" items="${invoicePayments}" varStatus="status" >
   					<tr>
   						<td style="padding: 5px;">
   						<fmt:timeZone value="Canada/Eastern"> 	
   							<fmt:formatDate type="both" value="${invoicePayments[status.count-1].insertDateTime}" />
   						</fmt:timeZone>   						
   						</td>
   						<td style="padding: 5px;">
   							<fmt:formatDate type="date" value="${invoicePayments[status.count-1].paymentDate}" />   						
   						</td>   						
   						<td>
   							<c:out value="${invoicePayments[status.count-1 ].description}" />
   						</td>
   						<td  >
   							<c:out value="${invoicePayments[status.count-1].paymentMethod}" />   					
   						</td>
   						<td>
   							<c:out value="${invoicePayments[status.count-1].userName}"/>
   						</td>
   						<td class="invoice-item-numeric">
   							<c:out value="${invoicePayments[status.count-1].amt}"/>
   						</td>
   						<td class="invoice-item-numeric">
   							<c:out value="${invoicePayments[status.count-1].balance}"/>
   						</td>
   						<td>
	   						<sec:authorize access="hasRole('ROLE_POS_ADMIN')">
	   						<a href="/pos/pymt-edit?id=<c:out value="${invoicePayments[status.count-1 ].id}" />"
								style="color: #333; text-decoration: none;" id="pymt-edit"><img
									src="/images/form.png" alt="Edit" /></a>
							</sec:authorize>		
						</td>
   					</tr>
				</c:forEach>				
			</table>		
		</div>
	 
		
</div>
<div class="invoice-buttons">
	<a href="#" onClick="window.print();return false" style="color:#FFF;" class="bluebtn">Print</a>&nbsp;&nbsp;
	<a href="/pos/invoice-adp-pymt?invoiceNumber=<c:out value="${invoice.invoiceNumber}" />" style="color:#FFF;" class="bluebtn" id="refreshBtn">Refresh</a>&nbsp;&nbsp;	
	<a href="/pos/invoice-view?invoiceNumber=<c:out value="${invoice.invoiceNumber}"/>" style="color:#FFF;" class="bluebtn">View Invoice</a>&nbsp;&nbsp;
</div>
</div>
<script>
$(function (){
  $('#pymt-edit').click(function() {
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

$(document).ready(function(){
    $(".numeric").numeric();

});

   
</script>
