<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="formwrapper">
	<div id="title">
		<h1>
			<div id="icon-tools" class="icon32"></div>
			<span style="float: left; margin-left: 5px; margin-top: 6px;">Funding Options
				</span>
		</h1>
	</div>
	<div style="clear: both" />
	<p>Reference data : Funding Options</p>

	<form:form id="funding-option-form" commandName="fundingOption"
		action="/refdata/funding-option/formsubmit" method="POST">
		<form:hidden path="id" />
		<form:errors path="*" cssClass="errorblock" element="div" />
		<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>
		<h2>Basic Details</h2>
		<ul>
			<li><label>Option:</label> <form:input path="option" size="40"
					cssErrorClass="form-error-field" />
				<div class="form-error-message">
					<form:errors path="option" />
				</div></li>
			<li><label>Funding Details Type:</label> <form:input
					path="fundingDetailsType" size="40"
					cssErrorClass="form-error-field" />
				<div class="form-error-message">
					<form:errors path="fundingDetailsType" />
				</div></li>			
			<li><label>Description:</label> <form:textarea cols="60"
					rows="5" path="description" size="40"
					cssErrorClass="form-error-field" />
				<div class="form-error-message">
					<form:errors path="description" />
				</div></li>
			<li><input type="submit" value="Save" class="bluebtn" /></li>
		</ul>
		<script>
			setupAjaxFormReplace("funding-option-form", "formwrapper");
		</script>
	</form:form>
</div>
