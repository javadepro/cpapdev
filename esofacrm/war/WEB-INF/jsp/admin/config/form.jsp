<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="formwrapper">
	<div id="title">
		<h1>
			<div id="icon-tools" class="icon32"></div>
			<span style="float: left; margin-left: 5px; margin-top: 6px;">Configurations</span>
		</h1>
	</div>
	<div style="clear: both" />
	<p>Admin: Configuration</p>
	<form:form commandName="config" action="/admin/config/formsubmit"
		method="POST" id="config-form">
		<form:hidden path="id" />

		<form:errors path="*" cssClass="errorblock" element="div" />
		<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>
		<ul>
			<li><label>Name</label> <form:input path="name" size="40"
					cssErrorClass="form-error-field" /> <form:errors cssClass="error"
					path="name" /></li>
			<li><label>Value</label> <form:input path="value" size="40"
					cssErrorClass="form-error-field" /> <form:errors cssClass="error"
					path="value" /></li>
			<li><label>Note</label> <form:textarea path="note"
					cssErrorClass="form-error-field" /></li>
			<li><input type="submit" value="Save" class="bluebtn" /></li>
		</ul>
		<script>
			setupAjaxFormReplace("config-form", "formwrapper");
		</script>
	</form:form>
</div>



