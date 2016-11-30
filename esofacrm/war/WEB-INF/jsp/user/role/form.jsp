<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="formwrapper">
	<div id="title">
		<h1>
			<div id="icon-tools" class="icon32"></div>
			<span style="float: left; margin-left: 5px; margin-top: 6px;">Users</span>
		</h1>
	</div>
	<div style="clear: both" />
	<p>User Role management</p>

	<form:form id="role-form" commandName="authority"
		action="/user/role/formsubmit" method="POST">
		<form:hidden path="id" />
		<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>
		<form:errors path="*" cssClass="errorblock" element="div" />

		<h2>Basic Details</h2>
		<ul>
			<li><label>Role:</label> <form:input path="role" 
					cssErrorClass="form-error-field" />
				</li>
			<li><label>Description:</label> <form:input path="description"
					 cssErrorClass="form-error-field" /></li>

			
			<li><input type="submit" value="Save" class="bluebtn" /></li>
		</ul>
		<script>
			setupAjaxFormReplace("role-form", "formwrapper");
		</script>
	</form:form>
</div>