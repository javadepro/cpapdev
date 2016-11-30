<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page session="true" %>
	<c:if test="${sessionScope.companyMode == 'CPAPDIRECT' }">
		<c:set var="isCpapMode">true</c:set>
	</c:if>
<div id="invoiceWrapper">
<div id="invoiceTitle">
<h1>
	<div id="icon-tools" class="icon32"></div>
	<span style="float: left; margin-left: 5px; margin-top: 6px;">POS</span>
</h1>
</div>

<sec:authorize access="hasRole('ROLE_POS_CD_ADMIN')">				
	<c:set var="isPosCdAdmin">true</c:set>
</sec:authorize>


<form:form id="invoice-form" commandName="invoiceForm"
	action="/pos/invoice-formsubmit" method="POST">

<p class="pageDesc">
<c:if test="${isCpapMode == 'true'  }">
<c:if test="${invoiceForm.invoice.invoiceType == '' }">
First select an Invoice Type.  Standard is an invoice for selling the customer products.  To scan a Rental Fee or a Report Fee, please switch to Invoice Type= 'Other'.

Trial fees are NOT to be handled in this. It should be handled in Trial Mode instead by selecting the customer first.
</c:if>
<c:if test="${invoiceForm.invoice.invoiceType == 'TRIAL_REFUND' }">
You need to scan in a trial fee in order to complete the trial refund.
</c:if>
<c:if test="${invoiceForm.invoice.invoiceType == 'STANDARD' }">
When converting from a trial to a purchase, it is required to have the mask and the trial credit.  To scan a Rental Fee or a Report Fee, please switch to Invoice Type= 'Other'.
</c:if>
<c:if test="${invoiceForm.invoice.invoiceType == 'OTHER' }">
This is only used for special fees only.  Enter the fee amount into the Service Price field.
</c:if>

