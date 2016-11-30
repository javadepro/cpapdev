<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="formwrapper">
<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Discount Reason</span>
	</h1>
</div>
<div style="clear: both" />
<p>Reference data : Discount Reason</p>
<form:form commandName="discountReason" action="/refdata/discount-reason/formsubmit"
	method="POST" id="discount-reason-form">
	<form:hidden path="id" />
	
	<form:errors path="*" cssClass="errorblock" element="div"  />
		<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>
	<ul>
		<li><label>Reason:</label> <form:input path="reason" size="40"
				cssErrorClass="form-error-field" /></li>
		<li><label>Display Order:</label> <form:input path="displayOrder" size="40"
				cssErrorClass="form-error-field" /></li>				
		<li><input type="submit" value="Save" class="bluebtn"/></li>
	</ul>
	<script>
	setupAjaxFormReplace("discount-reason-form","formwrapper");
	</script>
</form:form>
</div>



