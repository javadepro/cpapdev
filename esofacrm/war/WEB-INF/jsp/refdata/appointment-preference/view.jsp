<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Appointment Preference</span>
	</h1>
</div>
<div style="clear: both" />
<p>Reference data : Appointment Preference</p>
<form>
	<ul>
		<li><label>Preference:</label> <c:out value="${appointmentPreference.preference}" /></li>
	</ul>

</form>
	<br />
	<a href="/refdata/appointment-preference/form?id=<c:out value="${appointmentPreference.id}" />" class="open-on-page bluebtn">Edit</a>
	
<script>
$(".open-on-page").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	$('#mainbody').load(url);
	return false;
});
</script>




