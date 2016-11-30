<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="formwrapper">
	<div id="title">
		<h1>
			<div id="icon-tools" class="icon32"></div>
			<span style="float: left; margin-left: 5px; margin-top: 6px;">Clinicians</span>
		</h1>
	</div>
	<div style="clear: both" />
	<p>Reference data : Clinicians</p>

	<form:form id="clinician-form" commandName="clinician"
		action="/refdata/clinician/formsubmit" method="POST">
		<form:hidden path="id" />

		<form:errors path="*" cssClass="errorblock" element="div" />
		<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>

		<h2>Basic Details</h2>
		<ul>
			<li><label>First Name:</label> <form:input path="firstname"
					size="40" cssErrorClass="form-error-field" /></li>
			<li><label>Last name:</label> <form:input path="lastname"
					size="40" cssErrorClass="form-error-field" /></li>
			<li><label>Email:</label> <form:input path="email" size="40"
					cssErrorClass="form-error-field" /></li>
			<li><label>Clinic:</label> <form:select path="shop"
					cssErrorClass="form-error-field">
					<form:options items="${shops}" itemLabel="name" />
				</form:select></li>
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
			setupAjaxFormReplace("clinician-form", "formwrapper");
		</script>
	</form:form>
</div>