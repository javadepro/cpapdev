<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<form:form id="event-form" commandName="eventType"
	action="/refdata/event-type/formsubmit" method="POST" >
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Note / Customer Activity History Type</span></h1>
</div>
<div style="clear:both" />
<p>Reference data : Note / Customer Activity History Type.  Each note that you add to a customer's profile belongs to a note type and note sub type. You will need at least one note subtype for the note type.  If there is none, just add a default sub type.. </p>

<form:hidden path="id" />
	
	<form:errors path="*" cssClass="errorblock" element="div" />
	<h2>Basic Details</h2>
	<ul>
		<li><label>Event Type:</label> <form:input path="type" size="40"
				cssErrorClass="form-error-field" /> <form:errors cssClass="error"
				path="type" /></li>
	</ul>

	
	<input type="submit" value="Save" class="bluebtn" />
	<br /><br /><br />
	<c:if test="${subtypes!=null}">
	<h2>Event Type</h2>
	<table class="listing">
		<tr>
			<th>Id</th>
			<th>Event Type</th>
		</tr>  
		<c:forEach items="${subtypes}" var="subtype" varStatus="x">
			<tr>
				<td><c:out value="${subtype.id}" /></td>
				<td><c:out value="${subtype.type}" /></td>
			</tr>
		</c:forEach>
	</table>
	<br />
	<a id="add-type" href="/refdata/event-type/subtype-form?parentId=<c:out value="${eventType.id}" />" class="bluebtn open-on-tab">Add a Subtype</a> 
	</c:if>
	<br /><br />
	
	<script>
		setupAjaxForm("event-form");
		$(".open-on-tab").click(function(e){
			e.preventDefault();
			var url = $(this).attr('href');
			var title = $(this).attr('title');
			$('#mainbody').load(url);
			return false;
		});
		
	</script>
</form:form>
