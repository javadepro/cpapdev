<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Clinician</span></h1>
</div>
<div style="clear:both" />
<p>Reference data : Clinicians.</p>

<table class="listing">
<tr>
<tr>
<th class="minorinfo">Id</th>
<th>First Name</th>
<th>Last Name</th>
<th>Clinic</th>
<th width="70">Action</th>
</tr>
<c:forEach var="clinician" items="${clinicians}">
<tr>
<td><c:out value="${clinician.value.id}"/></td>
<td><c:out value="${clinician.value.firstname}"/></td>
<td><c:out value="${clinician.value.lastname}"/></td>
<td><c:out value="${shops[clinician.value.shop].name}"/></td>
<td>
<a href="/refdata/clinician/view?id=<c:out value="${clinician.value.id}"/>" class="open-on-tab" style="color:#333">View</a>&nbsp;|&nbsp;
<a href="/refdata/clinician/form?id=<c:out value="${clinician.value.id}"/>" class="open-on-tab" style="color:#333">Edit</a>
</td>
</tr>
</c:forEach>
</table>
<br />
<a href="#" class="bluebtn" id="toggle-info">Toggle hidden info</a>
<a href="/refdata/clinician/form.do" class="open-on-tab bluebtn">Add</a>
<script>
$(".open-on-tab").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	var title = $(this).attr('title');
	$('#mainbody').load(url);
	return false;
});
$("#toggle-info").click(function(e){
	$(".minorinfo").toggle();
});
</script>