<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div id="insurance-wrapper">
	
		<h2>Funding Option</h2>
		<p style="">Insurance detail information depends on the funding
			option. Select the insurance funding option you desire. Then, hit
			save. Once you save, additional details would be available for you to
			fill in regarding that specific funding option.</p>
			<form:form commandName="customerInsuranceInfo"
		id="insurance-option-form"
		action="/customer/insurance-option-form-save">
		<form:hidden path="customer" />
		<form:hidden path="id" />
		<c:if test="${messageInsuranceOption!=null}">
			<div class="messageblock">${messageInsuranceOption}</div>
		</c:if>
		<ul>
			<li><label>Insurance Funding Option:<span
					style="color: red">*</span></label> <form:select
					path="fundingOptionInsurance">
						<form:option value="" label=""/>
						<form:options items="${insuranceFundingOptions}" itemLabel="option" />
				</form:select></li>
		</ul>
		<br/><br/>
		<ul>
			<li><label>Government Funding Option:<span
					style="color: red">*</span></label> <form:select
					path="fundingOptionGovernment">
					<form:options items="${governmentFundingOptions}" itemLabel="option" />
				</form:select></li>
		</ul>
		<br /><br />
		
		<ul>
			<li><label>Insurance Note:</label><form:textarea cols="60" rows="2" size="40" path="insuranceNote"></form:textarea></li>
		</ul>
		
		<br />
		<input type="submit" value="Update" class="bluebtn" />
	</form:form>

	
	<c:choose>
		<c:when
			test="${fundingOptions[customerInsuranceInfo.fundingOptionInsurance].fundingDetailsType==0}">
		</c:when>
		<c:when
			test="${fundingOptions[customerInsuranceInfo.fundingOptionInsurance].fundingDetailsType==1}">
			<h2>Insurance Funding Option</h2>
			<%@include file="form-insurance-type1.jsp"%>
		</c:when>
		<c:when
			test="${fundingOptions[customerInsuranceInfo.fundingOptionInsurance].fundingDetailsType==2}">
			<h2>Insurance Funding Option</h2>
			<%@include file="form-insurance-type2.jsp"%>

		</c:when>
		<c:when
			test="${fundingOptions[customerInsuranceInfo.fundingOptionInsurance].fundingDetailsType==3}">
			<h2>Insurance Funding Option</h2>
			<%@include file="form-insurance-type3.jsp"%>
		</c:when>
	</c:choose>

	<c:choose>
		<c:when
			test="${fundingOptions[customerInsuranceInfo.fundingOptionGovernment].fundingDetailsType==10}">
		</c:when>
		<c:when
			test="${fundingOptions[customerInsuranceInfo.fundingOptionGovernment].fundingDetailsType>10}">
			<h2>Government Funding Option</h2>
			<%@include file="form-insurance-type4.jsp"%>
		</c:when>
	</c:choose>
	<script>
	$('form').bind('submit', function(e) {
	     $(this).find('input:submit').attr('disabled', 'disabled').attr('class','orangebtn').val("Working...");
	});
		//insurance option change save -> reload the whole section
		setupAjaxFormReplace("insurance-option-form", "insurance-wrapper");
	</script>
</div>
