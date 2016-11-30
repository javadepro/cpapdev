<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Clinics</span>
	</h1>
</div>
<div style="clear: both" />
<p>Reference data : Clinicians.</p>
<form>
	<h2>Basic Details</h2>

	<ul>
		<li><label>First name:</label> <c:out value="${clinician.firstname}" /></li>
		<li><label>Last name:</label> <c:out value="${clinician.lastname}" /></li>
		<li><label>Email:</label><c:out value="${clinician.email}" /></li>
		<li><label>Clinic:</label><c:out value="${shops[clinician.shop].name}" /></li>
	</ul>
	
	<h2>Note</h2>
	<ul>
		<li><label>Comment:</label></li>
	</ul>
	<br style="clear:both" />
	<textarea style="clear:both" cols="60" rows="5" disabled="disabled">
		<c:out value="${clinician.comment}" />
		</textarea>
	
	
</form>
<br />
<a href="/refdata/clinician/form?id=<c:out value="${clinician.id}" />" class="open-on-page bluebtn">Edit</a>
<script>
$(".open-on-page").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	$('#mainbody').load(url);
	return false;
});
</script>