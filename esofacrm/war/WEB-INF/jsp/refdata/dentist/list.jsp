<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Dentist</span></h1>
</div>
<div style="clear:both" />
<p>Reference data : Dentist</p>

<table class="listing">
<tr>
<th class="minorinfo">Id</th>
<th>Last Name</th>
<th>First Name</th>
<th>Phone</th>
<th>Address</th>

<th>Action</th>
</tr>
<c:forEach var="dentist" items="${dentists}">
<tr>
<td class="minorinfo"><c:out value="${dentist.value.id}"/></td>
<td><c:out value="${dentist.value.lastName}"/></td>
<td><c:out value="${dentist.value.firstName}"/></td>
<td><c:out value="${dentist.value.phone}"/></td>
<td><c:out value="${dentist.value.address}"/></td>
<td>
<a href="/refdata/dentist/view?id=<c:out value="${dentist.value.id}"/>" class="open-on-same-page" style="color:#333;">View</a>&nbsp;|&nbsp;
<a href="/refdata/dentist/form?id=<c:out value="${dentist.value.id}"/>" class="open-on-same-page" style="color:#333;">Edit</a>
</td>
</tr>
</c:forEach>
</table>
<br />
<a href="#" class="bluebtn" id="toggle-info">Toggle hidden info</a>
<a href="/refdata/dentist/form" class="bluebtn open-on-same-page">Add</a>
<script>
$(".open-on-same-page").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	$('#mainbody').load(url);
	return false;
});
$("#toggle-info").click(function(e){
	$(".minorinfo").toggle();
});
</script>
