<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="title">
<h1><div id="icon-users" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Customer</span></h1>
</div>
<div style="clear:both" />

<table class="listing">
	<tr>
		<th>Id</th>
		<th>Name</th>
		<th>Clinician</th>
		<th>Action</th>
	</tr>
	<c:forEach var="customer" items="${customers}">
		<tr>
		<td><c:out value="${customer.value.id}" /></td>
			<td><c:out value="${customer.value.name}" /></td>
			<td><c:out value="${clinicians[customer.value.clinician].name}" /></td>

			<td>
			<a href="/customer/tabview?id=<c:out value="${customer.value.id}" />" style="color:#333" class="open-on-tab" title="view"><image src="/images/view.png" /></a>&nbsp;
			<a href="/customer/tabedit?id=<c:out value="${customer.value.id}" />" style="color:#333" class="open-on-tab" title="edit"><image src="/images/form.png" /><%--</a>&nbsp;|&nbsp;
			OnePager&nbsp;<a href="/customer/onepagerview?id=<c:out value="${customer.value.id}" />" style="color:#333" class="open-on-tab"><image src="/images/view.png" /></a>&nbsp;
			<a href="/customer/onepageredit?id=<c:out value="${customer.value.id}" />" style="color:#333" class="open-on-tab"><image src="/images/form.png" /></a> --%>
			</td>
			
		</tr>
	</c:forEach>
	
</table>
<%--<br />
<a href="/customer/list?cursor=<c:out value="${cursor}&page=-2" />">Previous</a>&nbsp;|&nbsp;<a href="/customer/list?cursor=<c:out value="${cursor}" />">Next</a>
<br />--%>
<br /> 

<script>
$(".open-on-tab").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	var title = $(this).attr('title');
	$('#mainbody').load(url);
	return false;
});
</script>

