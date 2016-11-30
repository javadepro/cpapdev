<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page session="true" %>
	<c:if test="${sessionScope.companyMode == 'CPAPDIRECT' }">
		<c:set var="isCpapMode">true</c:set>
	</c:if>
					
	<h3>Therapeutic Information</h3>
	<ul>
		<li><label>Diagnosis:</label><span><form:select
					path="diagnosis">
					<form:options items="${cpapDiagnosis}" itemLabel="diagnosis" />
				</form:select></span></li>
		<li><label>Date of Diagnostic Study:</label><span><form:input
					path="dateOfDiagnosticStudy" placeholder="MM/YYYY"/>&nbsp;</span></li>
		<li><label>Date of CPAP Titration:</label><span><form:input
					path="dateOfCpapTitration" cssClass="datepicker" />&nbsp;</span></li>
		<li><label>AHI:</label><span><form:input path="ahi" />&nbsp;</span></li>
		<li><label>O2 Percent:</label><span><form:input path="o2Percent" /> &nbsp;</span></li>
		<li><label>Longest Apnea:</label><span><form:textarea 
					path="longestApnea"  col="40" row="2"/> &nbsp;</span></li>
		<li><label>Existing CPAP Patient:</label><span> <form:select
					path="existingCpapPatient">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select>
		</span></li>
		<li><label>CPAP Patient Since:</label><span><form:input
					path="since" cssClass="datepicker" placeholder="dd/MM/yyyy" />&nbsp;</span></li>
	</ul>
	
	<h3>Medical History</h3>
	<ul>
		<li><label>Allergies:</label> <form:radiobutton
				path="allergiesFlag" value="false" cssClass="yes-no-comment" />No&nbsp;<form:radiobutton
				path="allergiesFlag" value="true" cssClass="yes-no-comment" />Yes <br />
			<form:textarea cols="60" rows="1" path="allergies" size="40"
				cssErrorClass="form-error-field"
				placeholder="indicate any allergies that this patient may have." /></li>
		<li><label>Hypertension:</label> <form:radiobutton
				path="hypertensionFlag" value="false" cssClass="yes-no-comment" />No&nbsp;<form:radiobutton
				path="hypertensionFlag" value="true" cssClass="yes-no-comment" />Yes
			<br /> <form:textarea cols="60"
				placeholder="Hypertension or high blood pressure, sometimes called arterial hypertension, is a chronic medical condition in which the blood pressure in the arteries is elevated."
				rows="1" path="hypertension" size="40"
				cssErrorClass="form-error-field" /></li>
		<li><label>Sinus Problems:</label> <form:radiobutton
				path="sinusProblemsFlag" value="false" cssClass="yes-no-comment" />No&nbsp;<form:radiobutton
				path="sinusProblemsFlag" value="true" cssClass="yes-no-comment" />Yes
			<br /> <form:textarea cols="60" rows="1" path="sinusProblems"
				size="40" cssErrorClass="form-error-field"
				placeholder="any issues with the patient's sinuses" /></li>
		<li><label>Cardiac History:</label> <form:radiobutton
				path="cardiacHxFlag" value="false" cssClass="yes-no-comment" />No&nbsp;<form:radiobutton
				path="cardiacHxFlag" value="true" cssClass="yes-no-comment" />Yes <br />
			<form:textarea cols="60" rows="1" path="cardiacHx" size="40"
				cssErrorClass="form-error-field"
				placeholder="Any sickness or issues with the patient's cardiac system" /></li>
		<li><label>Diabetes:</label> <form:radiobutton
				path="diabetesFlag" value="false" cssClass="yes-no-comment" />No&nbsp;<form:radiobutton
				path="diabetesFlag" value="true" cssClass="yes-no-comment" />Yes <br />
			<form:textarea cols="60" rows="1" path="diabetes" size="40"
				cssErrorClass="form-error-field"
				placeholder="Diabetes is a group of metabolic diseases in which a person has high blood sugar, either because the body does not produce enough insulin, or because cells do not respond to the insulin that is produced." /></li>
		<li><label>Chronic Faitgue:</label> <form:radiobutton
				path="chronicFaitqueFlag" value="false" cssClass="yes-no-comment" />No&nbsp;<form:radiobutton
				path="chronicFaitqueFlag" value="true" cssClass="yes-no-comment" />Yes
			<br /> <form:textarea cols="60" rows="1" path="chronicFaitque"
				size="40" cssErrorClass="form-error-field" /></li>
		<li><label>Asthma:</label> <form:radiobutton path="asthmaFlag"
				value="false" cssClass="yes-no-comment" />No&nbsp;<form:radiobutton
				path="asthmaFlag" value="true" cssClass="yes-no-comment" />Yes <br />
			<form:textarea cols="60" rows="1" path="asthma" size="40"
				cssErrorClass="form-error-field"
				placeholder="Asthma is the common chronic inflammatory disease of the airways characterized by variable and recurring symptoms, reversible airflow obstruction, and bronchospasm. Symptoms include wheezing, coughing, chest tightness, and shortness of breath." /></li>
		<li><label>Thyroid History:</label> <form:radiobutton
				path="thyroidHxFlag" value="false" cssClass="yes-no-comment" />No&nbsp;<form:radiobutton
				path="thyroidHxFlag" value="true" cssClass="yes-no-comment" />Yes <br />
			<form:textarea cols="60" rows="1" path="thyroidHx" size="40"
				cssErrorClass="form-error-field"
				placeholder="Any sickness or issues with the patient's Thyroid" /></li>
		<li><label>Epistaxis:</label> <form:radiobutton
				path="epitaxisFlag" value="false" cssClass="yes-no-comment" />No&nbsp;<form:radiobutton
				path="epitaxisFlag" value="true" cssClass="yes-no-comment" />Yes <br />
			<form:textarea cols="60" rows="1" path="epitaxis" size="40"
				cssErrorClass="form-error-field"
				placeholder="Epitaxis or a nosebleed is the relatively common occurrence of hemorrhage from the nose, usually noticed when the blood drains out through the nostrils." /></li>
		<li><label>GERD:</label> <form:radiobutton path="gerdFlag"
				value="false" cssClass="yes-no-comment" />No&nbsp;<form:radiobutton
				path="gerdFlag" value="true" cssClass="yes-no-comment" />Yes <br />
			<form:textarea cols="60" rows="1" path="gerd" size="40"
				cssErrorClass="form-error-field" placeholder="Gerd is" /></li>
		<li><label>Bruxism:</label> <form:radiobutton path="bruxismFlag"
				value="false" cssClass="yes-no-comment" />No&nbsp;<form:radiobutton
				path="bruxismFlag" value="true" cssClass="yes-no-comment" />Yes <br />
			<form:textarea cols="60" rows="1" path="bruxism" size="40"
				cssErrorClass="form-error-field"
				placeholder="Bruxism is characterized by the grinding of the teeth and typically includes the clenching of the jaw." /></li>
		<li><label>Claustrophobia:</label> <form:radiobutton
				path="claustrophobiaFlag" value="false"
				cssClass="yes-no-comment" />No&nbsp;<form:radiobutton
				path="claustrophobiaFlag" value="true" cssClass="yes-no-comment" />Yes
			<br /> <form:textarea cols="60" rows="1" path="claustrophobia"
				size="40" cssErrorClass="form-error-field" placeholder="" /></li>
		<li><label>Acid Reflux Syndrome:</label> <form:radiobutton
				path="acidRefluxSyndromeFlag" value="false"
				cssClass="yes-no-comment" />No&nbsp;<form:radiobutton
				path="acidRefluxSyndromeFlag" value="true" cssClass="yes-no-comment" />Yes
			<br /> <form:textarea cols="60" rows="1" path="acidRefluxSyndrome"
				size="40" cssErrorClass="form-error-field" placeholder="" /></li>
		<li><label>Copd:</label> <form:radiobutton
				path="copdFlag" value="false"
				cssClass="yes-no-comment" />No&nbsp;<form:radiobutton
				path="copdFlag" value="true" cssClass="yes-no-comment" />Yes
			<br /> <form:textarea cols="60" rows="1" path="copd"
				size="40" cssErrorClass="form-error-field" placeholder="" /></li>
	</ul>
	<h3>Sleep History</h3>
	<ul>
		<li><label>Wake Up Urination Events:</label><form:input path="wakeUpUrinationEvents" /></li>
		<li><label>Approx Time of Sleep Onset:</label><span><form:input
					path="approxTimeOfSleepOnset" />&nbsp;</span></li>
		<li><label>Bed time Hour From/To:</label><span><form:input
					path="bedTimeHour" />&nbsp;</span></li>
		<li><label>Snores:</label><span> <form:select
					path="hasSnore">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select>
		</span></li>
		<li><label>Toss And Turns:</label><span> <form:select
					path="hasTossAndTurn">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select>
		</span></li>
		<li><label>Has Dry Mouth:</label><span> <form:select path="hasDryMouth">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select>
		</span></li>
		<li><label>Is Shift Worker:</label><span> <form:select
					path="isShiftWorker">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select>
		</span></li>
		<li><label>Drooling:</label><span> <form:select
					path="droolingFlag">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select>
		</span></li>
		<li><label>Nightmares:</label><span> <form:select
					path="nightmaresFlag">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select>
		</span></li>
		<li><label>Morning Headaches/Migraines:</label><span> <form:select
					path="morningHeadaches">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select>
		</span></li>
		<li><label>Sleeping Pills:</label><span> <form:select
					path="sleepingPills">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select>
		</span></li>
	</ul>