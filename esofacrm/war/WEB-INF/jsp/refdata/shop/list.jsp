<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Clinics</span></h1>
</div>
<span style="clear:both;" />
<p>Reference data : Clinic is the entity which inventories, clinicians and users reference to. 
<br />There are two type of shop: 
<br />1) Real - A real store location (including satelite locations). In this case, use the location name.
<br />2) Virtual - a sales person that will carry products with them when they make home visits.  In this case, use the person's name.</p>

<table class="listing">
<tr>
<th class="minorinfo">Id</th>
<th width="100">Name</th>
<th>Short Name</th>
<th width="120">Phone</th>
<th width="100">Fax</th>
<th>Address</th>
<th>Is Active</th>
<th width="70">Action</th>
</tr>
<c:forEach var="shop" items="${shops}">
<tr>
<td class="minorinfo"><c:out value="${shop.value.id}"/></td>
<td><c:out value="${shop.value.name}"/></td>
<td><c:out value="${shop.value.shortName}"/></td>
<td><c:out value="${shop.value.phone}"/></td>
<td><c:out value="${shop.value.fax}"/></td>
<td><c:out value="${shop.value.address.line1}"/></td>
<td><c:out value="${shop.value.displayDropDown}"/></td>
<td>
<a href="/refdata/shop/view?id=<c:out value="${shop.value.id}"/>" class="open-on-tab" style="color:#333">View</a>&nbsp;|&nbsp;
<a href="/refdata/shop/form?id=<c:out value="${shop.value.id}"/>" class="open-on-tab" style="color:#333">Edit</a>
</td>
</tr>
</c:forEach>
</table>
<br />
<a href="#" class="bluebtn" id="toggle-info">Toggle hidden info</a>
<a href="/refdata/shop/refresh" class="bluebtn">Refresh</a>
<sec:authorize access="hasRole('ROLE_SUPER') or hasRole('ROLE_ADMIN')">
<a href="/refdata/shop/form.do" class="open-on-tab bluebtn">Add</a>
</sec:authorize>
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