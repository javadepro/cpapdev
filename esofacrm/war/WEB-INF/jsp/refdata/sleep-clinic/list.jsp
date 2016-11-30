<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Sleep Clinic</span></h1>
</div>
<div style="clear:both" />
<p>Reference data : Sleep Clinic</p>

<table class="listing">
<tr>
<tr>
<th class="minorinfo">Id</th>
<th width="200">Name</th>
<th width="10">ADP Number</th>
<th width="90">Phone</th>
<th width="90">Fax</th>
<th>Address</th>
<th width="70">Action</th>
</tr>
<c:forEach var="sleepClinic" items="${sleepClinics}">
<tr>
<td class="minorinfo"><c:out value="${sleepClinic.value.id}"/></td>
<td><c:out value="${sleepClinic.value.name}"/></td>
<td><c:out value="${sleepClinic.value.adpNumber}"/></td>
<td><c:out value="${sleepClinic.value.phone}"/></td>
<td><c:out value="${sleepClinic.value.fax}"/></td>
<td><c:out value="${sleepClinic.value.address.line1}"/></td>

<td>
<a href="/refdata/sleep-clinic/view?id=<c:out value="${sleepClinic.value.id}"/>" class="open-on-tab" style="color:#333">View</a>&nbsp;|&nbsp;
<a href="/refdata/sleep-clinic/form?id=<c:out value="${sleepClinic.value.id}"/>" class="open-on-tab" style="color:#333">Edit</a>
</td>
</tr>
</c:forEach>
</table>
<br />
<a href="/refdata/sleep-clinic/form.do" class="open-on-tab bluebtn">Add</a>
<script>
$(".open-on-tab").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	var title = $(this).attr('title');
	$('#mainbody').load(url);
	return false;
});
</script>