<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setLocale value="en_US"/>
<%@ page session="true" %>
	<c:if test="${sessionScope.companyMode == 'CPAPDIRECT' }">
		<c:set var="isCpapMode">true</c:set>
	</c:if>
<div id="invoiceWrapper">
<div id="invoiceTitle">
<h1>
	<div id="icon-tools" class="icon32"></div>
	<span style="float:left; margin-left: 5px; margin-top: 6px;">Invoice</span>
</h1>
</div>

	
	<form:form id="invoicePymtForm" commandName="invoicePymtForm"
		action="/pos/invoice-pymt-formsubmit-v" method="POST">	
	<form:errors path="*" cssClass="errorblock" style="width: 1015px;"></form:errors>
	<c:if test="${message!=null}">
		<div class="messageblock" style="width: 1070px;">${message}</div>
	</c:if>
	<c:if test="${invoice.status=='PARTIAL_PAYMENT' }">
		<div class="invoice-summary-block" >
		<div>Invoice Payment</div>
	
				<form:hidden path="invoiceNumber" />
				<div class="invoiceSetupCol" style="width: 40%;">
				<ul>
					<li><label style="width: 200px; padding-left: 10px;">Remaining Balance:</label>
						<div id="balanceRemaining"><c:out value="${invoice.balanceRemaining}" /></div>
						<div id="balanceRemainingCash" ><c:out value="${cashRemainingBal}" /></div>						
					</li>		
					<li><label style="width: 200px; padding-left: 10px;">Payment Amount:</label>
						<form:input path="paymentAmount" class="numeric" />
					</li>	
					<li><label style="width: 200px; padding-left: 10px;">Payment Method:</label>
						 <form:select path="paymentMethod" id="paymentMethod">
							<form:options items="${paymentMethods}" itemLabel="name" itemValue="value" />
						</form:select></li>
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
					<input type="submit" name="submit-view" value="submit" class="bluebtn" />
				</div>
				<br>
				<br>
		</div>
	</c:if>
	
			
	<div class="invoice-block">	
	
		<div style="height: 130px; width: auto;">
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
				
				<c:if test="${isCpapMode == 'true' }">		
				ADP Vendor #:<c:out value="${invoice.adpVendorNumber }"/><br/>
				</c:if>
				HST #:<c:out value="${invoice.hstNumber }"/>			
				<br /> <br /><br/>	
					Date: <fmt:formatDate pattern="dd MMM yyyy" value="${invoice.invoiceDate}" />
				<br/><br/><br/>			
			</div>
		</div>
		
		<div style="height: 80px; width:  auto;">
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
				<c:out value="${invoice.customerPhone }"/><br/>
			</div>
			
			<div class="invoice-gov-block">
				HEALTH CARD #:<c:out value="${invoice.healthCardNumber }"/><br/>
				<c:if test="${isCpapMode == 'true' }">		
				BENEFIT CODE&nbsp;:<c:out value="${invoice.benefitCode}"/><br/>
				MEMBER ID #&nbsp;&nbsp;:<c:out value="${invoice.memberId}"/><br/>
				</c:if>
			</div>
		</div>
	
		<div class="invoice-item-block" >			
			<table style="width: 1015px; ">
				<tr style="border-bottom: 1px solid #444;">
					<td class="invoice-item-heading" style="width: 390px;">PRODUCT</td>
					<td class="invoice-item-heading" style="width: 160px;">REF NO.</td>
					<td class="invoice-item-heading" style="width: 140px;">SERIAL NO.</td>
					<td class="invoice-item-heading" style="width: 40px; text-align: center;">QTY</td>
					<td class="invoice-item-heading" style="width: 90px; text-align: right;">UNIT PRICE</td>
					<td class="invoice-item-heading" style="width: 90px;  text-align: right;">TOTAL</td>
					<td style="width: 16px;"></td>
				</tr>
				
				<c:forEach  begin="0" items="${invoiceItems }" varStatus="status" >
   					<tr>
   						<td class="invoice-item-row">
							<c:if test="${invoiceItems[status.count-1].bypassInvAdj}">
								<img src="/images/no-inv-adj.jpg"  style="width: 13px; height: 16px;vertical-align:bottom;display: inline-block;"/>
							</c:if>   						
   							<c:out value="${invoiceItems[status.count-1].productName}" />
							<c:if test="${invoiceItems[status.count-1].description  != '' }">
								<br/>	
								<font size="2">	<c:out value="${invoiceItems[status.count-1 ].description}" escapeXml="false"  /></font>
							</c:if>
							<c:if test="${invoiceItems[status.count-1].lotNumber  != ''}">
								<br/>	
								<font size="2">Lot Number:<c:out value="${invoiceItems[status.count-1 ].lotNumber}" /></font>.
							</c:if>
							<c:if test="${invoiceItems[status.count-1].additionalSerial  != ''}">
								<font size="2">Humidifier Serial:<c:out value="${invoiceItems[status.count-1 ].additionalSerial}" /></font>
							</c:if>				
   						</td>
   						<td class="invoice-item-row" style="width: 160px;">
   							<c:out value="${invoiceItems[status.count-1].productReference}"/>
   						</td>   						
   						<td  class="invoice-item-row" style="width: 140px;">
   							<c:out value="${invoiceItems[status.count-1 ].productSerialNumber}" />
   						</td>
   						<td  class="invoice-item-row" style="text-align:center;" >
   							<c:out value="${invoiceItems[status.count-1].qty}" />   					
   						</td>
   						<td class="invoice-item-numeric" style="text-align: right;">
   							<c:out value="${invoiceItems[status.count-1].price}"/>
							<c:choose>
								<c:when test="${invoiceItems[status.count-1].priceTier  == 'TIER_2'}">*	</c:when>
								<c:otherwise>&nbsp;</c:otherwise>
							</c:choose>	   							
   						</td>
   						<td class="invoice-item-numeric" style="text-align:right;">
   							<c:out value="${invoiceItems[status.count-1].total}"/>
   						</td>
   						<td class="invoice-item-numeric"><!-- trash can goes here -->  					
   						</td>
   					</tr>
				</c:forEach>				
			</table>		
		</div>
	
	<div >
		<div class="invoice-cpap-info-block" style="width: 50%;">
			<c:choose>
			<c:when   test="${isCpapMode == 'true' }">		
			*ADP is the Assistive Devices branch of the Ontario Ministry of Health<br/>
			<b>CPAP / AutoPAP / BiPAP machine warranty + humidifier</b>: <c:out value="${invoice.machineWarranty}"/><br/>
			<b>Mask warranty</b>: <c:out value="${invoice.maskWarranty}"/><br/>
			</c:when>
			<c:otherwise>
				<br/><br/><br/>
			</c:otherwise>
			</c:choose>
				<br/><b>Notes:</b><br/>
				<c:out value="${invoice.remark1}" /><br/>
				<c:out value="${invoice.remark2}"/><br/>
			<br/><b>Prepared by</b>:
			<c:out value="${invoice.userName}"/><br/><br/>
			<b>Payment Method</b>: <c:out value="${invoice.paymentMethod}"/><br/>
			<b>Full Payment?</b>:<span><c:out value="${invoice.displayStatus}"/></span>
			<br/><br/><br/>
			<b>Client Signature</b>: ___________________________________________
		
		</div>
		<div class="invoice-totals-block">
			<table style="width: 100%;">
					<c:if test="${invoice.redemCode  != null &&  invoice.redemCode  != '0.00'}">			
					<tr style="">
						<td class="invoice-totals-item">REDEM. CODE %:</td>
						<td  class="invoice-totals-item">
							<fmt:formatNumber value="${invoice.redemCode}" type="number" />%					
						</td>
						<td style="vertical-align: bottom; text-align: center;">
							<!-- refresh icon goes here -->
   						</td>
					</tr>
					</c:if>			
					<tr>
						<td class="invoice-totals-item">SUBTOTAL:</td>
						<td class="invoice-totals-item" style="width:100px;">
							<fmt:formatNumber value="${invoice.subTotal }" type="currency"/>
						</td>
						<td style="width:29px">&nbsp;</td>
					</tr>
					<tr style="">
						<td class="invoice-totals-item">SHIPPING:</td>
						<td  class="invoice-totals-item">
							<fmt:formatNumber value="${invoice.shippingTotal}" type="number" />				
						</td>
						<td style="vertical-align: bottom; text-align: center;">
							<!-- refresh icon goes here -->
   						</td>
					</tr>						
					<tr style="">
						<td class="invoice-totals-item">HST:</td>
						<td class="invoice-totals-item">
							<fmt:formatNumber value="${invoice.hst }" type="currency"/>	
						</td>
					</tr>
					<tr style="">
						<td class="invoice-totals-item">TOTAL:</td>
						<td class="invoice-totals-item">
							<fmt:formatNumber value="${invoice.total }" type="currency" />	
						</td>
					</tr>
					<c:if test="${isCpapMode == 'true' }">							
					<tr style="">
						<td class="invoice-totals-item">Less ADP PORTION:</td>
						<td class="invoice-totals-item">
							<fmt:formatNumber value="${invoice.adpPortion }" type="currency"/>	
						</td>
					</tr>	
					</c:if>
					<c:if test="${invoice.creditItemTotal  != null &&  invoice.creditItemTotal  != '0.00'}">
					<tr style="">
						<td class="invoice-totals-item">Less CLIENT CREDIT:</td>
						<td class="invoice-totals-item">
							<fmt:formatNumber value="${invoice.creditItemTotal }" type="currency"/>	
						</td>
					</tr>			
					</c:if>		
					<tr style="">
						<td class="invoice-totals-item">PROMO CODE:</td>
						<td class="invoice-totals-item">
							-<fmt:formatNumber value="${invoice.promoCode }" type="currency" />	
						</td>
					</tr>				
					<tr style="">
						<td class="invoice-totals-item">CLIENT PORTION:</td>
						<td class="invoice-totals-item">
							<fmt:formatNumber value="${invoice.clientPortion }" type="currency"/>	
						</td>
					</tr>
			</table>			
		</div>	
	</div>
