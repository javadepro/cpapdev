<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="formwrapper">
	<div id="title">
		<h1>
			<div id="icon-tools" class="icon32"></div>
			<span style="float: left; margin-left: 5px; margin-top: 6px;">POS Manager Passcode</span>
		</h1>
	</div>
	<div style="clear: both" />
	<p>POS Managers are required to set their passcode.  This passcode is used to allow price overrides
	of your staff during invoice creation.</p>

<form:form id="passCode-form" commandName="userPasscode"
	action="/user/passcode/formsubmit" method="POST">
		<form:hidden path="id" />
		<form:errors path="*" cssClass="errorblock" element="div" />
		<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>
		<c:if test="${errors!=null}">
			<div class="errorblock">${errors}</div>
		</c:if>


		<p>Please set your passcode for <c:out value="${crmUser.name}"/></p> 
		
		<ul>
			<li><label>Passcode:</label> <form:input path="passCode" 
					cssErrorClass="form-error-field" size="40"/>
			</li>
			<li><label>Retype Passcode:</label> <form:input path="reTypePassCode" 
					cssErrorClass="form-error-field" size="40"/>
			</li>
			<li><input type="submit" value="Save" class="bluebtn" /></li>
		</ul>
	</form:form>
</div>