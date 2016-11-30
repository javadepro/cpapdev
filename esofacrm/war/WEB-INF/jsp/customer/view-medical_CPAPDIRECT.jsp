<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
	
	<h3>Therapeutic Information</h3>
	<ul>
		<li><label>Diagnosis:</label><span><c:out value="${cpapDiagnosis[customerMedicalInfo.diagnosis].diagnosis}" />&nbsp;</span></li>
		<li><label>Date of Diagnostic Study:</label><span><c:out value="${customerMedicalInfo.dateOfDiagnosticStudy}" />&nbsp;</span></li>
		<li><label>Date of CPAP Titration:</label><span><c:out value="${customerMedicalInfo.dateOfCpapTitration}" />&nbsp;</span></li>
		<li><label>AHI::</label><span><c:out value="${customerMedicalInfo.ahi}" />&nbsp;</span></li>
		<li><label>O2 Percent:</label><span><c:out value="${customerMedicalInfo.o2Percent}" /></span></li>
		<li><label>Longest Apnea:</label><span><c:out value="${customerMedicalInfo.longestApnea}" /></span></li>
		<li><label>Existing CPAP Patient:</label><span> <c:out value="${yesNo[customerMedicalInfo.existingCpapPatient]}" /></span></li>
		<li><label>Since:</label><span><c:out value="${customerMedicalInfo.since}" />&nbsp;</span></li>
	</ul>
	
	<h3>Medical History</h3>
	<ul>
	<li><label>Allergies:</label>
	<c:choose>
		<c:when test="${customerMedicalInfo.allergiesFlag==true}">
			<textarea cols="60" rows="1" disabled="disabled" ><c:out value="${customerMedicalInfo.allergies}"  /></textarea>
		</c:when><c:otherwise>No</c:otherwise>
	</c:choose>
	</li>
	<li><label>Hypertension:</label>
	<c:choose>
		<c:when test="${customerMedicalInfo.hypertensionFlag==true}">
			<textarea cols="60" rows="1" disabled="disabled" ><c:out value="${customerMedicalInfo.hypertension}"  /></textarea>
		</c:when><c:otherwise>No</c:otherwise>
	</c:choose>
	</li>	
	<li><label>Sinus Problems:</label>
	<c:choose>
		<c:when test="${customerMedicalInfo.sinusProblemsFlag==true}">
			<textarea cols="60" rows="1" disabled="disabled" ><c:out value="${customerMedicalInfo.sinusProblems}"  /></textarea>
		</c:when><c:otherwise>No</c:otherwise>
	</c:choose>
	</li>	
	<li><label>Cardiac History:</label>
	<c:choose>
		<c:when test="${customerMedicalInfo.cardiacHxFlag==true}">
			<textarea cols="60" rows="1" disabled="disabled" ><c:out value="${customerMedicalInfo.cardiacHx}"  /></textarea>
		</c:when><c:otherwise>No</c:otherwise>
	</c:choose>
	</li>	
	<li><label>Diabetes:</label>
	<c:choose>
		<c:when test="${customerMedicalInfo.diabetesFlag==true}">
			<textarea cols="60" rows="1" disabled="disabled" ><c:out value="${customerMedicalInfo.diabetes}"  /></textarea>
		</c:when><c:otherwise>No</c:otherwise>
	</c:choose>
	</li>
	<li><label>Chronic Fatigue:</label>
	<c:choose>
		<c:when test="${customerMedicalInfo.chronicFaitqueFlag==true}">
			<textarea cols="60" rows="1" disabled="disabled" ><c:out value="${customerMedicalInfo.chronicFaitque}"  /></textarea>
		</c:when><c:otherwise>No</c:otherwise>
	</c:choose>
	</li>
	<li><label>Asthma:</label>
	<c:choose>
		<c:when test="${customerMedicalInfo.asthmaFlag==true}">
			<textarea cols="60" rows="1" disabled="disabled" ><c:out value="${customerMedicalInfo.asthma}"  /></textarea>
		</c:when><c:otherwise>No</c:otherwise>
	</c:choose>
	</li>				
	<li><label>Thyroid History:</label>
	<c:choose>
		<c:when test="${customerMedicalInfo.thyroidHxFlag==true}">
			<textarea cols="60" rows="1" disabled="disabled" ><c:out value="${customerMedicalInfo.thyroidHx}"  /></textarea>
		</c:when><c:otherwise>No</c:otherwise>
	</c:choose>
	</li>	
	<li><label>Epistaxis:</label>
	<c:choose>
		<c:when test="${customerMedicalInfo.epitaxisFlag==true}">
			<textarea cols="60" rows="1" disabled="disabled" ><c:out value="${customerMedicalInfo.epitaxis}"  /></textarea>
		</c:when><c:otherwise>No</c:otherwise>
	</c:choose>
	</li>	
	<li><label>GERD:</label>
	<c:choose>
		<c:when test="${customerMedicalInfo.gerdFlag==true}">
			<textarea cols="60" rows="1" disabled="disabled" ><c:out value="${customerMedicalInfo.gerd}"  /></textarea>
		</c:when><c:otherwise>No</c:otherwise>
	</c:choose>
	</li>	
	<li><label>Bruxism:</label>
	<c:choose>
		<c:when test="${customerMedicalInfo.bruxismFlag==true}">
			<textarea cols="60" rows="1" disabled="disabled" ><c:out value="${customerMedicalInfo.bruxism}"  /></textarea>
		</c:when><c:otherwise>No</c:otherwise>
	</c:choose>
	</li>	
	<li><label>Claustrophobia:</label>
	<c:choose>
		<c:when test="${customerMedicalInfo.claustrophobiaFlag==true}">
			<textarea cols="60" rows="1" disabled="disabled" ><c:out value="${customerMedicalInfo.claustrophobia}"  /></textarea>
		</c:when><c:otherwise>No</c:otherwise>
	</c:choose>
	</li>	
	<li><label>Acid Reflux Syndrome:</label>
	<c:choose>
		<c:when test="${customerMedicalInfo.acidRefluxSyndromeFlag==true}">
			<textarea cols="60" rows="1" disabled="disabled" ><c:out value="${customerMedicalInfo.acidRefluxSyndrome}"  /></textarea>
		</c:when><c:otherwise>No</c:otherwise>
	</c:choose>
	</li>	
	<li><label>Copd:</label>
		<c:choose>
			<c:when test="${customerMedicalInfo.copdFlag==true}">
				<textarea cols="60" rows="1" disabled="disabled" ><c:out value="${customerMedicalInfo.copd}"  /></textarea>
			</c:when>
			<c:otherwise>No	</c:otherwise>
		</c:choose>
	</li>	
	</ul>	
	<h3>Sleep History</h3>
	<ul>
		<li><label>Wake Up Urination Events:</label><span><c:out value="${customerMedicalInfo.wakeUpUrinationEvents}" /></span></li>
		<li><label>Approx Time of Sleep Onset:</label><span><c:out value="${customerMedicalInfo.approxTimeOfSleepOnset}"/>&nbsp;</span></li>
		<li><label>Bed time Hour From/To:</label><span><c:out value="${customerMedicalInfo.bedTimeHour}" />&nbsp;</span></li>
		<li><label>Snores:</label><span><c:out value="${yesNo[customerMedicalInfo.hasSnore]}" /></span></li>			
		<li><label>Toss And Turns:</label><span> <c:out value="${yesNo[customerMedicalInfo.hasTossAndTurn]}"/></span></li>
		<li><label>Has Dry Mouth:</label><span><c:out value="${yesNo[customerMedicalInfo.hasDryMouth]}" /></span></li>
		<li><label>Is Shift Worker:</label><span> <c:out value="${yesNo[customerMedicalInfo.isShiftWorker]}" /></span></li>
		<li><label>Drooling:</label><span> <c:out value="${yesNo[customerMedicalInfo.droolingFlag]}" /></span></li>	
		<li><label>Nightmares:</label><span> <c:out value="${yesNo[customerMedicalInfo.nightmaresFlag]}" /></span></li>
		<li><label>Morning Headaches/Migraines:</label><span> <c:out value="${yesNo[customerMedicalInfo.morningHeadaches]}" /></span></li>
		<li><label>Sleeping Pills:</label><span> <c:out value="${yesNo[customerMedicalInfo.sleepingPills]}" /></span></li>
	</ul>	