<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Appointment Preference</span>
	</h1>
</div>
<div style="clear: both" />
<p>Reference data : entries in the appointment preference drop down.</p>
<table class="listing">
<tr>
<th class="minorinfo">Id</th>
<th>Preference</th>
<th>Action</th>
</tr>
<c:forEach var="appointmentPreference" items="${appointmentPreferences}">
<tr>
<td class="minorinfo"><c:out value="${appointmentPreference.value.id}"/></td>
<td><c:out value="${appointmentPreference.value.preference}"/></td>

<td><a href="/refdata/appointment-preference/view?id=<c:out value="${appointmentPreference.value.id}"/>" style="color:#333" class="open-on-tab">View</a>&nbsp;|&nbsp;
<a href="/refdata/appointment-preference/form?id=<c:out value="${appointmentPreference.value.id}"/>" style="color:#333" class="open-on-tab">Edit</a></td>

</tr>
</c:forEach>
</table>
<br />	
<a href="#" class="bluebtn" id="toggle-info">Toggle hidden info</a>
<a href="/refdata/appointment-preference/form" class="bluebtn open-on-tab">Add</a>
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


