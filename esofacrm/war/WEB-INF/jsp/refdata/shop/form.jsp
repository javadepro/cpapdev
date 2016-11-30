<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="formwrapper">
	<div id="title">
		<h1>
			<div id="icon-tools" class="icon32"></div>
			<span style="float: left; margin-left: 5px; margin-top: 6px;">Clinics</span>
		</h1>
	</div>
	<div style="clear: both" />
	<p>Reference data : Clinic is the entity which inventories,
		clinicians and users reference to. There are two type of shop, 1) Real
		- A real shop; 2) Virtual - A mean to hold inventories</p>

	<form:form id="shop-form" commandName="shop"
		action="/refdata/shop/formsubmit" method="POST">
		<form:hidden path="id" />
		<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>
		<form:errors path="*" cssClass="errorblock" element="div" />

		<h2>Basic Details</h2>
		<ul>
			<li><label>Name:</label> <form:input path="name" size="40"
					cssErrorClass="form-error-field" />
				<div class="form-error-message">
					<form:errors path="name" />
				</div></li>
			<li><label>Short name:</label> <form:input path="shortName"
					size="40" cssErrorClass="form-error-field" /></li>

			<li><label>Phone:</label> <form:input path="phone" size="40"
					cssErrorClass="form-error-field" /></li>
			<li><label>Fax:</label> <form:input path="fax" size="40"
					cssErrorClass="form-error-field" /></li>`
			<li><label>ADP Vendor:</label>
				<form:input path="adpVendorNumber" size="40" cssErrorClass="form-error-field"/>
			</li>
			<li><label>HST Number:</label>
				<form:input path="hstNumber" size="40" cssErrorClass="form-error-field"/>
			</li>
			
		</ul>

		<h2>Address</h2>
		<ul>
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
			<li><label>City:</label> <form:input path="address.city"
					size="40" cssErrorClass="form-error-field" />
				<div class="form-error-message">
					<form:errors path="address.line2" />
				</div></li>
			<li><label>Province/State:</label> <form:select
					path="address.province" cssErrorClass="form-error-field">
					<form:option value="-- Please Select --" />
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
					<form:options items="${countryList}" itemLabel="displayValue" itemValue="displayValue" />
				</form:select>
				<div class="form-error-message">
					<form:errors path="address.province" />
				</div></li>
		</ul>

		<h2>Misc</h2>
		<ul>
			<li><label>Type:</label> <form:select path="shopType"
					cssErrorClass="form-error-field">
					<form:options items="${shopTypeList}" />
				</form:select></li>
			<li><label>Order:</label> <form:input path="order" size="40"
					cssErrorClass="form-error-field" />
				<div class="form-error-message">
					<form:errors path="order" />
				</div></li>
			<li><label>Active:</label>
				 <form:select path="displayDropDown" cssErrorClass="form-error-field" >
					<form:option label="Active" value="true" />
					<form:option label="Inactive" value="false" />				 
				</form:select>
				<div class="form-error-message">
					<form:errors path="order" />
				</div></li>				
			<li><input type="submit" value="Save" class="bluebtn" /></li>
		</ul>
		<script>
			setupAjaxFormReplace("shop-form", "formwrapper");
		</script>
	</form:form>
</div>