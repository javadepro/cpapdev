<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<form:form id="cpap-form" commandName="customerMedicalInfoWrapper"
	action="/customer/cpap-form-save" method="POST">
	<h2>Cpap Information</h2>

	<form:hidden path="customerCpapTrialInfo.id" />	
	<form:hidden path="customerMedicalInfo.id" />
	<form:hidden path="customerCpapTrialInfo.customer" />
	<form:hidden path="customerMedicalInfo.customer" />
	
	<form:errors path="*" cssClass="errorblock"></form:errors>
	<c:if test="${message!=null}">
		<div class="messageblock">${message}</div>
	</c:if>
	
	<ul>
		<li><label>CPAP Pressure:</label><span><form:input
					path="customerMedicalInfo.cpapPressure" />
					</span></li>
		<li><label>Ramp:</label><span> <form:select path="customerMedicalInfo.hasRamp"
					id="has-ramp">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select>
		</span></li>
		<li><label>Ramp Mins:</label><span> 
			<form:input path="customerMedicalInfo.rampMins"/>
		</span></li> 
		<li><label>Ramp From/To:</label><span><form:input
					path="customerMedicalInfo.ramp" />&nbsp;</span></li>
		<li><label>EPR or C-Flex:</label><span> <form:select path="customerMedicalInfo.hasEprCFlex">
					<form:option label="Yes" value="true" />
					<form:option label="No" value="false" />
				</form:select>
				<form:input path="customerMedicalInfo.eprCFlexDesc"/>
		</span></li>		
	</ul>
	
	<br/>
	<h3>Current CPAP Machine</h3>
	<ul>
		<li><label>Model:</label><span> <form:select
			path="customerMedicalInfo.currentCpapMachine" cssClass="autobox">
			<form:option value="" label="--" />
			<form:options items="${machines}" itemLabel="name" />
		</form:select></span></li>
		<li><label>Serial Number:</label><span><form:input
			path="customerMedicalInfo.currentCpapMachineSerial" />&nbsp;</span></li>
		<li><label>Humidifier Serial Number:</label><span><form:input
			path="customerMedicalInfo.currentCpapMachineHumidifierSerial" />&nbsp;</span></li>
		<li><label>CPAP Purchase Date:</label><span> <form:input
					path="customerMedicalInfo.cpapPurchaseDate" cssClass="datepicker" placeholder="dd/MM/YYYY" />
		</span></li>			
	
	</ul>
	<h3>Greater than 5 Year Replacement CPAP Machine</h3>

	<ul> 
				<li><label>Model:</label><span> <form:select
					path="customerMedicalInfo.replacementCpapMachine" cssClass="autobox">
					<form:option value="" label="--" />
					<form:options items="${machines}" itemLabel="name"  />
				</form:select></li>
				<li><label>Serial Number:</label><span><form:input
					path="customerMedicalInfo.replacementCpapMachineSerial" />&nbsp;</span></li>
				<li><label>Humidifier Serial Number:</label><span><form:input
					path="customerMedicalInfo.replacementCpapMachineHumidifierSerial" />&nbsp;</span></li>
	
	</ul>
	<br/>
	<br/>
	<ul>

		<li><label>Current Mask:</label><span> <form:select
					path="customerMedicalInfo.currentMask" cssClass="autobox">
					<form:option value="" label="--" />					
					<form:options items="${masks}" itemLabel="name" />
				</form:select></li>
		<li><label>Mask Purchase Date:</label><span> <form:input
					path="customerMedicalInfo.maskPurchaseDate" cssClass="datepicker" placeholder="dd/MM/YYYY" />
		</span></li>
		<li><label>CPAP Purchase Notes:</label><span> <form:textarea
					cols="60" rows="5" path="customerMedicalInfo.cpapPurchaseNotes" size="40"
					cssErrorClass="form-error-field"
					placeholder="notes regarding the trials or cpap related purchases should go here. For other notes or notes that belongs into the patient's file permanently, please use the notes / client activity history. " />
		</span></li>
	</ul> 
	
	<h3>CPAP Trial Information</h3>
	<ul>
		<li><label>CPAP Deposit Amount:</label><span><form:input path="customerCpapTrialInfo.cpapDepositAmt"/></span></li>
		<li><label>CPAP Received By:</label><span><form:input path="customerCpapTrialInfo.cpapReceivedBy"/></span></li>
			<li><label>CPAP Rental Start Date:</label><span> <form:input
			path="customerCpapTrialInfo.cpapRentalStart" cssClass="datepicker" placeholder="dd/MM/YYYY" />		
		<li><label>CPAP Rental End Date:</label><span> <form:input
			path="customerCpapTrialInfo.cpapRentalEnd" cssClass="datepicker" placeholder="dd/MM/YYYY" />
	</ul>
	<br>
	<ul>	
		<li><label>APAP Deposit Amount:</label><span><form:input path="customerCpapTrialInfo.apapDepositAmt"/></span></li>
		<li><label>APAP Received By:</label><span><form:input path="customerCpapTrialInfo.appaReceivedBy"/></span></li>
		<li><label>APAP Rental Start Date:</label><span> <form:input
			path="customerCpapTrialInfo.apapRentalStart" cssClass="datepicker" placeholder="dd/MM/YYYY" />
		
		<li><label>APAP Rental End Date:</label><span> <form:input
			path="customerCpapTrialInfo.apapRentalEnd" cssClass="datepicker" placeholder="dd/MM/YYYY" />				
	</ul>
	<br/>
	<input type="submit" value="Save Cpap Information" class="bluebtn" />
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
		//$('.timepicker').timepicker();
		$(document).ready(function() {
			if ($("#has-ramp").val() == "false") {
				<%-- R1.2 change request. To be removed.
				$("#ramp-from").attr("disabled", "disabled");
				$("#ramp-to").attr("disabled", "disabled");
				--%>
				$("#ramp").attr("disabled", "disabled");
				$("#rampMins").attr("disabled", "disabled");
			} else {
				<%-- R1.2 change request. To be removed.
				$("#ramp-from").removeAttr("disabled");
				$("#ramp-to").removeAttr("disabled");
				--%>
				$("#ramp").removeAttr("disabled");
				$("#rampMins").removeAttr("disabled");
			}
			if($("#hasEprCFlex").val() == "false") {
				$("#eprCFlexDesc").attr("disabled","disabled");
			} else {
				$("#eprCFlexDesc").removeAttr("disabled");
			}
		});
		$('#has-ramp').change(function() {
			if ($(this).val() == "false") {
				<%-- R1.2 change request. To be removed.
				$("#ramp-from").attr("disabled", "disabled");
				$("#ramp-to").attr("disabled", "disabled");
				--%>
				$("#ramp").attr("disabled", "disabled");
				$("#rampMins").attr("disabled", "disabled");
			} else {
				<%-- R1.2 change request. To be removed.
				$("#ramp-from").removeAttr("disabled");
				$("#ramp-to").removeAttr("disabled");
				--%>
				$("#ramp").removeAttr("disabled");
				$("#rampMins").removeAttr("disabled");
			}
		});
		$("#hasEprCFlex").change(function() {
			if($(this).val() == "false") {
				$("#eprCFlexDesc").attr("disabled","disabled");
			} else {
				$("#eprCFlexDesc").removeAttr("disabled");
			}
		});
		setupAjaxForm("cpap-form");
	</script>
</form:form>