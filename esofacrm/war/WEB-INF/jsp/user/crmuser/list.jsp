<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">CRM Users Admin</span></h1>
</div>
<div style="clear:both" />
<br/>
<br/>
<br/>
<a href="/user/crmuser/list?getActiveOnly=false" class="open-on-page bluebtn">Show All</a>
<br/><br/>
<br/><br/>
<table class="listing">
<tr>
<th class="minorinfo">Id</th>
<th width="100">email</th>
<th>First name</th>
<th>Last name</th>
<th>Initial</th>
<th>Action</th>
</tr>
<c:forEach var="crmUser" items="${crmUsers}">
<tr>
<td class="minorinfo"><c:out value="${crmUser.value.id}"/></td>
<td><c:out value="${crmUser.value.email}"/></td>
<td><c:out value="${crmUser.value.firstname}"/></td>
<td><c:out value="${crmUser.value.lastname}"/></td>
<td><c:out value="${crmUser.value.initial}"/></td>

<td>
<a href="/user/crmuser/view?id=<c:out value="${crmUser.value.id}"/>" class="open-on-tab" style="color:#333">View</a>&nbsp;|&nbsp;
<a href="/user/crmuser/form?id=<c:out value="${crmUser.value.id}"/>" class="open-on-tab" style="color:#333">Edit</a>&nbsp;|&nbsp;
<c:if test="${crmUser.value.active }">
<a href="/user/crmuser/inactive?id=<c:out value="${crmUser.value.id}"/>"  style="color:#333">Deactivate</a>
</c:if>

</td>
</tr>
</c:forEach>
</table>
<br />
<a href="/user/crmuser/form.do" class="open-on-tab bluebtn">Add</a>
<script>
$(".open-on-tab").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	var title = $(this).attr('title');
	$('#mainbody').load(url);
	return false;
});
</script>