<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page session="true" %>
	<c:if test="${sessionScope.companyMode == 'CPAPDIRECT' }">
		<c:set var="isCpapMode">true</c:set>
	</c:if>

	<h3>Medical History</h3>
	<ul>
		<li><label>High Blood Pressure:</label><span> <form:select path="highBloodPressureFlag">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select>
		</span></li>	
		<li><label>Diabietes:</label><span> <form:select path="diabetesFlag">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select>
		</span></li>
		<li><label>Atrial Fibrillation:</label><span> <form:select path="atrialFibrillationFlag">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select>
		</span></li>
		<li><label>Congested Heart Failure:</label><span> <form:select path="congestedHeartFailureFlag">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select>
		</span></li>				 
	</ul>
	<h3>Sleep History</h3>
	<ul>
		<li><label>Wake Up Urination Events:</label><form:input path="wakeUpUrinationEvents" /></li>
		<li><label>Approx Time of Sleep Onset:</label><span><form:input
					path="approxTimeOfSleepOnset" />&nbsp;</span></li>
		<li><label>Bed time Hour From/To:</label><span><form:input
					path="bedTimeHour" />&nbsp;</span></li>
		<li><label>Dry Mouth/Sore Throat::</label><span> <form:select path="hasDryMouth">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select>
		</span></li>
		<li><label>Bruxism:</label><span> <form:select
					path="bruxismFlag">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select>
		</span></li>
		<li><label>Halitosis:</label><span> <form:select
					path="halitosisFlag">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select>
		</span></li>
		<li><label>Snores:</label><span> <form:select
					path="hasSnore">
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
		<li><label>Excessive Day time Sleepiness:</label><span> <form:select
					path="chronicFaitqueFlag">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select>
		</span></li>
	</ul>