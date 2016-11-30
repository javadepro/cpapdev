<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Alert Type</span></h1>
</div>
<div style="clear:both" />
<p>Reference data : Alert Type.  Each alert that you add in the system belongs to a alert type and alert sub type. You will need at least one alert subtype for the alert type.  If there is none, just add a default sub type. </p>
	<form>
	<h2>Basic Details</h2>
	<ul>
		<li><label>Alert Group:</label> <c:out value="${alertType.type}" /></li>
		<li><label>Alert Category:</label> <c:out value="${alertType.alertCategory}" /></li>
	</ul>
	
	<br />
	<a id="add-type" href="/refdata/alert-type/form?id=<c:out value="${alertType.id}" />" class="bluebtn open-on-same-page">Edit</a> 
	<br />
	</fieldset>
	</form>
	<br /><br /><br />
	<c:if test="${subtypes!=null}">
	<h2>Alert Type</h2>
	<table class="listing">
		<tr>
			<th>Id</th>
			<th>Alert Type</th>
			<th>Action</th>
		</tr>
		<c:forEach items="${subtypes}" var="subtype" varStatus="x">
			<tr>
				<td><c:out value="${subtype.id}" /></td>
				<td><c:out value="${subtype.type}" /></td>
				<td>
					<a href="/refdata/alert-type/subtype-form?id=<c:out value="${subtype.id}"/>&parentId=<c:out value="${alertType.id}" />" class="open-on-same-page" style="color:#333">Edit</a>
				</td>
			</tr>
		</c:forEach>
	</table>
	<br />
	<a id="add-type" href="/refdata/alert-type/subtype-form?parentId=<c:out value="${alertType.id}" />" title="Add a Subtype" class="bluebtn open-on-same-page">Add a Subtype</a> 
	<br />
	</c:if>
	<br />
	
	<script>
		$(".open-on-same-page").click(function(e){
			e.preventDefault();
			var url = $(this).attr('href');
			var title = $(this).attr('title');
			$('#mainbody').load(url);
			return false;
		});
	</script>
