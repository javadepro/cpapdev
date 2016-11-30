<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Sleep Doctor</span>
	</h1>
</div>
<div style="clear: both" />
<p>Reference data : Sleep Doctor</p>

<form>
	<h2>Basic Details</h2>

	<ul>
		<li><label>First Name:</label> <c:out value="${sleepDoctor.firstName}" /></li>
		<li><label>Last Name:</label> <c:out value="${sleepDoctor.lastName}" /></li>
		
		<li><label>Email:</label><c:out value="${sleepDoctor.email}" />&nbsp;</li>
		<li><label>Phone:</label>
		<c:out value="${sleepDoctor.phone}" />-<c:out value="${sleepDoctor.ext}" /></li>
		<li><label>Fax:</label> <c:out value="${sleepDoctor.fax}" />&nbsp;</li>
		<li><label>Physician Billing Number:</label> <c:out value="${sleepDoctor.hibNumber}" />&nbsp;</li>
	</ul>
	<h2>Sleep Clinic</h2>
	<ul>
		<li><label>Clinic:</label>
		<table class="listing">
		<tr><th>Clinic</th><tr>
			<c:forEach var="clinic" items="${sleepDoctor.clinics}" >
				<tr><td><c:out value="${sleepClinics[clinic].name}" /></td></tr>
			</c:forEach>
	</table>
	</li>
	</ul>
	<h2>Note</h2>
	<ul>
		<li><label>Comment:</label></li>
	</ul>
	<br style="clear:both" />
	<textarea style="clear:both" cols="60" rows="5" disabled="disabled">
		<c:out value="${sleepDoctor.comment}" />
		</textarea>
	
</form>
<br />
<a href="/refdata/sleep-doctor/form?id=<c:out value="${sleepDoctor.id}" />" class="open-on-page bluebtn">Edit</a>
<script>
$(".open-on-page").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	$('#mainbody').load(url);
	return false;
});
</script>