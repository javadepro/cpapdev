<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="formwrapper">
	<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Customer Profile Temp Access</span></h1>
</div>
<div style="clear:both" />
<p>Customer Profile Temp Access</p>
	

	<form:form id="temp-access-form" commandName="tempAccess"
		action="/user/temp-access/formsubmit" method="POST">
		<form:hidden path="id" />
		<form:hidden path="grantedBy" />
		
		<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>
		<form:errors path="*" cssClass="errorblock" element="div" />

		<h2>Basic Details</h2>
		<ul>
			<li><label>Customer:</label> <form:select path="customer"
					cssErrorClass="form-error-field" cssClass="combobox">
					<form:options items="${customers}" itemLabel="name" />
				</form:select></li>
			<li><label>Clinician:</label> <form:select path="crmUser"
					cssErrorClass="form-error-field">
					<form:options items="${clinicians}" itemLabel="name" />
				</form:select></li>
			<li><label>Expiration:</label>
			<form:input path="expiration" size="15" cssClass="datepicker" /></li>
			<li><label>Note:</label>
			<form:input path="Note" size="40" /></li>
			<li><input type="submit" value="Save" class="bluebtn" /></li>
		</ul>
		<script>
			setupAjaxFormReplace("temp-access-form", "formwrapper");
			$(".datepicker").datepicker({
				dateFormat : "dd/mm/yy",
				changeMonth : true,
				changeYear : true,
				yearRange: "-1:+2",
				numberOfMonths: 3,
				showButtonPanel: true
			});
			$(".combobox").combobox();
		</script>
	</form:form>
</div>