<c:if test="${invoiceForm.invoice.invoiceType == 'TRIAL' }">
you need to scan the trial deposit and the mask to be used in order to complete the trial invoice.
</c:if>
</c:if>
</p>

	<form:errors path="*" cssClass="errorblock" style="width: 1050px;"></form:errors>
	<c:if test="${message!=null}">
		<div class="messageblock" style="width: 1070px;">${message}</div>
	</c:if>
	<c:if test="${warning!=null}">
		<div class="warningblock" style="width: 1000px;">${warning}</div>
	</c:if>



	<form:hidden path="customerId" />
	<div class="invoicesetup-block" style="width: 1070px; height: 110px">
		<div>Invoice Setup</div>

		<div class="invoiceSetupColWrapper">
			<div class="invoiceSetupCol">
				<form:hidden path="invoice.company"/>			
				<form:hidden path="ctp"/>				
				<ul>				
					<li>
						<label style="width: 100px;">Invoice Type:</label>						
							<c:choose>
								<c:when test="${invoiceForm.invoice.invoiceType ==''}">
									<form:select path="invoice.invoiceType">
										<form:option value="" label=""/>
										<form:options items="${invoiceTypes}" />
									</form:select>								
								</c:when>
								<c:otherwise>
									<form:input path="invoice.invoiceType" readonly="true" cssStyle="border: 0px;"/>
								</c:otherwise>
							</c:choose>
					</li>
					<li><label style="width: 100px;">Clinic:</label> <form:select
							path="location">							
							<form:options items="${shops}" itemLabel="name" />
						</form:select></li>			
				</ul>
			</div>
			<div class="invoiceSetupCol" style="width: 38%;">
				<input type="submit" name="actionType" value="update"
					class="bluebtn" />
			</div>
		</div>
	</div>
	<div class="productadd-block" style="width: 1050px; padding: 10px;">
		<div class="invoiceSetupColWrapper">
			<div class="invoiceSetupCol" style="">
				Barcode:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<form:input path="barcode" />
			</div>
			<div class="invoiceSetupCol" style="">
				Quantity:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<form:input path="qty" class="numeric" />
			</div>			
			<div class="invoiceSetupCol" style="">
				Pricing Tier:&nbsp;
				<form:select path="priceTier">
					<form:options items="${priceTierList}" />
				</form:select>	
			</div>
			<br>
			<br>
		</div>
		<div class="invoiceSetupColWrapper">
			<c:if test="${isCpapMode == 'true' }">		
			<div class="invoiceSetupCol" style="">
				Product Serial (opt):
				<form:input path="serial" />
			</div>
			<div class="invoiceSetupCol" style="">
				Service Price:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<form:input path="servicePrice" class="numeric" />
			</div>		
			</c:if>
			<div class="invoiceSetupCol" style="width: 8%">
				&nbsp;&nbsp;<input type="submit" name="actionType" value="add" class="bluebtn" />
			</div>
			<br>
			<br>			
		</div>		
		<c:if test="${isCpapMode == 'true' }">		
		<div class="invoiceSetupColWrapper">
			<div class="invoiceSetupCol" style="">
				Lot Number (opt):&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<form:input path="lotNumber" />
			</div>
			<div class="invoiceSetupCol" style="width:33%">
				Humidifier Serial (pkg only):
				<form:input path="additionalSerial"/> 
			</div>		
			<div class="invoiceSetupCol" style="width: 33%">
				<c:choose>
				<c:when test="${invoiceForm.ctp}">
				Do Not Adjust Inventory:				
					<input type="checkbox" name="bypassInvAdj" <c:if test="${invoiceForm.bypassInvAdj }">checked="checked"</c:if>/>				
				</c:when>
				</c:choose>
			</div>			
		</div>			
		</c:if>
	</div>

	<div class="invoice-block" >

		<div style="height: 130px; width: auto;">
			<div class="invoice-logo-block">
				<img src="/images/logo_${invoiceForm.invoice.company}.png"  class="invoice-logo-img" />
			</div>
			<div class="invoice-addr-block">
				<c:out value="${invoiceForm.invoice.shopAddress.line1}" /><br />
				<c:if test="${invoiceForm.invoice.shopAddress.line2 != ''}">
					<c:out value="${invoiceForm.invoice.shopAddress.line2}" /><br />
				</c:if>
				<c:out value="${invoiceForm.invoice.shopAddress.city}" />,
				<c:out value="${invoiceForm.invoice.shopAddress.province}" />&nbsp;
				<c:out value="${invoiceForm.invoice.shopAddress.postalCode}" /><br />
				P: <c:out value="${invoiceForm.invoice.shopPhone}" />&nbsp;&nbsp;&nbsp;
				F: <c:out value="${invoiceForm.invoice.shopFax}" /><br/>
				<c:if test="${invoice.company == 'CPAPDIRECT' }">
				www.cpapdirect.ca
				</c:if>

				<form:hidden path="invoice.shopAddress.line1" />
				<form:hidden path="invoice.shopAddress.line2" />
				<form:hidden path="invoice.shopAddress.city" />
				<form:hidden path="invoice.shopAddress.province" />
				<form:hidden path="invoice.shopAddress.postalCode" />
				<form:hidden path="invoice.shopAddress.country" />
				<form:hidden path="invoice.shopPhone" />
				<form:hidden path="invoice.shopFax" />
				<form:hidden path="invoice.shopName"/>
				<form:hidden path="invoice.shopKey"/>
			</div>
			<div class="invoice-meta-block">
				<font style="font-weight: bold; font-size: 16px;">INVOICE #:<c:out
						value="${invoiceForm.invoice.invoiceNumber}" /></font><br />

				<c:if test="${isCpapMode == 'true' }">		
				ADP Vendor #:<c:out value="${invoiceForm.invoice.adpVendorNumber }" />
					<form:hidden path="invoice.adpVendorNumber" />
					<br />
				</c:if>
				HST #:<c:out value="${invoiceForm.invoice.hstNumber }" />
				<form:hidden path="invoice.hstNumber" />
				<br /> <br /><br/>

				Date: 
				<c:choose>
					<c:when test="${isPosCdAdmin == 'true'}" >
						<form:input 
						path="invoice.invoiceDate" class="datepicker" placeholder="dd/MM/yyyy" />
					</c:when>
					<c:otherwise>
						<fmt:timeZone value="Canada/Eastern"> 	
							<fmt:formatDate pattern="dd MMM yyyy" value="${invoiceForm.invoice.invoiceDate}" />
						</fmt:timeZone>
						<form:hidden path="invoice.invoiceDate" />				
					</c:otherwise>
				</c:choose>
				<br /> <br /><br />
			</div>
		</div>

		<div style="height: 80px; width: auto;">
			<div class="invoice-cust-block">	
				<c:out value="${invoiceForm.invoice.customerName}" /><br />
				<c:out value="${invoiceForm.invoice.customerAddress.line1}" /><br />
				<c:if test="${invoiceForm.invoice.customerAddress.line2 != ''}">
					<c:out value="${invoiceForm.invoice.customerAddress.line2}" /><br />
				</c:if>
				<c:out value="${invoiceForm.invoice.customerAddress.city}" />&nbsp;
				<c:out value="${invoiceForm.invoice.customerAddress.province}" />&nbsp;
				<c:out value="${invoiceForm.invoice.customerAddress.postalCode}" /><br />
				<c:out value="${invoiceForm.invoice.customerAddress.country}" /><br />
				<c:out value="${invoiceForm.invoice.customerPhone }"/><br/>
				<form:hidden path="invoice.customerFirstName" />
				<form:hidden path="invoice.customerLastName" />
				<form:hidden path="invoice.customerAddress.line1" />
				<form:hidden path="invoice.customerAddress.line2" />
				<form:hidden path="invoice.customerAddress.city" />
				<form:hidden path="invoice.customerAddress.province" />
				<form:hidden path="invoice.customerAddress.postalCode" />
				<form:hidden path="invoice.customerAddress.country" />
				<form:hidden path="invoice.customerPhone"/>
			</div>

			<div class="invoice-gov-block">
				HEALTH CARD #:<c:out value="${invoiceForm.invoice.healthCardNumber }" /><br /> 
				
				<c:if test="${isCpapMode == 'true' }">		
				BENEFIT CODE&nbsp;:
		
				<c:choose>
					<c:when test="${invoiceForm.invoice.customerKey == null}">
					<form:select
						path="invoice.benefitCode">
						<form:options items="${benefitCodes}" />
					</form:select>
					<input
					type="submit" name="actionType" value="calculateTotals"
					class="invoice-img-submit"
					style="background: url(/images/reload_icon.png) no-repeat; width: 16px; height: 16px;" />						
					</c:when>
					<c:otherwise>
						<c:out value="${invoiceForm.invoice.benefitCode }" />	
						<form:hidden path="invoice.benefitCode" />				
					</c:otherwise>
				</c:choose>
				<br /> MEMBER ID #&nbsp;&nbsp;:<c:out value="${invoiceForm.invoice.memberId}" /><br />
				</c:if>
				<form:hidden path="invoice.healthCardNumber" />				
				<form:hidden path="invoice.memberId" />
			</div>
		</div>

		<div class="invoice-item-block">

			<table style="width: 1039px; ">
				<tr style="border-bottom: 1px solid #444;">
					<td class="invoice-item-heading" style="width: 390px; ">PRODUCT</td>
					<td class="invoice-item-heading" style="width: 210px; ">REF NO.</td>
					<td class="invoice-item-heading" style="width: 160px; ">SERIAL NO.</td>
					<td class="invoice-item-heading" style="width: 40px; text-align: center;">QTY</td>
					<td class="invoice-item-heading" style="width: 90px; text-align: right;">UNIT PRICE</td>
					<td class="invoice-item-heading" style="width: 90px; text-align: right;">TOTAL</td>
					<td style="width: 16px;"></td>
				</tr>

				<c:forEach begin="0" items="${invoiceForm.invoiceItems }" varStatus="status">
					<tr>
						<td class="invoice-item-row" style="padding-left: 1px;">
							<form:hidden path="invoiceItems[${status.count-1 }].order" /> 
							<form:hidden path="invoiceItems[${status.count-1 }].productKey" />
							<form:hidden path="invoiceItems[${status.count-1}].bypassInvAdj"/>
							<form:hidden path="invoiceItems[${status.count-1}].priceTier"/>
						 	<c:if test="${invoiceForm.invoiceItems[status.count-1].bypassInvAdj}">
								<img src="/images/no-inv-adj.jpg"  style="width: 13px; height: 16px;vertical-align:bottom;display: inline-block;"/>
							</c:if>	<form:input
								path="invoiceItems[${status.count-1 }].productName"
								readonly="true" cssStyle="width: 280px;border: 0px;" />								
							<c:if test="${invoiceForm.invoiceItems[status.count-1].description  != ''  }">
								<br/>	
								<form:hidden path="invoiceItems[${status.count-1 }].description" />
								<font size="2"><c:out value="${invoiceForm.invoiceItems[status.count-1 ].description}" escapeXml="false"  /></font>
							</c:if>
							<c:if test="${invoiceForm.invoiceItems[status.count-1].lotNumber  != ''}">
								<br/>	
								<form:hidden path="invoiceItems[${status.count-1 }].lotNumber" />
								<font size="2">	Lot Number: <c:out value="${invoiceForm.invoiceItems[status.count-1 ].lotNumber}"   />.</font>
							</c:if>			
							<c:if test="${invoiceForm.invoiceItems[status.count-1].additionalSerial  != ''}">
								<form:hidden path="invoiceItems[${status.count-1 }].additionalSerial" />
								<font size="2">	Humidifier Serial: <c:out value="${invoiceForm.invoiceItems[status.count-1 ].additionalSerial}"   /></font>
							</c:if>													
						</td>
						<td class="invoice-item-row" >
							<form:input
								path="invoiceItems[${status.count-1 }].productReference"
								readonly="true" cssStyle="width: 160px;border: 0px;" />
						</td>										
						<td class="invoice-item-row">
							<form:input
								path="invoiceItems[${status.count-1 }].productSerialNumber"
								readonly="true" cssStyle="width: 160px;border: 0px;" />
						</td>
						<td class="invoice-item-row" style="text-align:center;">
							<form:input path="invoiceItems[${status.count-1 }].qty"
								readonly="true" cssClass="invoice-item-input"
								cssStyle="width: 40px;border:  0px; text-align:center;" />
						</td>
						<td class="invoice-item-numeric" style="text-align: right;">
							<form:input
								path="invoiceItems[${status.count-1 }].price" readonly="true"
								cssClass="invoice-item-input" cssStyle="border: 0px;" />
							<c:choose>
								<c:when test="${invoiceForm.invoiceItems[status.count-1].priceTier  == 'TIER_2'}">*	</c:when>
								<c:otherwise>&nbsp;</c:otherwise>
							</c:choose>							
						</td>
						<td class="invoice-item-numeric" style="text-align: right;" >
							<form:input
								path="invoiceItems[${status.count-1 }].total" readonly="true"
								cssClass="invoice-item-input" cssStyle="border: 0px;" />
						</td>
						<td class="invoice-item-numeric" style="padding-left: 0px;">						
						<input type="submit"
							name="actionType" value="delete_${status.count-1}"
							class="invoice-img-submit"
							style="background: url(/images/trash.gif) no-repeat; width: 16px; height: 16px;" />
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>

		<div>
			<div class="invoice-cpap-info-block" style="width: 50%;">
			
				<c:choose>
					<c:when   test="${isCpapMode == 'true' }">		
					*ADP is the Assistive Devices branch of the Ontario Ministry of Health<br /> 
					<b>CPAP / AutoPAP / BiPAP machine warranty  + humidifier</b>:
					<form:radiobuttons items="${machineWarranties}"
						path="invoice.machineWarranty" itemLabel="name" itemValue="value" cssClass="invoice-radio-buttons" />
					<br/><b>Mask warranty</b>: 
					<form:radiobuttons items="${maskWarranties}"
						path="invoice.maskWarranty" itemLabel="name" itemValue="value" cssClass="invoice-radio-buttons" />
					</c:when>
					<c:otherwise>
						<br/><br/><br/>
						<form:hidden path="invoice.machineWarranty" value="N/A"/>
						<form:hidden path="invoice.maskWarranty" value="N/A"/>
					</c:otherwise>
				</c:choose>
				<br/><b>Notes:</b><br/>
				<form:input path="invoice.remark1" style="width:500px;" title="remark1" maxlength="70" /><br/>
				<form:input path="invoice.remark2" style="width:500px;" title="remark2"  maxlength="70"/><br/>
				<br/><b>Prepared by</b>:
				<c:choose>
					<c:when test="${isPosCdAdmin}">
						<form:select
							path="preparedByUserKey">							
							<form:options items="${clinicians}" itemLabel="name" />
						</form:select>
					</c:when>
					<c:otherwise>
						<form:input path="invoice.userName" readonly="true"
							style="border: none;" />
						<form:hidden path="invoice.userFirstName" />
						<form:hidden path="invoice.userLastName" />							
					</c:otherwise>
				</c:choose>

				<br /> <br /> <b>Payment Method</b>:
				<form:radiobuttons items="${paymentMethods}"
					path="invoice.paymentMethod" itemLabel="name" itemValue="value"
					cssClass="invoice-radio-buttons" />
				<br /> <b>Full Payment?</b>: <span> 
				
					<c:if test="${isCpapMode == 'true' }">							
					<form:radiobutton path="invoice.status" label="Full Payment" value="FULL_PAYMENT" cssClass="invoice-radio-buttons"   /> 
					<form:radiobutton path="invoice.status" label="Partial Payment" value="PARTIAL_PAYMENT" cssClass="invoice-radio-buttons"  />
					</c:if>
					<c:if test="${isCpapMode != 'true' }">Full Payment&nbsp;
					<form:hidden path="invoice.status" value="FULL_PAYMENT"/>							 
					</c:if>					
					
				</span>
				<br/><br/><br/>
			<b>Client Signature</b>: _________________________________________________

			</div>
			<div class="invoice-totals-block">
				<table style="width: 100%;">					
					<tr>
						<td class="invoice-totals-item">SUBTOTAL:</td>
						<td class="invoice-totals-item" style="width: 100px;">
							<fmt:formatNumber value="${invoiceForm.invoice.subTotal }" type="currency" />
							<form:hidden path="invoice.subTotal" />
						</td>
						<td style="width: 29px">&nbsp;</td>
					</tr>
					<tr>
						<td class="invoice-totals-item">SHIPPING:</td>
						<td class="invoice-totals-item">
							<form:input path="invoice.shippingTotal" size="9" style="text-align: right;" class="numeric" />
						</td>						
						<td style="vertical-align: bottom; text-align: center;">
							<input
							type="submit" name="actionType" value="calculateTotals"
							class="invoice-img-submit"
							style="background: url(/images/reload_icon.png) no-repeat; width: 16px; height: 16px;" />
						</td>
					</tr>					
					<tr style="">
						<td class="invoice-totals-item">HST:</td>
						<td class="invoice-totals-item">
							<fmt:formatNumber value="${invoiceForm.invoice.hst }" type="currency" /> 
							<form:hidden path="invoice.hst" />
						</td>
					</tr>					
					<tr style="">
						<td class="invoice-totals-item">TOTAL:</td>
						<td class="invoice-totals-item">
							<fmt:formatNumber value="${invoiceForm.invoice.total }" type="currency" /> 
							<form:hidden path="invoice.total" />
						</td>
					</tr>
					<c:if test="${isCpapMode == 'true' }">		
					<tr style="">
						<td class="invoice-totals-item">Less ADP PORTION:</td>
						<td class="invoice-totals-item">
							<fmt:formatNumber value="${invoiceForm.invoice.adpPortion }" type="currency" /> 
							<form:hidden path="invoice.adpPortion" />
						</td>
					</tr>		
					</c:if>
					<tr style="">
						<td class="invoice-totals-item">PROMO CODE:</td>
						<td class="invoice-totals-item">-<form:input
								path="invoice.promoCode" size="9" style="text-align: right;" class="numeric" /></td>
						<td style="vertical-align: bottom; text-align: center;"><input
							type="submit" name="actionType" value="calculateTotals"
							class="invoice-img-submit"
							style="background: url(/images/reload_icon.png) no-repeat; width: 16px; height: 16px;" />
						</td>
					</tr>	
					<tr style="">
						<td class="invoice-totals-item">CLIENT PORTION:</td>
						<td class="invoice-totals-item">
							<fmt:formatNumber value="${invoiceForm.invoice.clientPortion }" type="currency" />
							<form:hidden path="invoice.clientPortion" />
						</td>
					</tr>
				</table>
			</div>
		</div>
