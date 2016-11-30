<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="formwrapper">
	<div id="title">
		<h1>
			<div id="icon-tools" class="icon32"></div>
			<span style="float: left; margin-left: 5px; margin-top: 6px;">Insurance
				Provider</span>
		</h1>
	</div>
	<div style="clear: both" />
	<p>Reference data : Insurance Provider</p>


	<form:form id="insurance-provider-form" commandName="insuranceProvider"
		action="/refdata/insurance-provider/formsubmit" method="POST">
		<form:hidden path="id" />

		<form:errors path="*" cssClass="errorblock" element="div" />
		<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>
		<ul>
			<li><label>Name:</label> <form:input path="name" size="40"
					cssErrorClass="form-error-field" /> <form:errors path="name" /></li>
			<li><label>Phone:</label> <form:input path="phone" size="40"
					cssErrorClass="form-error-field" /> <form:errors path="phone" /></li>
			<li><label>Phone Ext:</label> <form:input path="phoneExt" size="40"
					cssErrorClass="form-error-field" /> <form:errors path="phoneExt" /></li>
			<li><label>Email:</label> <form:input path="email" size="40"
					cssErrorClass="form-error-field" /> <form:errors path="email" /></li>
			<li><label>Address Line1:</label> <form:input
					path="address.line1" size="40" cssErrorClass="form-error-field" />
				<div class="form-error-message">
					<form:errors path="address.line1" />
				</div></li>
			<li><label>Address Line2:</label> <form:input
					path="address.line2" size="40" cssErrorClass="form-error-field" />
				<div class="form-error-message">
					<form:errors path="address.line2" />
				</div></li>
			<li><label>Province/State:</label> <form:select
					path="address.province" cssErrorClass="form-error-field">
					<form:options items="${provinceList}" />
				</form:select>
				<div class="form-error-message">
					<form:errors path="address.province" />
				</div></li>
			<li><label>Postal/Zip Code:</label> <form:input path="address.postalCode"
					size="40" cssErrorClass="form-error-field" />
				<div class="form-error-message">
					<form:errors path="address.postalCode" />
				</div></li>
			<li><label>Country:</label> <form:select path="address.country"
					cssErrorClass="form-error-field">
					<form:option value="-- Please Select --" />
					<form:options items="${countryList}" itemLabel="displayValue" itemValue="displayValue"/>
				</form:select>
				<div class="form-error-message">
					<form:errors path="address.province" />
				</div></li>
			<li></li>
		</ul>
		<input type="submit" value="Save" class="bluebtn" />

	</form:form>
	<script>
		setupAjaxFormReplace("insurance-provider-form","formwrapper");
	</script>
</div>