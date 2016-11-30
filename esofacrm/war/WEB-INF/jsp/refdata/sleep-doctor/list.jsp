<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Sleep Doctor</span></h1>
</div>
<div style="clear:both" />
<p>Reference data : Sleep Doctor</p>

<table class="listing" style="width:100%">
<tr>
<tr>
<th class="minorinfo">Id</th>
<th>Last Name</th>
<th>First Name</th>
<th>HIB</th>
<th>Clinics</th>
<th width="70">Action</th>
</tr>
<c:forEach var="sleepDoctor" items="${sleepDoctors}">
<tr>
<td class="minorinfo"><c:out value="${sleepDoctor.value.id}"/></td>
<td><c:out value="${sleepDoctor.value.lastName}"/></td>
<td><c:out value="${sleepDoctor.value.firstName}"/></td>
<td><c:out value="${sleepDoctor.value.hibNumber}"/></td>
<td>
<c:forEach var="clinic" items="${sleepDoctor.value.clinics}" varStatus="status">
	<c:out value="${clinics[clinic].name}"/><c:if test="${not status.last}">&nbsp;,</c:if>
</c:forEach>
</td>
<td>
<a href="/refdata/sleep-doctor/view?id=<c:out value="${sleepDoctor.value.id}"/>" class="open-on-tab" style="color:#333">View</a>&nbsp;|&nbsp;
<a href="/refdata/sleep-doctor/form?id=<c:out value="${sleepDoctor.value.id}"/>" class="open-on-tab" style="color:#333">Edit</a>
</td>
</tr>
</c:forEach>
</table>
<br />
 <a href="#" class="bluebtn" id="toggle-info">Toggle hidden info</a>
<a href="/refdata/sleep-doctor/form.do" class="open-on-tab bluebtn">Add</a>
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