</div>
	<div class="invoicenote-block" style="width: 1050px; padding: 10px;">
		Please enter your reason for using a promo/redem code or any other important notes. (100 char max)
		<div class="invoiceSetupColWrapper">
			<div class="invoiceSetupCol" style="">
				Discount Reason: 
				<form:select path="invoice.discountReason" >
					<form:option value="" />
					<form:options items="${discountReasons}" itemLabel="reason" />
				</form:select>
				<br/>
				Internal Note:
				<form:input path="invoice.discountNote" style="width:700px;" maxlength="100"  />
				</br></br/>
				
				<c:if test="${isCpapMode == 'true' }">		
				Referenced Invoice Number:
				<c:out value="${invoiceForm.invoice.referencedInvoiceNumber}"/>
				<form:hidden path="invoice.referencedInvoiceNumber"/>
				<br/><br/><br/>
				
				Reference Invoice Carryover Credit:
				<fmt:formatNumber value="${invoiceForm.invoice.refInvoiceCredit }" type="currency" />				
				<form:hidden path="invoice.refInvoiceCredit"/>
				<br/>
				Adjusted Client Portion:
				<c:choose>
					<c:when test="${invoiceForm.invoice.adjInvoiceClientPortion != invoiceForm.invoice.clientPortion}">
						<font color="red">
					</c:when>
					<c:otherwise>
						<font color="black">
					</c:otherwise>
					
				</c:choose>
						<fmt:formatNumber value="${invoiceForm.invoice.adjInvoiceClientPortion }" type="currency" />
					</font>
				
				<form:hidden path="invoice.adjInvoiceClientPortion"/>	
				</c:if>			
			</div>
		</div>			
	</div>
	
	<c:if test="${invoiceForm.invoice.invoiceType =='SALES_RETURN'}">
	<form:hidden path="reqMgrPasscode"/>
	<c:choose>
		<c:when test="${invoiceForm.invoice.passcodeHash !='' && invoiceForm.invoice.passcodeHash != null}">
			<div class="invoicenote-block" style="width: 1050px; padding: 10px;">			
				<div class="invoiceSetupColWrapper">
					<div class="invoiceSetupCol" style="">
						<c:out value="${posMgrPasscode.crmUser.name}" />
						<form:hidden path="posMgrPasscode.crmUser"/>
						<form:hidden path="invoice.passcodeHash" />
						Manager Passcode Accepted.
					</div>
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<div class="invoicenote-block" style="width: 1050px; padding: 10px;">
				If you have a used a barcode that requires manager override, please have the manager
				type the passcode in here.
				<div class="invoiceSetupColWrapper">
					<div class="invoiceSetupCol" style="">
					<br/>
						Manager: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<form:select path="posMgrPasscode.crmUser" >
							<form:option value="" />
							<form:options items="${posMgrs}" itemLabel="name" />
						</form:select>
						<br/><br/>
						Manager Passcode: 
						<form:input path="posMgrPasscode.passCode" maxlength="100"  />
						<input
							type="submit" name="actionType" value="updateMgrPasscode"
							class="invoice-img-submit"
							style="background: url(/images/reload_icon.png) no-repeat; width: 16px; height: 16px;" />									
					</div>
				</div>			
			</div>			
		</c:otherwise>
	</c:choose>
		</c:if>

</div>
<div class="invoice-buttons">
<c:if test="${customer.id != null }">	
	<a href="/customer/tabview?id=<c:out value="${customer.id}"/>" style="color:#FFF;" id="profileOpen" class="bluebtn">Cancel</a>&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</c:if>
<c:if test="${customer.id == null }">	
	<a href="/pos/invoice-form" style="color:#FFF;"  class="bluebtn">Cancel</a>&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</c:if>
		
	<form:hidden path="maxAdpAmtBeforeBenefitDiscount"/>
	<input type="submit" name="actionType" value="Save" class="bluebtn" />&nbsp;
		
</div>
</form:form>
<script>
$(document).ready(function(){
    $(".numeric").numeric();
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
$(".datepicker").datepicker({
	dateFormat : "dd/mm/yy",
	changeMonth: true,
	changeYear: true,
	yearRange: "-1:+0",
	defaultDate: new Date()
});

</script>
