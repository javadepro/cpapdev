<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="formwrapper">
<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Contact Preference</span>
	</h1>
</div>
<div style="clear: both" />
<p>Reference data : Contact Preference</p>
<form:form commandName="contactPreference" action="/refdata/contact-preference/formsubmit"
	method="POST" id="contactpreference-form">
	<form:hidden path="id" />
	
	<form:errors path="*" cssClass="errorblock" element="div"  />
		<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>
	<ul>
		<li><label>Name:</label> <form:input path="preference" size="40"
				cssErrorClass="form-error-field" /></li>
		<li><input type="submit" value="Save" class="bluebtn"/></li>
	</ul>
	<script>
	setupAjaxFormReplace("contactpreference-form","formwrapper");
	</script>
</form:form>
</div>



