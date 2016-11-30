<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Insurance Provider</span>
	</h1>
</div>
<div style="clear: both" />
<p>Reference data : Insurance Provider</p>
<h2>Basic Details</h2>
<form>
	<ul>
		<li><label>Name:</label> <c:out value="${insuranceProvider.name}" /> </li>
		<li><label>Phone:</label><c:out value="${insuranceProvider.phone}" />&nbsp;<b>Ext:</b><c:out value="${insuranceProvider.phoneExt}" />&nbsp;</li>
		<li><label>Address Line1:</label> <c:out value="${insuranceProvider.address.line1}" />&nbsp;</li>
		<li><label>Address Line2:</label> <c:out value="${insuranceProvider.address.line2}" />&nbsp;</li>
		<li><label>City:</label> <c:out value="${insuranceProvider.address.city}" />&nbsp;</li>
		<li><label>Province/State:</label> <c:out value="${insuranceProvider.address.province}" />&nbsp;</li>
		<li><label>Postal/Zip Code:</label> <c:out value="${insuranceProvider.address.postalCode}" />&nbsp;</li>
		<li><label>Country:</label> <c:out value="${insuranceProvider.address.country}" />&nbsp;</li>
	</ul>

</form>
<br />
<a href="/refdata/insurance-provider/form?id=<c:out value="${insuranceProvider.id}" />" class="open-on-tab bluebtn">Edit</a>

<script>
$(".open-on-tab").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	var title = $(this).attr('title');
	$('#mainbody').load(url);
	return false;
});
</script>