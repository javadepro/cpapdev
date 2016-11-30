<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page session="true" %>
	<c:if test="${sessionScope.companyMode == 'CPAPDIRECT' }">
		<c:set var="isCpapMode">true</c:set>
	</c:if>

<form:form id="medical-form" commandName="customerMedicalInfo"
	action="/customer/medical-form-save" method="POST">
	<h2>Medical Information</h2>
	<form:hidden path="id" />
	<form:hidden path="customer" />
	<form:errors path="*" cssClass="errorblock"></form:errors>
	<c:if test="${message!=null}">
		<div class="messageblock">${message}</div>
	</c:if>
	<ul>
		<li><label>Family Doctor:</label> <form:select
				path="familyDoctor">
				<form:option value="" label="--" />
				<form:options items="${familyDoctors}" itemLabel="name" />
			</form:select></li>
		<c:if test="${isCpapMode == 'true' }">		
			
		<li><label>Sleep Doctor:
			<span style="color: red">*</span>		
		</label>
			<form:select path="sleepDoctor">
				<form:option value="" label="--" />
				<form:options items="${sleepDoctors}" itemLabel="name" />
			</form:select></li>
		<li><label>Sleep Clinic:</label> <form:select path="clinic">
				<form:option value="" label="--" />
				<form:options items="${clinics}" itemLabel="name" />
			</form:select></li>
		</c:if>
		
		<c:if test="${isCpapMode != 'true' }">					
		<li><label>Dentist:</label> <form:select path="dentist">
				<form:option value="" label="--" />
				<form:options items="${dentists}" itemLabel="name" />
			</form:select></li>		
		<li><label>Dental Clinic:</label> <form:select path="dentalClinic">
				<form:option value="" label="--" />
				<form:options items="${dentalClinics}" itemLabel="name" />
			</form:select></li>
		</c:if>
					
		<li><label>Referred By:</label> <form:select path="referredBy">
				<form:option value="" label="--" />
				<form:option label="Family Doctor" value="Family Doctor" />
				<form:option label="Sleep Doctor" value="Sleep Doctor" />
				<c:if test="${isCpapMode != 'true' }">
					<form:option label="Dentist" value="Dentist" />
				</c:if>							
				<form:option label="Other" value="Other" />
			</form:select></li>
			
	</ul>
	
	<c:choose>

		<c:when test="${isCpapMode == 'true' }">
			<%@ include file="form-medical_CPAPDIRECT.jsp"%>
		</c:when>
		<c:otherwise>
			<%@ include file="form-medical_SLEEPMED.jsp"%>
		</c:otherwise>
	</c:choose>
	
	<h3>Personal Information</h3>
	<ul>
		<li><label>Is Smoker:</label><span><form:select
					path="isSmoker">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select></span></li>
		<li><label>Has Dentures:</label><span><form:input
					path="dentures" />&nbsp;</span></li>
		<li><label>Sleeping with Dentures:</label><span><form:select
					path="isSleepsWithDentures">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select></span></li>
		<li><label>Has Pet:</label><span><form:select
					path="hasPet">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select></span></li>
		<li><label>Has Bed Partner:</label><span><form:select
					path="hasSleepPartner">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select></span></li>
		<li><label>Travel Frequency (per Month):</label><span><form:input
					path="travelFreq" placeholder="times per month" />&nbsp;</span></li>
		<li><label>Has Facial Hair:</label><span><form:select
					path="hasFacialHair">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select></span></li>
		<li><label>Occupation:</label><span><form:input
					path="occupation" />&nbsp;</span></li>
		<li><label>Sleep Position:</label><span><form:input
					path="sleepPosition" /></span></li>
		<li><label>Medication List</label><span><form:textarea path="medicationList" col="40" row="1" /></span></li>
	</ul>
	<h3>Other</h3>
	<ul>
		<li><label>Special Medical Note:</label> <form:textarea cols="60"
				rows="5" path="specialMedicalNote" size="40"
				cssErrorClass="form-error-field"
				placeholder="notes for special medical conditions or special care related to their medical conditions." />
			<div class="form-error-message">
				<form:errors path="specialMedicalNote" />
			</div></li>

	</ul>
	<input type="submit" value="Save Medical Information" class="bluebtn" />
	<script>
		$('form').bind(
				'submit',
				function(e) {
					$(this).find('input:submit').attr('disabled', 'disabled')
							.attr('class', 'orangebtn').val("Working...");
					// clean comment if flag is no
					$('.yes-no-comment').each(
							function() {
								var flagname = $(this).attr('name');
								var commentname = flagname.replace('Flag', '');
								var flag = $(
										'input[name=' + flagname + ']:checked')
										.val();
								if (flag != 'true') {
									$('#' + commentname).val('');
								}
							});
				});
		$('.yes-no-comment').bind('change', function(e) {
			var flagname = $(this).attr('name');
			var commentname = flagname.replace('Flag', '');
			var flag = $('input[name=' + flagname + ']:checked').val();
			if (flag == 'true') {
				$('#' + commentname).show();
			} else {
				$('#' + commentname).hide();
			}
		});
		$('.yes-no-comment').change();

		
		$(".datepicker").datepicker({
			dateFormat : "dd/mm/yy",
			changeMonth : true,
			changeYear : true
		});
		$(".autobox").combobox();
  
		setupAjaxForm("medical-form");
	</script>
</form:form>