</div> 
	<div class="invoicenote-block" style="width: 1050px; padding: 10px;">
		<div class="invoiceSetupColWrapper">
			<div class="invoiceSetupCol" style="">
				Discount Reason: 
				<c:out value="${discountReasons[invoice.discountReason].reason}"/><br/>
				Internal Note:
				<c:out value="${invoice.discountNote}" /><br/>			
				<c:if test="${isCpapMode == 'true' }">			
				Referenced Invoice Number:
				<a href="/pos/invoice-view?invoiceNumber=<c:out value="${invoice.referencedInvoiceNumber}"/>"><c:out value="${invoice.referencedInvoiceNumber}"/></a>
				<br/><br/>
				Reference Invoice Carryover Credit:
				<fmt:formatNumber value="${invoice.refInvoiceCredit }" type="currency" />		
				<br/>
				Adjusted Client Portion:
				<c:choose>
				<c:when test="${invoice.adjInvoiceClientPortion != invoice.clientPortion}">
					<font color="red">
				</c:when>
				<c:otherwise>
					<font color="black">
				</c:otherwise>						
				</c:choose>		
					<fmt:formatNumber value="${invoice.adjInvoiceClientPortion }" type="currency" />
					</font>		
				</c:if>
			</div>
		</div>			
	</div>
