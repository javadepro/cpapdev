<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="formwrapper">
	<div id="title">
		<h1>
			<div id="icon-tools" class="icon32"></div>
			<span style="float: left; margin-left: 5px; margin-top: 6px;">CRM User</span>
		</h1>
	</div>
	<div style="clear: both" />
	<p>CRM User management</p>

	<form:form id="crmuser-form" commandName="crmUser"
		action="/user/crmuser/formsubmit" method="POST">
		<form:hidden path="id" />
		<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>
		<form:errors path="*" cssClass="errorblock" element="div" />

		<h2>Basic Details</h2>
		<ul>
			<li><label>email:</label> <form:input path="email" 
					cssErrorClass="form-error-field" size="40"/>
			</li>
			<li><label>firstname:</label> <form:input path="firstname" 
					cssErrorClass="form-error-field" size="40"/>
			</li>
			<li><label>lastname:</label> <form:input path="lastname" 
					cssErrorClass="form-error-field" size="40"/>
			</li>
			<li><label>initial:</label> <form:input path="initial" 
					cssErrorClass="form-error-field" size="40"/>
			</li>
			<li><label>alternate email:</label> <form:input path="alternateEmail" 
					cssErrorClass="form-error-field" size="40"/>
			</li>			
			<li><label>Shop:</label> 
			<form:select path="shops" cssErrorClass="form-error-field">
				<form:options items="${shops}"
							itemLabel="name" />
			</form:select>
			</li>
			<li><label>Role:</label> 
			<form:select path="authorities" cssErrorClass="form-error-field">
				<form:options items="${authorities}"
							itemLabel="role" />
			</form:select>
			</li>
			<li><label>Status</label>
				<form:select path="active">
					<form:option value="true">Active</form:option>
					<form:option value="false">Inactive</form:option>
				</form:select>
			</li>
			
			<li><input type="submit" value="Save" class="bluebtn" /></li>
		</ul>
		<script>
			setupAjaxFormReplace("crmuser-form", "formwrapper");
		</script>
	</form:form>
</div>