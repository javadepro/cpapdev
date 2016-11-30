<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Customer Profile Temp Access</span></h1>
</div>
<div style="clear:both" />
<p>Customer Profile Temp Access</p>

<c:if test="${message!=null}">
		<div class="messageblock">${message}</div>
	</c:if>
<table class="listing">
<tr>
<th class="minorinfo">Id</th>
<th>Customer</th>
<th>Clinician</th>
<th>Expiration</th>
<th>Note</th>
<th>Action</th>
</tr>
<c:forEach var="tempAccess" items="${tempAccesses}">
<tr>
<td class="minorinfo"><c:out value="${tempAccess.value.id}"/></td>
<td><c:out value="${customers[tempAccess.value.customer].name}"/></td>
<td><c:out value="${clinicians[tempAccess.value.crmUser].name}"/></td>
<td><c:out value="${tempAccess.value.expiration}"/></td>
<td><c:out value="${tempAccess.value.note}"/></td>
<td>
<a href="/user/temp-access/view?id=<c:out value="${tempAccess.value.id}"/>" class="open-on-tab" style="color:#333">View</a>&nbsp;|&nbsp;
<a href="/user/temp-access/form?id=<c:out value="${tempAccess.value.id}"/>" class="open-on-tab" style="color:#333">Edit</a>&nbsp;|&nbsp;
<a href="/user/temp-access/delete?id=<c:out value="${tempAccess.value.id}"/>" style="color:#333">Delete</a>

</td>
</tr>
</c:forEach>
</table>
<br />
<a href="/user/temp-access/form.do" class="open-on-tab bluebtn">Add</a>
<script>
$(".open-on-tab").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	var title = $(this).attr('title');
	$('#mainbody').load(url);
	return false;
});
</script>