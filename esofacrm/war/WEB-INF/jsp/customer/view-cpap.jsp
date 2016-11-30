<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<form>

<h2>Cpap Information</h2>

<c:if test="${customerMedicalInfoWrapper!=null}">
	<ul>
	<li><label>CPAP Pressure:</label><span><c:out value="${customerMedicalInfoWrapper.customerMedicalInfo.cpapPressure}" /></span></li>
	<li><label>Ramp:</label><span> <c:out value="${yesNo[customerMedicalInfoWrapper.customerMedicalInfo.hasRamp]}" /></span></li>		
	<li><label>Ramp Mins:</label><span><c:out value="${customerMedicalInfoWrapper.customerMedicalInfo.rampMins}" /></span></li>
	<li><label>Ramp From/To:</label><span><c:out value="${customerMedicalInfoWrapper.customerMedicalInfo.ramp}" /></span></li>
	<li><label>EPR or C-Flex:</label><span> <c:out value="${yesNo[customerMedicalInfoWrapper.customerMedicalInfo.hasEprCFlex]}" /> <c:out value="${customerMedicalInfoWrapper.customerMedicalInfo.eprCFlexDesc}" />
		</span></li>	
	</ul>
 	<h3>Current CPAP Machine</h3>
 	<ul>
		<li><label>Model:</label><span>
			<c:out value="${products[customerMedicalInfoWrapper.customerMedicalInfo.currentCpapMachine].name}" /></li> 
		<li><label>Serial Number:</label><span><c:out value="${customerMedicalInfoWrapper.customerMedicalInfo.currentCpapMachineSerial}" />
			</span></li>		
		<li><label>Humidifier Serial Number:</label><span><c:out value="${customerMedicalInfoWrapper.customerMedicalInfo.currentCpapMachineHumidifierSerial}" />
			</span></li>			
	<li><label>CPAP Purchase Date:</label><span> 
	<c:out value="${customerMedicalInfoWrapper.customerMedicalInfo.cpapPurchaseDate}"/>&nbsp;
		</span></li>			
	</ul>
	<h3>Greater than 5 Year Replacement CPAP Machine</h3>
	<ul>		 
		<li><label>Model:</label><span>
			<c:out value="${products[customerMedicalInfoWrapper.customerMedicalInfo.replacementCpapMachine].name}" /></li> 
		<li><label>Serial NUmber:</label><span><c:out value="${customerMedicalInfoWrapper.customerMedicalInfo.replacementCpapMachineSerial}" />
			</span></li>		
		<li><label>Humidifier Serial Number:</label><span><c:out value="${customerMedicalInfoWrapper.customerMedicalInfo.replacementCpapMachineHumidifierSerial}" />
			</span></li>	 
	</ul>
	<br/><br/>&nbsp;
	<ul>
	<li><label>Current Mask:</label><span>
		 <c:out value="${products[customerMedicalInfoWrapper.customerMedicalInfo.currentMask].name}" />
				</li> 
	<li><label>Mask Purchase Date:</label><span> 
	<c:out value="${customerMedicalInfoWrapper.customerMedicalInfo.maskPurchaseDate}" /> &nbsp;
		</span></li> 
	<li><label>CPAP Purchase Notes:</label><span> 
	<textarea cols="60" rows="2" disabled="disabled" ><c:out value="${customerMedicalInfoWrapper.customerMedicalInfo.cpapPurchaseNotes}"  /></textarea></span></li> 
	</ul>				
	<h3>CPAP Trial Information</h3>
	<ul>
		<li><label>CPAP Deposit Amount:</label><span><c:out value="${customerMedicalInfoWrapper.customerCpapTrialInfo.cpapDepositAmt}"/></span></li>
		<li><label>CPAP Received By:</label><span><c:out  value="${customerMedicalInfoWrapper.customerCpapTrialInfo.cpapReceivedBy}"/></span></li>
		<li><label>CPAP Rental Start Date:</label><span><c:out value="${customerMedicalInfoWrapper.customerCpapTrialInfo.cpapRentalStart}"  /> </span></li>
		<li><label>CPAP Rental End Date:</label><span><c:out value="${customerMedicalInfoWrapper.customerCpapTrialInfo.cpapRentalEnd}"  /></span></li>
	</ul>
	<br/><br/>&nbsp;
	<ul>	
		<li><label>APAP Deposit Amount:</label><span><c:out  value="${customerMedicalInfoWrapper.customerCpapTrialInfo.apapDepositAmt}"/></span></li>
		<li><label>APAP Received By:</label><span><c:out  value="${customerMedicalInfoWrapper.customerCpapTrialInfo.appaReceivedBy}"/></span></li>
		<li><label>APAP Rental Start Date:</label><span><c:out value="${customerMedicalInfoWrapper.customerCpapTrialInfo.apapRentalStart}" /></span></li>		
		<li><label>APAP Rental End Date:</label><span><c:out value="${customerMedicalInfoWrapper.customerCpapTrialInfo.apapRentalEnd}" /></span></li>			
	</ul>
<br />
</c:if>
</form>