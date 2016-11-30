<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="formwrapper">
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Alert Type</span></h1>
</div>
<div style="clear:both" />
<p>Reference data : Alert Type</p>
<form:form id="alert-subtype-form" method="post"
	action="/refdata/alert-type/subtype-formsubmit"
	commandName="alertSubType">
	<h2>SubType Basic Details</h2>
	<form:hidden path="id" />
	<form:hidden path="parentType"  />
	<form:errors path="*" cssClass="errorblock" element="div" />
<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>
	<ul>
		<li><label>Alert Group:</label>
		<c:out value="${alertType.type}" /></li>
		<li><label>Alert SubType:</label> <form:input path="type"
				size="40" cssErrorClass="form-error-field" /> <form:errors
				cssClass="error" path="type" /></li>
		<li><input type="submit" value="Save" class="bluebtn" />
	</ul>

</form:form>
<script>
	setupAjaxFormReplace("alert-subtype-form","formwrapper");
</script>
</div>