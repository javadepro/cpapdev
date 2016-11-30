<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Confiuration</span>
	</h1>
</div>
<div style="clear: both" />
<p>Admin : Configuration</p>
<table class="listing">
<tr>
<th>Id</th>
<th>Name</th>
<th>Value</th>
<th>Note</th>
<th>Action</th>


</tr>
<c:forEach var="config" items="${configs}">
<tr>
<td><c:out value="${config.value.id}"/></td>
<td><c:out value="${config.value.name}"/></td>
<td><c:out value="${config.value.value}"/></td>
<td><c:out value="${config.value.note}"/></td>


<td><a href="/admin/config/view?id=<c:out value="${config.value.id}"/>" style="color:#333" class="open-on-tab">View</a>&nbsp;|&nbsp;
<a href="/admin/config/form?id=<c:out value="${config.value.id}"/>" style="color:#333" class="open-on-tab">Edit</a></td>

</tr>
</c:forEach>
</table>
<br />	
<a href="/admin/config/form" class="bluebtn open-on-tab">Add</a>
<script>
$(".open-on-tab").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	var title = $(this).attr('title');
	$('#mainbody').load(url);
	return false;
});
</script>


