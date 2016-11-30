<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Insurance Provider</span>
	</h1>
</div>
<div style="clear: both" />
<p>Reference data : Insurance Provider</p>

<table class="listing">
	<tr>
		<th class="minorinfo">Id</th>
		<th>Name</th>
<!-- 		<th>email</th>
		<th>phone</th>
		<th>address</th> -->
		<th>Action</th>
	</tr>
	<c:forEach var="insurance" items="${insuranceProviders}">
		<tr>
			<td class="minorinfo"><c:out value="${insurance.value.id}" /></td>
			<td><c:out value="${insurance.value.name}" /></td>
<!-- 			<td><c:out value="${insurance.value.email}" /></td>
			<td><c:out value="${insurance.value.phone}" /></td>
			<td><c:out value="${insurance.value.address.line1}" /></td>
-->
			<td><a
				href="/refdata/insurance-provider/view?id=<c:out value="${insurance.value.id}"/>"
				 class="open-on-tab" style="color:#333;">View</a>&nbsp;|&nbsp;
			<a
				href="/refdata/insurance-provider/form?id=<c:out value="${insurance.value.id}"/>"
				 class="open-on-tab" style="color:#333;">Edit</a>
			</td>
		</tr>
	</c:forEach>
</table>
<br />
<a href="#" class="bluebtn" id="toggle-info">Toggle hidden info</a>

<sec:authorize access="hasRole('ROLE_SUPER') or hasRole('ROLE_ADMIN')">
<a href="/refdata/insurance-provider/form"
	title="Insurance Provider - Add" class=" bluebtn open-on-tab">Add</a>
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