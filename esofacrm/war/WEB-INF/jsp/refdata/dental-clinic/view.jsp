<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Dental Clinic</span>
	</h1>
</div>
<div style="clear: both" />
<p>Reference data : Dental Clinic</p>

<form>
	<h2>Basic Details</h2>

	<ul>
		<li><label>Name:</label> <c:out value="${dentalClinic.name}" /></li>
		<li><label>Email:</label><c:out value="${dentalClinic.email}" />&nbsp;</li>
		<li><label>Phone:</label>
		<c:out value="${dentalClinic.phone}" />&nbsp;<b>Ext:</b>&nbsp;<c:out value="${dentalClinic.phoneExt}" /></li>
		<li><label>Fax:</label><c:out value="${dentalClinic.fax}" />&nbsp;</li>
	</ul>
	<h2>Address</h2>
	<ul>
		<li><label>Address Line1:</label> <c:out
				value="${dentalClinic.address.line1}" /></li>
		<li><label>Address Line2:</label> <c:out
				value="${dentalClinic.address.line2}" />&nbsp;</li>
		<li><label>Province/State:</label> <c:out
				value="${dentalClinic.address.province}" /></li>
		<li><label>Postal/Zip Code:</label>
		<c:out value="${dentalClinic.address.postalCode}" /></li>
		<li><label>Country:</label> <c:out
				value="${dentalClinic.address.country}" /></li>
	</ul>
	<h2>Note</h2>
	<ul>
		<li><label>Comment:</label></li>
	</ul>
	<br style="clear:both" />
	<textarea style="clear:both" cols="60" rows="5" disabled="disabled">
		<c:out value="${dentalClinic.comment}" />
		</textarea>
	
</form>
<br />
<a href="/refdata/dental-clinic/form?id=<c:out value="${dentalClinic.id}" />" class="open-on-page bluebtn">Edit</a>
<script>
$(".open-on-page").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	$('#mainbody').load(url);
	return false;
});
</script>