<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page session="true" %>
	<c:if test="${sessionScope.companyMode == 'CPAPDIRECT' }">
		<c:set var="isCpapMode">true</c:set>
	</c:if>

<form>
<c:if test="${customerMedicalInfo!=null}">
<h2>Medical Information</h2>
	<ul>
		<li><label>Family Doctor:</label><c:out value="${familyDoctors[customerMedicalInfo.familyDoctor].name}" />&nbsp;</li>
		<li><label>Sleep Doctor:</label><c:out value="${sleepDoctors[customerMedicalInfo.sleepDoctor].name}" />&nbsp;</li>
		<li><label>Sleep Clinic:</label><c:out value="${clinics[customerMedicalInfo.clinic].name}" />&nbsp;</li>
		
		<c:if test="${isCpapMode != 'true' }">					
		<li><label>Dentist:</label><c:out value="${dentists[customerMedicalInfo.dentist].name}" />&nbsp;</li>
		<li><label>Dental Clinic:</label><c:out value="${dentalClinics[customerMedicalInfo.dentalClinic].name}" />&nbsp;</li>
		</c:if>
		
		<li><label>Referred By:</label><c:out value="${customerMedicalInfo.referredBy}" />&nbsp;</li>
		
	</ul>

	<c:choose>

		<c:when test="${isCpapMode == 'true' }">
			<%@ include file="view-medical_CPAPDIRECT.jsp"%>
		</c:when>
		<c:otherwise>
			<%@ include file="view-medical_SLEEPMED.jsp"%>
		</c:otherwise>
	</c:choose>
	<h3>Personal Information</h3>
	<ul>
		<li><label>Is Smoker:</label><span><c:out value="${yesNo[customerMedicalInfo.isSmoker]}" />&nbsp;</span></li>
		<li><label>Dentures:</label><span><c:out value="${customerMedicalInfo.dentures}" />&nbsp;</span></li>
		<li><label>Sleeping with Dentures:</label><span><c:out value="${yesNo[customerMedicalInfo.isSleepsWithDentures]}" />&nbsp;</span></li>
		<li><label>Has Pet:</label><span><c:out value="${yesNo[customerMedicalInfo.hasPet]}" />&nbsp;</span></li>
		<li><label>Has Bed Partner:</label><span><c:out value="${yesNo[customerMedicalInfo.hasSleepPartner]}" />&nbsp;</span></li>
		<li><label>Travel Frequency(per Month):</label><span><c:out value="${customerMedicalInfo.travelFreq}" />&nbsp;</span></li>		
		<li><label>Has Facial Hair:</label><span><c:out value="${yesNo[customerMedicalInfo.hasFacialHair]}" />&nbsp;</span></li>
		<li><label>Occupation:</label><span><c:out value="${customerMedicalInfo.occupation}" />&nbsp;</span></li>
		<li><label>Sleep position:</label><span><c:out value="${customerMedicalInfo.sleepPosition}" />&nbsp;</span></li>
		<li><label>Medication List:</label><span><c:out value="${customerMedicalInfo.medicationList}" />&nbsp;</span></li>
		
	</ul>	
	<h3>Other</h3>
	<ul>
	<li><label>Special Medical Note:</label><textarea cols="60" rows="2" disabled="disabled" ><c:out value="${customerMedicalInfo.specialMedicalNote}"  /></textarea></li>
	</ul>
<br />
</c:if>
</form>