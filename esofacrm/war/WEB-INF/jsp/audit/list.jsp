<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="audit-list">
	<div id="title">
		<h1><div id="icon-bubble" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Audit</span></h1>
	</div>
	<div style="clear:both" />
	<h2>Audit</h2>
	<br />
	<form:form id="audit-filter" commandName="auditSearch"
		action="/audit/list" method="GET">
		<b>Period:</b>&nbsp;<form:select path="numDays">
			<form:options items="${periodFilter}" />
		</form:select>&nbsp;
	<b>Users:</b>&nbsp;<form:select path="user">
			<form:option value="" label="ALL" />
			<form:options items="${crmusers}" itemLabel="name" />
		</form:select>&nbsp;
		
	<b>Type:</b>&nbsp;<form:select path="entryType">
			<form:option value="" label="ALL" />
			<form:options items="${auditEntryTypes}" />
		</form:select>&nbsp;

		
		<input type="submit" value="Filter" id="refresh-btn" class="bluebtn" />
	</form:form>

	<br /><br />
<table class="listing">
<tr><th style="width:60px;">Date</th><th>User</th><th style="width:100px;">Type</th><th>Audit Details</th><th>User Message</th></tr>
<c:forEach var="audit" items="${auditEntries}" >
<tr>
<td><fmt:formatDate value="${audit.entryDate}" type="DATE" pattern="dd/MM/yyyy"/></td>
<td><c:out value="${crmusers[audit.user].name}" /></td>
<td><c:out value="${audit.entryType}" /></td>  
<td><c:out value="${audit.auditDetails}" escapeXml="false" /></td>  
<td><c:out value="${audit.userMessage}" /></td>
</tr>
</c:forEach>
</table>
<br />
<script>
$(".open-on-same-page").click(function(e) {
	e.preventDefault();
	var url = $(this).attr('href');
	var title = $(this).attr('title');
	$('#mainbody').load(url);
	return false;
});
function refreshList(){
	$("#refresh-btn").click();
}
</script>
</div><!-- End div:id=alert-list -->
