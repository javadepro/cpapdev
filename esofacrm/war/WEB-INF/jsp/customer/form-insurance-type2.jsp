<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<form:form id="insurance-type1-form"
	commandName="customerInsuranceInfoDetails"
	action="/customer/insurance-type2-form-save" method="POST">
	<form:hidden path="id" />
	<form:hidden path="customer" />
	<form:hidden path="fundingOption" />
	
	<h2>Spouse Insurance Information</h2>
	<form:errors path="*" cssClass="errorblock"></form:errors>
	<c:if test="${message!=null}">
	<div class="messageblock">${message}</div>
	</c:if>
	<ul>
		<li><label>Insurance Provider:<span style="color:red">*</span></label> <form:select
				path="insuranceProvider">
				<form:options items="${insuranceProviders}" itemLabel="name" />
			</form:select></li>
		<li><label>Policy Number:<span style="color:red">*</span></label><span><form:input
					path="policyNumber" cssErrorClass="" maxlength="15"/></span></li>
		<li><label>Certificate Number:</label><span><form:input
					path="certificateNumber" cssErrorClass="" maxlength="15"/></span></li>
		<li><label>Division Number:</label><span><form:input
					path="divisionNumber" cssErrorClass="" maxlength="15"/></span></li>
	</ul>
	<input type="submit" value="Save Insurance Infomation" class="bluebtn" />
	<script>
		setupAjaxForm("insurance-type1-form");
	</script>
</form:form>