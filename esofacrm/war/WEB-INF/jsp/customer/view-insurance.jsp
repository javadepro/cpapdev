<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<form>
<h2>Insurance Funding Option</h2>
	
<c:if
	test="${fundingOptions[customerInsuranceInfo.fundingOptionInsurance].fundingDetailsType==0}">
	<ul>
	<li><label>Insurance Funding Option:</label> <c:out value="${fundingOptions[customerInsuranceInfo.fundingOptionInsurance].option}" /></li>
	</ul>
</c:if>
<c:if
	test="${fundingOptions[customerInsuranceInfo.fundingOptionInsurance].fundingDetailsType==1}">
	<ul>
		<li><label>Insurance Funding Option:</label> <c:out value="${fundingOptions[customerInsuranceInfo.fundingOptionInsurance].option}" /></li>
	</ul>
	</br />
		<h2>Insurance Information</h2>
		<ul>
			<li><label>Insurance Provider:</label> <c:out value="${insuranceProviders[customerInsuranceInfoDetails.insuranceProvider].name}"  /></li>
			<li><label>Policy Number:</label><span><c:out value="${customerInsuranceInfoDetails.policyNumber}" />&nbsp;</span></li>
			<li><label>Certificate Number:</label><span><c:out value="${customerInsuranceInfoDetails.certificateNumber}" />&nbsp;</span></li>
			<li><label>Division Number:</label><span><c:out value="${customerInsuranceInfoDetails.divisionNumber}" />&nbsp;</span></li>
		</ul>
</c:if>
<c:if
	test="${fundingOptions[customerInsuranceInfo.fundingOptionInsurance].fundingDetailsType==2}">
	<ul>
		<li><label>Insurance Funding Option:</label> <c:out value="${fundingOptions[customerInsuranceInfo.fundingOptionInsurance].option}" /></li>
	</ul>
	</br />
	<h2>Spouse Insurance Information</h2>
	<ul>
			<li><label>Insurance Provider:</label> <c:out value="${insuranceProviders[customerInsuranceInfoDetails.insuranceProvider].name}"  /></li>
			<li><label>Policy Number:</label><span><c:out value="${customerInsuranceInfoDetails.policyNumber}" />&nbsp;</span></li>
			<li><label>Certificate Number:</label><span><c:out value="${customerInsuranceInfoDetails.certificateNumber}" />&nbsp;</span></li>
			<li><label>Division Number:</label><span><c:out value="${customerInsuranceInfoDetails.divisionNumber}" />&nbsp;</span></li>
		</ul>
</c:if>
<c:if
	test="${fundingOptions[customerInsuranceInfo.fundingOptionInsurance].fundingDetailsType==3}">
	<ul>
		<li><label>Insurance Funding Option:</label> <c:out value="${fundingOptions[customerInsuranceInfo.fundingOptionInsurance].option}" /></li>
	</ul>
	</br />
	<h2>Self+Spouse Insurance Information</h2>
	<table class="listing">
			<tr><th></th><th>Self</th><th>Spouse</th></tr>
			<tr><td>Insurance Provider:</td><td> <c:out value="${insuranceProviders[wrapper.self.insuranceProvider].name}"  /></td><td><c:out value="${insuranceProviders[wrapper.spouse.insuranceProvider].name}"  /></td></tr>
			<tr><td>Policy Number:</td><td><c:out value="${wrapper.self.policyNumber}" /></td><td><c:out value="${wrapper.spouse.policyNumber}" /></td></tr>
			<tr><td>Certificate Number:</td><td><c:out value="${wrapper.self.certificateNumber}" /></td><td><c:out value="${wrapper.spouse.certificateNumber}" /></td></tr>
			<tr><td>Division Number:</td><td><c:out value="${wrapper.self.divisionNumber}" /></td><td><c:out value="${wrapper.spouse.divisionNumber}" /></td></tr>
		</table>
</c:if>
<br/>
<c:if
	test="${fundingOptions[customerInsuranceInfo.fundingOptionGovernment].fundingDetailsType == 9
	|| fundingOptions[customerInsuranceInfo.fundingOptionGovernment].fundingDetailsType == 10}">
	<ul>
		<li><label>Government Funding Option:</label> <c:out value="${fundingOptions[customerInsuranceInfo.fundingOptionGovernment].option}" /></li>
	</ul>
	</c:if>
<c:if
	test="${fundingOptions[customerInsuranceInfo.fundingOptionGovernment].fundingDetailsType > 10}">
	<ul>
		<li><label>Government Funding Option:</label> <c:out value="${fundingOptions[customerInsuranceInfo.fundingOptionGovernment].option}" /></li>
	</ul>
	<br/><br/>
	<h2>Government Funding Information</h2>
	<ul>
		<li><label>Member Id:</label><span><c:out value="${customerInsuranceInfoDetailsGovernment.memberId}" /></span></li>
			<li><label>Case Worker Number:</label><span><c:out value="${customerInsuranceInfoDetailsGovernment.caseWorkerNumber}" />&nbsp;</span></li>
			<li><label>Case Worker Phone:</label><span><c:out value="${customerInsuranceInfoDetailsGovernment.caseWorkerPhone}" />&nbsp;</span></li>
			<li><label>Ext:</label><span><c:out value="${customerInsuranceInfoDetailsGovernment.caseWorkerPhoneExt}" />&nbsp;</span></li>
	</ul>
</c:if>
		<ul>
			<li><label>Insurance Note:</label><textarea cols="60" rows="2" disabled="disabled" ><c:out value="${customerInsuranceInfo.insuranceNote}"  /></textarea></li>
		</ul>
		
</form>