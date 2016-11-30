<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Users - IP Restriction List</span></h1>
</div>
<div style="clear:both" />
<p>IP Restriction Admin</p>

<table class="listing">
<tr>
<tr>
<th class="minorinfo">Id</th>
<th width="100">IP Address</th>
<th>Desc</th>
<th>Action</th>
</tr>
<c:forEach var="allowedIP" items="${allowedIPs}">
<tr>
<td class="minorinfo"><c:out value="${allowedIP.value.id}"/></td>
<td><c:out value="${allowedIP.value.ipAddress}"/></td>
<td><c:out value="${allowedIP.value.desc}"/></td>

<td>
<a href="/user/ip/view?id=<c:out value="${allowedIP.value.id}"/>" class="open-on-tab" style="color:#333">View</a>&nbsp;|&nbsp;
<a href="/user/ip/form?id=<c:out value="${allowedIP.value.id}"/>" class="open-on-tab" style="color:#333">Edit</a>&nbsp;|&nbsp;
<a href="/user/ip/delete?id=<c:out value="${allowedIP.value.id}"/>" style="color:#333">Delete</a>

</td>
</tr>
</c:forEach>
</table>
<br />
<a href="/user/ip/form.do" class="open-on-tab bluebtn">Add</a>
<script>
$(".open-on-tab").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	var title = $(this).attr('title');
	$('#mainbody').load(url);
	return false;
});
</script>