<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<form:form id="insurance-type4-form" commandName="customerInsuranceInfoDetailsGovernment"
	action="/customer/insurance-type4-form-save" method="POST">
	
	
	<form:hidden path="id" />
	<form:hidden path="customer" />
	<form:hidden path="fundingOption" />
	
	<h2>Insurance Information</h2>
	<form:errors path="*" cssClass="errorblock"></form:errors>
	<c:if test="${message!=null}">
	<div class="messageblock">${message}</div>
	</c:if>
	<ul>
		<li><label>Member Id:<span style="color:red">*</span></label><span><form:input
					path="memberId" maxlength="12"/></span></li>
		<li><label>Case Worker Number:</label><span><form:input
					path="caseWorkerNumber" title="xxx-xxx-xxxx"/>&nbsp;</span></li>
		<li><label>Case Worker Phone:</label><span><form:input
					path="caseWorkerPhone" title="xxx-xxx-xxxx"/>&nbsp;</span></li>
		<li><label>Case Worker Fax:</label><span><form:input
					path="caseWorkerFax" title="xxx-xxx-xxxx"/>&nbsp;</span></li>
		<li><label>Ext:</label><span><form:input
					path="caseWorkerPhoneExt" />&nbsp;</span></li>
	</ul>
	<br />
	<input type="submit" value="Save Insurance Infomation" class="bluebtn" />
	<script>
		setupAjaxForm("insurance-type4-form");
	</script>
</form:form>