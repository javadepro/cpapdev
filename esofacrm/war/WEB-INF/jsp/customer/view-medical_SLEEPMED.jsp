<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


	<h3>Medical History</h3>
	<ul>
		<li><label>High Blood Pressure:</label><span><c:out value="${yesNo[customerMedicalInfo.highBloodPressureFlag]}" /></span></li>
		<li><label>Diabetes:</label><span><c:out value="${yesNo[customerMedicalInfo.diabetesFlag]}" /></span></li>	
		<li><label>Atrial Fibrillation:</label><span><c:out value="${yesNo[customerMedicalInfo.atrialFibrillationFlag]}" /></span></li>
		<li><label>Congested Heart Failure:</label><span><c:out value="${yesNo[customerMedicalInfo.congestedHeartFailureFlag]}" /></span></li>
	</ul>	
	<h3>Sleep History</h3>
	<ul>	
		<li><label>Wake Up Urination Events:</label><span><c:out value="${customerMedicalInfo.wakeUpUrinationEvents}" /></span></li>
		<li><label>Approx Time of Sleep Onset:</label><span><c:out value="${customerMedicalInfo.approxTimeOfSleepOnset}"/>&nbsp;</span></li>
		<li><label>Bed time Hour From/To:</label><span><c:out value="${customerMedicalInfo.bedTimeHour}" />&nbsp;</span></li>
	
		<li><label>Dry Mouth/Sore Throat:</label><span><c:out value="${yesNo[customerMedicalInfo.hasDryMouth]}" /></span></li>
		<li><label>Bruxism (Teeth Grinding):</label><span><c:out value="${yesNo[customerMedicalInfo.bruxismFlag]}" /></span></li>
		<li><label>Halitosis (Bad Breath):</label><span><c:out value="${yesNo[customerMedicalInfo.halitosisFlag]}" /></span></li>
		<li><label>Snores:</label><span><c:out value="${yesNo[customerMedicalInfo.hasSnore]}" /></span></li>
		<li><label>Morning Headaches/Migraines:</label><span> <c:out value="${yesNo[customerMedicalInfo.morningHeadaches]}" /></span></li>
		<li><label>Excessive Day time Sleepiness:</label><span><c:out value="${yesNo[customerMedicalInfo.chronicFaitqueFlag]}" /></span></li>
	</ul>	
