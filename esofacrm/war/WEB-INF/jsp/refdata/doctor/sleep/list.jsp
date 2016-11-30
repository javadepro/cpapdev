<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<table class="listing">
<tr>
<th>Id</th>
<th>First name</th>
<th>LastName</th>
<th>email</th>

<th>Action</th>
</tr>
<c:forEach var="doctor" items="${doctors}">
<tr>
<td><c:out value="${doctor.id}"/></td>
<td><c:out value="${doctor.firstName}"/></td>
<td><c:out value="${doctor.lastName}"/></td>
<td><c:out value="${doctor.email}"/></td>

<td><a href="/refdata/doctor/sleep/form?id=<c:out value="${doctor.id}"/>" title="Sleep Doctor - Edit" class="open-on-tab">Edit</a></td>
</tr>
</c:forEach>
</table>
<a href="/refdata/doctor/sleep/form.do" title="Sleep Doctor - Add" class="open-on-tab">Add</a>
<script>
$(".open-on-tab").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	var title = $(this).attr('title');
	$tabs.tabs("add", url, title);
	return false;
});
</script>
