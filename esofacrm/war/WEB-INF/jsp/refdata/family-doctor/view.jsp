<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Family Doctor</span></h1>
</div>
<div style="clear:both" />
<p>Reference data : Family Doctor</p>
<form>
	<h2>Basic Details</h2>
	<ul>
		<li><label>First Name:</label> <c:out value="${familyDoctor.firstName}" /></li>
		<li><label>Last Name:</label>  <c:out value="${familyDoctor.lastName}" /></li>
		<li><label>Email:</label>  <c:out value="${familyDoctor.email}" />&nbsp;</li>
		<li><label>Phone:</label> <c:out value="${familyDoctor.phone}" />&nbsp;</li>
		<li><label>Fax:</label> <c:out value="${familyDoctor.fax}" />&nbsp;</li>
		<li><label>Billing Number:</label>  <c:out value="${familyDoctor.physicianBillingNumber}" /></li>
	</ul>
	<h2>Clinic Address</h2>
	<ul>
		<li><label>Address Line1:</label> <c:out value="${familyDoctor.address.line1}" /></li>
		<li><label>Address Line2:</label><c:out value="${familyDoctor.address.line2}" />&nbsp;</li>
		<li><label>City:</label><c:out value="${familyDoctor.address.city}" />&nbsp;</li>
		<li><label>Province/State:</label> <c:out value="${familyDoctor.address.province}" /></li>
		<li><label>Postal/Zip Code:</label> <c:out value="${familyDoctor.address.postalCode}" /></li>
		<li><label>Country:</label> <c:out value="${familyDoctor.address.country}" /></li>
	</ul>
	<h2>Note</h2>
	<ul>
		<li><label>Comment:</label></li>
	</ul>
	<br style="clear:both" />
	<textarea style="clear:both" cols="60" rows="5" disabled="disabled">
		<c:out value="${familyDoctor.comment}" />
		</textarea>
	<br /><br />
	<a href="/refdata/family-doctor/form?id=<c:out value="${familyDoctor.id}" />" class="bluebtn open-on-same-page">Edit</a>
</form>
<script>
$(".open-on-same-page").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	$('#mainbody').load(url);
	return false;
});
</script>