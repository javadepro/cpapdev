<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="formwrapper">
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Dentist</span></h1>
</div>
<div style="clear:both" />
<p>Reference data : Dentist</p>

<form:form id="dentist-form" commandName="dentist"
	action="/refdata/dentist/formsubmit" method="POST">
	<form:hidden path="id" />
	<form:errors path="*" cssClass="errorblock" element="div" />
	<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>
		<h2>Basic Details</h2>
		<ul>
		<li><label>First Name:</label> <form:input path="firstName"
				size="40" cssErrorClass="form-error-field" />
			</li>
		<li><label>Last Name:</label> <form:input path="lastName"
				size="40" cssErrorClass="form-error-field" />
			</li>
		<li><label>Email:</label> <form:input path="email" size="40"
				cssErrorClass="form-error-field" />
			</li>
		<li><label>Phone:</label> <form:input path="phone" size="40"
				cssErrorClass="form-error-field" />
			</li>
		<li><label>Fax:</label> <form:input path="fax" size="40"
				cssErrorClass="form-error-field" />
			</li>
		</ul>
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
		<li><label>City:</label> <form:input
				path="address.city" size="40" cssErrorClass="form-error-field" />
			<div class="form-error-message">
				<form:errors path="address.city" />
			</div></li>
		<li><label>Province/State:</label> <form:select path="address.province"
				cssErrorClass="form-error-field">
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
				<form:options items="${countryList}" itemLabel="displayValue" itemValue="displayValue"/>
			</form:select>
			<div class="form-error-message">
				<form:errors path="address.province" />
			</div></li>
		</ul>
		<h2>Note</h2>
		<ul>
		<li><label>Comment:</label> <form:textarea cols="60" rows="5" path="comment"
				size="40" cssErrorClass="form-error-field" />
			<div class="form-error-message">
				<form:errors path="comment" />
			</div></li>
		<li><input type="submit" value="Save" class="bluebtn"/></li>
	</ul>
	<script>
	$('form').bind('submit', function(e) {
	     $(this).find('input:submit').attr('disabled', 'disabled').attr('class','orangebtn').val("Working...");
	});
setupAjaxFormReplace("dentist-form","formwrapper");
</script>
</form:form>
</div>
