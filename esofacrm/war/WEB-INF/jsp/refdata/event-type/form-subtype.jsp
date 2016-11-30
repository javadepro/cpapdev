<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Note / Customer Activity History Type</span></h1>
</div>
<div style="clear:both" />
<p>Reference data : Note Subtype</p>
<form:form id="event-subtype-form" method="post"
	action="/refdata/event-type/subtype-formsubmit"
	commandName="eventSubType">
	<h2>SubType Basic Details</h2>
	<form:hidden path="id" />
	<form:hidden path="parentType"  />
	<form:errors path="*" cssClass="errorblock" element="div" />

	<ul>
		<li><label>Note Type:</label>
		<c:out value="${eventType.type}" /></li>
		<li><label>Note SubType:</label> <form:input path="type"
				size="40" cssErrorClass="form-error-field" /> <form:errors
				cssClass="error" path="type" /></li>
		<li><input type="submit" value="Save" class="bluebtn" />
	</ul>

</form:form>
<script>
	setupAjaxForm("event-subtype-form");
</script>