<div class="invoice-salesfinal">
	All Medical Equipment sales are Final - No Refunds or Exchanges
</div>
</div>
<div class="invoice-buttons">
	<a href="#" onClick="window.print();return false" style="color:#FFF;" class="bluebtn">Print</a>&nbsp;&nbsp;
	<a href="/pos/invoice-view?invoiceNumber=<c:out value="${invoice.invoiceNumber}"/>" style="color:#FFF;" class="bluebtn">Refresh</a>&nbsp;&nbsp;	
	<a href="/pos/invoice-pymt?invoiceNumber=<c:out value="${invoice.invoiceNumber}"/>" style="color:#FFF;" class="bluebtn">Payment History</a>&nbsp;&nbsp;
	
	<c:if test="${isCpapMode == 'true'}">
		<c:if test="${invoice.invoiceType =='STANDARD'}">
			<a href="/pos/invoice-adp-pymt?invoiceNumber=<c:out value="${invoice.invoiceNumber}"/>" style="color:#FFF;" class="bluebtn">ADP Payment History</a>&nbsp;&nbsp;
		</c:if>
		
		<c:if test="${invoice.invoiceType=='TRIAL'  && invoice.status !='VOID' && invoice.status !='TRIAL_ENDED'}">
			<a href="/pos/invoice-trial-refund-form?invoiceNumber=<c:out value="${invoice.invoiceNumber}"/>" style="color:#FFF;" class="bluebtn">Refund Trial Deposit</a>&nbsp;&nbsp;
			<a href="/pos/invoice-trial2purchase-form?invoiceNumber=<c:out value="${invoice.invoiceNumber}"/>" style="color:#FFF;" class="bluebtn">Convert Trial to Purchase </a>&nbsp;&nbsp;
		</c:if>
	</c:if>
	
	<c:if test="${customerId != null }">	
		<a href="/customer/tabview?id=<c:out value="${customerId}"/>" style="color:#FFF;" id="profileOpen" class="bluebtn">Customer Profile</a>&nbsp;&nbsp;
		
		<c:if test="${isCpapMode == 'true'  && invoice.invoiceType=='STANDARD'}">		
		<a href="/pos/generateGovPdf?invoiceNumber=<c:out value="${invoice.invoiceNumber}"/>" style="color:#FFF;" class="bluebtn">ADP Form</a>&nbsp;&nbsp;	
		</c:if>
	</c:if>
	
	<c:if test="${isCpapMode == 'true'  && !allReturned && (invoice.invoiceType == 'STANDARD' || invoice.invoiceType == 'OTHER')}">
		<a href="/pos/invoice-sales-return?invoiceNumber=<c:out value="${invoice.invoiceNumber}"/>" id="returns" style="color:#FFF;"  class="bluebtn">Sales Return</a>

	</c:if>
	
	<sec:authorize access="hasRole('ROLE_POS_ADMIN')">
	<c:if test="${invoice.status!='VOID' }">&nbsp;&nbsp;&nbsp;
		<a href="/pos/invoice-edit?invoiceNumber=<c:out value="${invoice.invoiceNumber}"/>" id="edit" style="color:#FFF;" class="bluebtn">Admin Edit</a>
		&nbsp;&nbsp;
		<a href="/pos/invoice-void-form?invoiceNumber=<c:out value="${invoice.invoiceNumber}"/>" id="void" style="color:#FFF;" class="bluebtn">Void Invoice</a>
	</c:if>
	</sec:authorize>
	
</div>
</form:form>
<script>
$(function (){
  $('#void,#edit').click(function() {
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
$("#profileOpen").click(function(e) {
	e.preventDefault();
	var url = $(this).attr('href');
	var title = $(this).attr('title');
	$('#mainbody').load(url,function(response, status, xhr) {
		  if (status == "error") {
			  	if(xhr.status==403){
			  		//alert($("#access-deneid"));
			  		$("#access-deneid").dialog("open");
			  	}
			  }
			});
	return false;
});
$(document).ready(function(){
    $(".numeric").numeric();
    

    var selected_item = $("#paymentMethod").val();
    if(selected_item == "Cash"){

        $("#balanceRemaining").css('display','none');
         $("#balanceRemainingCash").css('display','inline');
   }else{
        
        $("#balanceRemaining").css('display','inline');
        $("#balanceRemainingCash").css('display','none');
   }
});

$('#paymentMethod').change(function() {
    
    var selected_item = $(this).val();
     if(selected_item == "Cash"){

         $("#balanceRemaining").css('display','none');
          $("#balanceRemainingCash").css('display','inline');
    }else{
         
         $("#balanceRemaining").css('display','inline');
          $("#balanceRemainingCash").css('display','none');
    }
});
   
</script>
