<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="formwrapper">
	<div id="title">
		<h1>
			<div id="icon-tools" class="icon32"></div>
			<span style="float: left; margin-left: 5px; margin-top: 6px;">Rule Engine Setting</span>
		</h1>
	</div>
	<div style="clear: both" />
	<p>Admin: Rule Engine</p>
	<form:form commandName="rule" action="/admin/rule-engine/formsubmit"
		method="POST" id="rule-engine-form">
		<form:hidden path="id" />

		<form:errors path="*" cssClass="errorblock" element="div" />
		<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>
		<ul>
			<li><label>Name</label> <form:input path="name" size="40"
					cssErrorClass="form-error-field" /> <form:errors cssClass="error"
					path="name" /></li>
			<li><label>Note</label><form:textarea path="note" /></li>
			<li><label>Input class</label> <form:input path="inputClass" size="40"
					cssErrorClass="form-error-field" /> <form:errors cssClass="error"
					path="inputClass" /></li>
			<li><label>Condition</label> <form:input path="condition" size="40"
					cssErrorClass="form-error-field" /> <form:errors cssClass="error"
					path="condition" /></li>
			<li><label>Resources</label><form:textarea path="resources" col="40" rows="5"/></li>
			<li><label>Attribute</label><form:textarea path="attribute"  col="40" rows="5"/></li>
			<li><label>Action:</label> <form:select path="action" 
					cssErrorClass="form-error-field" >
					<form:options items="${actions}"/> 
					</form:select> <form:errors cssClass="error"
					path="action" /></li>
			<li><label>Priority</label> <form:input path="priority" size="5"
					cssErrorClass="form-error-field" /></li>
			<li>
				<label>Date OffSet (When creating alert. in days)</label> 
				<form:input path="dateOffset" size="5" cssErrorClass="form-error-field" />
			</li>					
			<li><input type="submit" value="Save" class="bluebtn" /></li>
		</ul>
		<script>
			setupAjaxFormReplace("rule-engine-form", "formwrapper");
		</script>
	</form:form>
</div>



