<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<form:form id="insurance-type1-form"
	commandName="wrapper"
	action="/customer/insurance-type3-form-save" method="POST">
	<form:hidden path="self.id" />
	<form:hidden path="self.customer" />
	<form:hidden path="spouse.id" />
	<form:hidden path="spouse.customer" />
	<form:hidden path="self.fundingOption" />
	<form:hidden path="spouse.fundingOption" />

	<h2>Self+Spouse Insurance Information</h2>
	<form:errors path="*" cssClass="errorblock"></form:errors>
	<c:if test="${message!=null}">
	<div class="messageblock">${message}</div>
	</c:if>
	<table class="listing">
	<tr>
	<th></th><th>Self</th><th>Spouse</th>
	</tr>
	<tr>
		<td>Insurance Provider<span style="color:red">*</span></td>
		<td><form:select
				path="self.insuranceProvider">
				<form:options items="${insuranceProviders}" itemLabel="name" />
				</form:select></td>
		<td>
		<form:select
				path="spouse.insuranceProvider">
				<form:options items="${insuranceProviders}" itemLabel="name" />
				</form:select></td>
	</tr>
		<tr><td>Policy Number<span style="color:red">*</span></td><td><form:input
					path="self.policyNumber" cssErrorClass="" maxlength="15"/></td><td><form:input
					path="spouse.policyNumber" cssErrorClass="" maxlength="15"/></td></tr>
		<tr><td>Certificate Number</td><td><form:input
					path="self.certificateNumber" cssErrorClass="" maxlength="15"/></td><td><form:input
					path="spouse.certificateNumber" cssErrorClass="" maxlength="15"/></td></tr>
		<tr><td>Division Number:</td><td><form:input
					path="self.divisionNumber" cssErrorClass="" maxlength="15"/></td><td><form:input
					path="spouse.divisionNumber" cssErrorClass="" maxlength="15"/></td></tr>	
	</table>
	<br />
	<input type="submit" value="Save Insurance Infomation" class="bluebtn" />
	<script>
		setupAjaxForm("insurance-type1-form");
	</script>
</form:form>