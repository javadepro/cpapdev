<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Customer Profile Temp Access</span></h1>
</div>
<div style="clear:both" />
<p>Customer Profile Temp Access</p>


<form>
	<h2>Details</h2>

	<ul>
		<li><label>Customer:</label> <c:out value="${customers[tempAccess.customer].name}" /></li>
		<li><label>Clinician:</label> <c:out value="${clinicians[tempAccess.crmUser].name}" /></li>
		<li><label>Expiration:</label> <c:out value="${tempAccess.expiration}" /></li>
		<li><label>Note:</label> <c:out value="${tempAccess.note}" /></li>
	</ul>
	
</form>
<br />
<br />
<br />
<a href="/user/ip/form?id=<c:out value="${tempAccess.id}" />" class="open-on-page bluebtn">Edit</a>
<script>
$(".open-on-page").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	$('#mainbody').load(url);
	return false;
});
</script>