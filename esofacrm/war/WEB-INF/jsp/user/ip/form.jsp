<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="formwrapper">
	<div id="title">
		<h1>
			<div id="icon-tools" class="icon32"></div>
			<span style="float: left; margin-left: 5px; margin-top: 6px;">IP Restriction</span>
		</h1>
	</div>
	<div style="clear: both" />
	<p>IP Address management</p>

	<form:form id="ip-form" commandName="allowedIP"
		action="/user/ip/formsubmit" method="POST">
		<form:hidden path="id" />
		<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>
		<form:errors path="*" cssClass="errorblock" element="div" />

		<h2>Basic Details</h2>
		<ul>
			<li><label>IP Address:</label> <form:input path="ipAddress" 
					cssErrorClass="form-error-field" size="40"/>
				</li>
			<li><label>Description:</label> <form:input path="desc"
					 cssErrorClass="form-error-field" />

			
			<li><input type="submit" value="Save" class="bluebtn" /></li>
		</ul>
		<script>
			setupAjaxFormReplace("ip-form", "formwrapper");
		</script>
	</form:form>
</div>