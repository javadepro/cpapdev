<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
	<form:form id="sleep-doctor-form" commandName="doctor" action="/refdata/doctor/sleep/formsubmit" method="POST">
		<form:hidden path="id" />
		<form:errors path="*" cssClass="errorblock" element="div" />
		<ul>
			<li><label>First Name:</label> <form:input path="firstName"
					size="40" cssErrorClass="form-error-field" />
				<div class="form-error-message">
					<form:errors path="firstName" />
				</div></li>
			<li><label>Last Name:</label> <form:input path="lastName"
					size="40" cssErrorClass="form-error-field" />
				<div class="form-error-message">
					<form:errors path="lastName" />
				</div></li>
			<li><label>HIB Number:</label> <form:input path="hibNumber"
					size="40" cssErrorClass="form-error-field" />
				<div class="form-error-message">
					<form:errors path="hibNumber" />
				</div></li>

			<li><label>Email:</label> <form:input path="email" size="40"
					cssErrorClass="form-error-field" />
				<div class="form-error-message">
					<form:errors path="email" />
				</div></li>
			<li><label>Phone:</label> <form:input path="phone" size="40"
					cssErrorClass="form-error-field" />
				<div class="form-error-message">
					<form:errors path="phone" />
				</div></li>
			<li><label>Fax:</label> <form:input path="fax" size="40"
					cssErrorClass="form-error-field" />
				<div class="form-error-message">
					<form:errors path="fax" />
				</div></li>
			<li><label>Clinic:</label></li>
			<li><form:select path="clinics" multiple="true">
				<form:options items="${clinics}" itemLabel="name" itemValue="id"  />
			</form:select></li>
			<li><label>Comment:</label> <form:textarea path="comment"
					size="40" cssErrorClass="form-error-field" />
				<div class="form-error-message">
					<form:errors path="comment" />
				</div></li>
			<li><input type="submit" value="Save" /></li>
		</ul>
		<script>
	setupAjaxForm("sleep-doctor-form");
	</script>
	</form:form>

	<div id="dialog" title="Clinic">
	</div>
	<script>
		$("#add-clinic").button().click(function() {
			$("#dialog").append("<p>Loading...</p>");
			$("#dialog").dialog("open");
			return false;
		});
		// modal dialog init: custom buttons and a "close" callback reseting the form inside
		var $dialog = $("#dialog").dialog(
				{
					autoOpen : false,
					modal : true,
					buttons : {
						Add : function() {

							var newclinic = $("<button>").append(
									$("#clinic-input").val()).button({
								icons : {
									primary : "ui-icon-close"
								}
							}).click(function() {
								$(this).remove();
								return false;
							});
							var hidden = $('<hidden id="++" value='+$("#clinic-input-hidden").val()+'>');
							$("#clinic-list").append(newclinic);
							$(this).dialog("close");
						},
						Cancel : function() {
							$(this).dialog("close");
						}
					},
					open : function() {
						$("#dialog").load("/refdata/doctor/sleep/clinicform");
						$("#clinic-input").focus();
					},
					close : function() {
						$("#clinic-input").val("");
					}
				});

		
	</script>
</body>
</html>