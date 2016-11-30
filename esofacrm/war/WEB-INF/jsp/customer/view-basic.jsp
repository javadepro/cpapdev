<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
<form>
<br />
<h2>Basic Details</h2>
<ul>
	<li><label>First Name:</label><span><c:out
				value="${customerWrapper.customer.firstname}" /></span></li>
	<li><label>Last Name:</label><span><c:out
				value="${customerWrapper.customer.lastname}" /></span></li>
	<li><label>Gender:</label><span><c:out
				value="${customerWrapper.customerExtendedInfo.gender}" /></span></li>
	<li><label>DOB:</label><span><fmt:formatDate
				value="${customerWrapper.customerExtendedInfo.dateOfBirth}" type="DATE" pattern="dd/MM/yyyy" />&nbsp;</span></li>
	<li><label>Health Card (Number - Version):</label><span><c:out 
				value="${customerWrapper.customer.healthCardNumber}" />&nbsp;-&nbsp;<c:out 
				value="${customerWrapper.customer.healthCardVersion}" /></span></li>
	<li><label>Clinician:</label><span><c:out
				value="${clinicians[customerWrapper.customer.clinician].name}" />&nbsp;</span></li>
	<li><label>Preferred Location:</label><span><c:out
				value="${shops[customerWrapper.customer.preferredLocation].name}" />&nbsp;</span></li>
	<li><label>Appointment Preference:</label><span><c:out
				value="${appointmentPreferences[customerWrapper.customerExtendedInfo.appointmentPreference].preference}" />&nbsp;</span></li>
	<li><label>Appointment Note:</label><span><c:out
				value="${customerWrapper.customerExtendedInfo.appointmentPreferenceNote}" />&nbsp;</span></li>
	<li><label>Language Preference:</label><span><c:out
				value="${customerWrapper.customerExtendedInfo.language}" />&nbsp;</span></li>				
	<li><label>Last Updated:</label><span><c:out
				value="${customerWrapper.customer.formattedLastUpdated}" />&nbsp;</span></li>				
</ul>

<br />

<h2>Contact Information</h2>
<ul>
	<li><label>Email:</label><span><c:out
				value="${customerWrapper.customerExtendedInfo.email}" />&nbsp;</span></li>
	<li><label>Phone (Home):</label><span><c:out
				value="${customerWrapper.customer.phoneHome}" />&nbsp;</span></li>
	<li><label>Phone (Work):</label><span><c:out
				value="${customerWrapper.customer.phoneOffice}" />&nbsp;Ext:<c:out
				value="${customerWrapper.customer.phoneOfficeExt}" /></span></li>
	<li><label>Phone (Mobile):</label><span><c:out
				value="${customerWrapper.customer.phoneMobile}" />&nbsp;</span></li>
	<li><label>Contact Preference:</label><span><c:out
				value="${contactPreferences[customerWrapper.customerExtendedInfo.contactPreference].preference}" />&nbsp;</span></li>			
	<li><label>Address Line1:</label><span><c:out
				value="${customerWrapper.customerExtendedInfo.address.line1}" />&nbsp;</span></li>
	<li><label>Address Line2:</label><span><c:out
				value="${customerWrapper.customerExtendedInfo.address.line2}" />&nbsp;</span></li>
	<li><label>City:</label><span><c:out
				value="${customerWrapper.customerExtendedInfo.address.city}" />&nbsp;</span></li>
	<li><label>Province/State:</label><span><c:out
				value="${customerWrapper.customerExtendedInfo.address.province}" />&nbsp;</span></li>
	<li><label>Postal/Zip Code:</label><span><c:out
				value="${customerWrapper.customerExtendedInfo.address.postalCode}" />&nbsp;</span></li>
	<li><label>Country:</label><span><c:out
				value="${customerWrapper.customerExtendedInfo.address.country}" />&nbsp;</span></li>
	<li><label>Contact Notes:</label> <textarea cols="60" rows="5" disabled="disabled" ><c:out value="${customerWrapper.customerExtendedInfo.contactNotes}"  /></textarea></li>		
	
	
	<h2>Consent Information</h2><br/>
	<ul>
		<li>
		<input type="checkbox" <c:if test="${customerWrapper.customerExtendedInfo.consentStorage}">checked="checked"</c:if> contenteditable="false" disabled="disabled">
		 I hereby give consent for <c:out value="${companyName}" />. to collect and use the information above to contact me at any of the phone numbers, e-mail addresses and/or mailing addresses provided. I agree to have my personal health information stored in <c:out value="${companyName}" />'s database and understand that this information will not be shared with anyone without my consent or as required by law.
		</li><br/><br/>
		<c:if test="${isCpapMode == 'true' }">							
		<li>I hereby give consent for CPAP Direct Ltd. to provide me with news and promotions via:
			<ul>
				<li><input type="checkbox" <c:if test="${customerWrapper.customerExtendedInfo.consentPhone}">checked="checked"</c:if> contenteditable="false" disabled="disabled"> &nbsp;Phone</li>
				<li><input type="checkbox" <c:if test="${customerWrapper.customerExtendedInfo.consentEmail}">checked="checked"</c:if> contenteditable="false" disabled="disabled"> &nbsp;Email</li>
				<li><input type="checkbox" <c:if test="${customerWrapper.customerExtendedInfo.consentMail}">checked="checked"</c:if> contenteditable="false" disabled="disabled"> &nbsp;Regular Mail</li>
			</ul>
		</li>		
		</c:if>
	</ul>	
</ul>
<br />
</form>
