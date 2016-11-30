<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Sleep Clinic</span>
	</h1>
</div>
<div style="clear: both" />
<p>Reference data : Sleep Clinic</p>

<form>
	<h2>Basic Details</h2>

	<ul>
		<li><label>Name:</label> <c:out value="${sleepClinic.name}" /></li>
		<li><label>ADP Number:</label> <c:out value="${sleepClinic.adpNumber}" /></li>
		<li><label>Email:</label><c:out value="${sleepClinic.email}" />&nbsp;</li>
		<li><label>Phone:</label>
		<c:out value="${sleepClinic.phone}" />&nbsp;<b>Ext:</b>&nbsp;<c:out value="${sleepClinic.phoneExt}" /></li>
		<li><label>Fax:</label><c:out value="${sleepClinic.fax}" />&nbsp;</li>
	</ul>
	<h2>Address</h2>
	<ul>
		<li><label>Address Line1:</label> <c:out
				value="${sleepClinic.address.line1}" /></li>
		<li><label>Address Line2:</label> <c:out
				value="${sleepClinic.address.line2}" />&nbsp;</li>
		<li><label>Province/State:</label> <c:out
				value="${sleepClinic.address.province}" /></li>
		<li><label>Postal/Zip Code:</label>
		<c:out value="${sleepClinic.address.postalCode}" /></li>
		<li><label>Country:</label> <c:out
				value="${sleepClinic.address.country}" /></li>
	</ul>
	<h2>Note</h2>
	<ul>
		<li><label>Comment:</label></li>
	</ul>
	<br style="clear:both" />
	<textarea style="clear:both" cols="60" rows="5" disabled="disabled">
		<c:out value="${sleepClinic.comment}" />
		</textarea>
	
</form>
<br />
<a href="/refdata/sleep-clinic/form?id=<c:out value="${sleepClinic.id}" />" class="open-on-page bluebtn">Edit</a>
<script>
$(".open-on-page").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	$('#mainbody').load(url);
	return false;
});
</script>