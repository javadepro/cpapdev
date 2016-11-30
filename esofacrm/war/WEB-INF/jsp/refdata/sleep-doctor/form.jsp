<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="formwrapper" >
<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Sleep
			Doctor</span>
	</h1>
</div>
<div style="clear: both" />
<p>Reference data : Sleep Doctor</p>

<form:form id="sleep-doctor-form" commandName="sleepDoctor"
	action="/refdata/sleep-doctor/formsubmit" method="POST">
	<form:hidden path="id" />

	<form:errors path="*" cssClass="errorblock" element="div" />
<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>
	<h2>Basic Details</h2>
	<ul>
		<li><label>First Name:</label> <form:input path="firstName" size="40"
				cssErrorClass="form-error-field" />
			</li>
		<li><label>Last Name:</label> <form:input path="lastName" size="40"
				cssErrorClass="form-error-field" />
			</li>
		<li><label>Email:</label> <form:input path="email" size="40"
				cssErrorClass="form-error-field" />
			</li>
		<li><label>Phone:</label> <form:input path="phone" size="40"
				cssErrorClass="form-error-field" />
			</li>
		<li><label>Phone Ext:</label> <form:input path="ext" size="40"
				cssErrorClass="form-error-field" />
			</li>
		<li><label>Fax:</label> <form:input path="fax" size="40"
				cssErrorClass="form-error-field" />
			</li>
		<li><label>HIB Number:</label> <form:input path="hibNumber"
				size="40" cssErrorClass="form-error-field" />
			</li>
	</ul>

	<h2>Sleep Clinic</h2>
	<ul>
		<li><label>Sleep Clinic:</label> <form:select path="clinics"
				multiple="true">

				<form:options items="${sleepClinics}" itemLabel="name" />

			</form:select>
			<div class="form-error-message">
				<form:errors path="ext" />
			</div></li>
	</ul>
	<h2>Note</h2>
	<ul>
		<li><label>Comment:</label> <form:textarea cols="60" rows="5"
				path="comment" size="40" cssErrorClass="form-error-field" />
			<div class="form-error-message">
				<form:errors path="comment" />
			</div></li>
	</ul>
	<input type="submit" value="Save" class="bluebtn" />
	<script>
		setupAjaxFormReplace("sleep-doctor-form","formwrapper");
	</script>
</form:form>
</div>