<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="formwrapper">
<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">CPAP Diagnosis</span>
	</h1>
</div>
<div style="clear: both" />
<p>Reference data : CPAP Diagnosis</p>
<form:form commandName="cpapDiagnosis" action="/refdata/cpap-diagnosis/formsubmit"
	method="POST" id="cpapdiagnosis-form">
	<form:hidden path="id" />
	
	<form:errors path="*" cssClass="errorblock" element="div"  />
		<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>
	<ul>
		<li><label>Diagnosis:</label> <form:input path="diagnosis" size="40"
				cssErrorClass="form-error-field" />
				</li>
		<li><input type="submit" value="Save" class="bluebtn"/></li>
	</ul>
	<script>
	setupAjaxFormReplace("cpapdiagnosis-form","formwrapper");
	</script>
</form:form>
</div>



