
<%--
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://ckeditor.com" prefix="ckeditor" %>
<%@page import="com.ckeditor.CKEditorConfig"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<div id="formwrapper">
	<div id="title">
		<h1>
			<div id="icon-tools" class="icon32"></div>
			<span style="float: left; margin-left: 5px; margin-top: 6px;">Mail Template</span>
		</h1>
	</div>
	<div style="clear: both" />
	<p>Mail Template</p>

	<form:form id="mailer-template-form" commandName="mailTemplate"
		action="/mailer/template/formsubmit" method="POST">
		<form:hidden path="id" />

		<form:errors path="*" cssClass="errorblock" element="div" />
		<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>

		<h2>Mailer Template</h2>
		<ul>
			<li><label>Template Name:</label> <form:input path="name"
					size="40" cssErrorClass="form-error-field" /></li>
			<li><label>Subject:</label> <form:input path="subject"
					size="40" cssErrorClass="form-error-field" /></li>
			<li><label>Body:</label></li>
		</ul>
		<br style="clear:both"/>
						<form:textarea path="body" cols="50" rows="8" />
							<ckeditor:replace replace="body" basePath="/js/ckeditor/" />
		<%-- 
				String value = "";
				Map<String, String> attr = new HashMap<String, String>();
				attr.put("rows", "8");
				attr.put("cols", "50");
				CKEditorConfig settings = new CKEditorConfig();
				settings.addConfigValue("width", "600");
				settings.addConfigValue("toolbar", "Basic");
			--%>
			<%--
			<ckeditor:editor textareaAttributes="<%=attr %>"
				basePath="/js/ckeditor/" config="<%=settings %>"
				editor="body" value="<%= value %>" /> --%>
	<%--	<input type="submit" value="Save" class="bluebtn" />
		
	</form:form>
</div>

 --%>