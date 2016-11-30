<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page session="true" %>
	<c:choose>
	<c:when test="${sessionScope.companyMode == 'CPAPDIRECT' }">
		<c:set var="isCpapMode">true</c:set>
		<c:set var="companyName">Cpap Direct Ltd</c:set>
	</c:when>
	<c:otherwise>
		<c:set var="companyName">SleepMed Corp</c:set>
	</c:otherwise>
	</c:choose>

<form:form id="basic-form" commandName="customerWrapper"
	action="/customer/basic-form-save" method="POST">
	
	<form:hidden path="customer.id" />
	<form:hidden path="customerExtendedInfo.id" />
	<form:hidden path="customerExtendedInfo.customer" />
	<h2>Basic Details</h2>
	
	<p  class="pageDesc">Please try to obtain as much information about the customer as possible.  Any additional contact information can go into the Contact Notes.</p>
	<form:errors path="*" cssClass="errorblock"></form:errors>
	<c:if test="${message!=null}">
	<div class="messageblock">${message}</div>
	</c:if>
	<ul>
		<li><label>Status:</label><span> <form:select path="customer.active">
					<form:option value="true">Active</form:option>
					<form:option value="false">Inactive</form:option>
				</form:select>
		</span></li>

		<li><label>First Name:<span style="color:red">*</span></label><span><form:input
					path="customer.firstname" cssErrorClass="form-error-field"/></span></li>
		<li><label>Last Name:<span style="color:red">*</span></label><span><form:input
					path="customer.lastname" cssErrorClass="form-error-field" /></span></li>
		<li><label>Gender:</label><span><form:select path="customerExtendedInfo.gender">
		<form:options items="${genders}" /> 
		</form:select>
		</span></li>
		<li><label>DOB:</label><span><form:input
					path="customerExtendedInfo.dateOfBirth" class="datepicker" placeholer="dd/MM/YYYY" /></span></li>
		<li><label>Health Card (Number - Version):</label><span><form:input
				path="customer.healthCardNumber" maxlength="12" cssErrorClass="form-error-field" placeholder="xxxx xxx xxx" />&nbsp;-&nbsp;<form:input 
				path="customer.healthCardVersion"  maxlength="2" title="up to 2 characters"/>
		</span></li>
		<li><label>Clinician:</label><span><form:select
					path="customer.clinician">
					<form:option value="" label=""/>
					<form:options items="${clinicians}" itemLabel="name" />
				</form:select> </span></li>
		<li><label>Preferred Location:</label> <form:select path="customer.preferredLocation" ><form:options items="${shops}" itemLabel="name" /></form:select></li>
		<li><label>Appointment Preference:</label><span><form:select
						path="customerExtendedInfo.appointmentPreference">
						<form:options items="${appointmentPreferences}"
							itemLabel="preference" />
					</form:select></span></li>
		<li><label>Appointment Preference Note:</label><span><form:input
					path="customerExtendedInfo.appointmentPreferenceNote"  placeholder="extra note for appointment preference"/></span></li>
		<li><label>Language Preference:</label><span><form:select
						path="customerExtendedInfo.language">
						<form:options items="${languagePreferences}"
							itemLabel="displayValue" />
					</form:select></span></li>
					
	</ul>
	<br />
	<h2>Contact Information</h2>
	<ul>
		<li><label>Email:</label><span><form:input path="customerExtendedInfo.email" />&nbsp;</span></li>
		<li><label>Phone (Home):<span style="color:red">*</span></label><span><form:input
					path="customer.phoneHome" placeholder="xxx-xxx-xxxx"/></span></li>
		<li><label>Phone (Work):</label><span><form:input
					path="customer.phoneOffice" placeholder="xxx-xxx-xxxx" />&nbsp;Ext:<form:input path="customer.phoneOfficeExt" /></span></li>
		<li><label>Phone (Mobile):</label><span><form:input
					path="customer.phoneMobile" placeholder="xxx-xxx-xxxx"/>&nbsp;</span></li>
		<li><label>Contact Preference:</label><span><form:select
						path="customerExtendedInfo.contactPreference">
						<form:options items="${contactPreferences}"
							itemLabel="preference" />
					</form:select></span></li>
		<li><label>Address Line1:<span style="color:red">*</span></label> <form:input
				path="customerExtendedInfo.address.line1"  cssErrorClass="form-error-field"/></li>
		<li><label>Address Line2:</label>
		<form:input path="customerExtendedInfo.address.line2" />&nbsp;</li>
		<li><label>City:</label> <form:input path="customerExtendedInfo.address.city" /></li>
		<%--<li><label>Province/State:<span style="color:red">*</span></label> <form:select path="customerExtendedInfo.address.province" ><form:options items="${provinces}" /></form:select></li>
		 --%>
		 <li><label>Province/State:<span style="color: red">*</span></label> 
			<form:input path="customerExtendedInfo.address.province" id="province" /></li>
		<li><label>Postal/Zip Code:</label> <form:input path="customerExtendedInfo.address.postalCode" maxlength="6" /></li>
		<%--
		<li><label>Country:<span style="color:red">*</span></label> <form:select path="customerExtendedInfo.address.country" ><form:options items="${countries}" itemValue="displayValue" itemLabel="displayValue" /></form:select></li>
		 --%>
		<li><label>Country:<span style="color: red">*</span></label> <form:input
					path="customerExtendedInfo.address.country" id="country"/>
					</li> 
		
		<li><label>Contact Notes:</label> <form:textarea cols="60" rows="5"
				path="customerExtendedInfo.contactNotes" cssErrorClass="form-error-field" title="any special notes regarding contacting the customer should go here. ie: notes to indicating the primary contact is a spouse, etc"/></li>
	</ul>
	
	<h2>Consent Information</h2><br/>
	<form:hidden path="customerExtendedInfo.consentContact"/>
	<ul>
		<li><form:checkbox path="customerExtendedInfo.consentStorage" /><span style="color: red">*</span>&nbsp;
		I hereby give consent for <c:out value="${companyName}" />. to collect and use the information above to contact me at any of the phone numbers, e-mail addresses and/or mailing addresses provided. I agree to have my personal health information stored in <c:out value="${companyName}" />'s database and understand that this information will not be shared with anyone without my consent or as required by law.
		</li><br/><br/>
		
		<c:if test="${isCpapMode == 'true' }">			
		
		<li>I hereby give consent for CPAP Direct Ltd. to provide me with news and promotions via:
			<ul>
				<li><form:checkbox path="customerExtendedInfo.consentPhone" />: &nbsp;Phone</li>
				<li><form:checkbox path="customerExtendedInfo.consentEmail" />: &nbsp;Email</li>
				<li><form:checkbox path="customerExtendedInfo.consentMail" />: &nbsp;Regular Mail</li>
			</ul>
		</li>
		</c:if>
	</ul>
	<br/><br/>
	
	<input type="submit" value="Save Basic Info" class="bluebtn" />
<script>
$('form').bind('submit', function(e) {
    $(this).find('input:submit').attr('disabled', 'disabled').attr('class','orangebtn').val("Working...");
});
var countries = [<c:forEach items="${countries}" var="country">"${country.displayValue}",</c:forEach>];
var provinces = [<c:forEach items="${provinces}" var="province">"${province}",</c:forEach>];

$( "#country" ).autocomplete({
	source: countries
});
$( "#province" ).autocomplete({
	source: provinces
});
$(".datepicker").datepicker({
		dateFormat : "dd/mm/yy",
		changeMonth: true,
		changeYear: true,
		yearRange: "-110:+0",
		defaultDate: new Date(1975, 00, 01)
});
$(".autobox").combobox();
$('.timepicker').timepicker();
setupAjaxForm("basic-form");
</script>
</form:form>