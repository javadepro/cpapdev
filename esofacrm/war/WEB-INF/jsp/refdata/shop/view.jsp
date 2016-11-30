<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Clinics</span>
	</h1>
</div>
<div style="clear: both" />
<p>Reference data : Clinic is the entity which inventories, clinicians
	and users reference to. There are two type of shop, 1) Real - A real
	shop; 2) Virtual - A mean to hold inventories</p>

<form>
	<h2>Basic Details</h2>

	<ul>
		<li><label>Name:</label> <c:out value="${shop.name}" /></li>
		<li><label>Short name:</label> <c:out value="${shop.shortName}" /></li>
		<%--<li><label>Email:</label><c:out value="${shop.email}" /></li> --%>
		<li><label>Phone:</label>
		<c:out value="${shop.phone}" /></li>
		<li><label>Fax:</label> <c:out value="${shop.fax}" /></li>
		<li><label>adpVendorNumber:</label> <c:out value="${shop.adpVendorNumber}" /></li>
		<li><label>hstNumber:</label> <c:out value="${shop.hstNumber}" /></li>
	</ul>
	<h2>Address</h2>
	<ul>
		<li><label>Address Line1:</label> <c:out
				value="${shop.address.line1}" /></li>
		<li><label>Address Line2:</label> <c:out
				value="${shop.address.line2}" />&nbsp;</li>
		<li><label>Province/State:</label> <c:out
				value="${shop.address.province}" /></li>
		<li><label>Postal/Zip Code:</label>
		<c:out value="${shop.address.postalCode}" /></li>
		<li><label>Country:</label> <c:out
				value="${shop.address.country}" /></li>
	</ul>
	<h2>Misc</h2>

	<ul>
		<li><label>Type:</label> <c:out value="${shop.shopType}" />&nbsp;</li>
		<li><label>Active:</label> <c:out value="${shop.displayDropDown}" />&nbsp;</li>
		<li><label>Order:</label>
		<c:out value="${shop.order}" /></li>
	</ul>
</form>
<br />
<sec:authorize access="hasRole('ROLE_SUPER') or hasRole('ROLE_ADMIN')">
<a href="/refdata/shop/form?id=<c:out value="${shop.id}" />" class="open-on-page bluebtn">Edit</a>
</sec:authorize>
<script>
$(".open-on-page").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	$('#mainbody').load(url);
	return false;
});
</script>