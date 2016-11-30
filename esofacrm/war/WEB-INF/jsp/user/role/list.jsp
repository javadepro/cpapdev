<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Users</span></h1>
</div>
<div style="clear:both" />
<p>Role admin</p>

<table class="listing">
<tr>
<tr>
<th>Id</th>
<th width="100">Role</th>
<th>Description</th>
<th>Action</th>
</tr>
<c:forEach var="authority" items="${authorities}">
<tr>
<td><c:out value="${authority.value.id}"/></td>
<td><c:out value="${authority.value.role}"/></td>
<td><c:out value="${authority.value.description}"/></td>

<td>
<a href="/user/role/view?id=<c:out value="${authority.value.id}"/>" class="open-on-tab" style="color:#333">View</a>&nbsp;|&nbsp;
<a href="/user/role/form?id=<c:out value="${authority.value.id}"/>" class="open-on-tab" style="color:#333">Edit</a>
</td>
</tr>
</c:forEach>
</table>
<br />
<a href="/user/role/form.do" class="open-on-tab bluebtn">Add</a>
<script>
$(".open-on-tab").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	var title = $(this).attr('title');
	$('#mainbody').load(url);
	return false;
});
</script>