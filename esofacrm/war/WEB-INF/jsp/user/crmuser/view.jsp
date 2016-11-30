<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">CRM User</span>
	</h1>
</div>
<div style="clear: both" />
<p>CRM User Management</p>
Note: deleting a user is permanent!  Most of the time, setting the user to inactive is sufficient.

<form>
	<h2>Basic Details</h2>

	<ul>
		<li><label>email:</label> <c:out value="${crmUser.email}" /></li>
		<li><label>First name:</label> <c:out value="${crmUser.firstname}" /></li>
		<li><label>Last name:</label> <c:out value="${crmUser.lastname}" /></li>
		<li><label>Initial:</label> <c:out value="${crmUser.initial}" /></li>
		<li><label>alternateEmail:</label> <c:out value="${crmUser.alternateEmail}" /></li>
		<li><label>Shops:</label> 
			<ul style="padding-left: 235px;">
			<c:forEach var="shop" items="${crmUser.shops}">
				<li><c:out value="${shops[shop].name}" /> <c:if test="${shops[shop].displayDropDown != true }">(inactive)</c:if></li>
			</c:forEach>
			
		</ul>
		</li>
		<li><label>Role:</label>
			<ul style="padding-left: 235px;">

				<c:forEach var="auth" items="${crmUser.authorities}">
					<li><c:out value="${authorities[auth].role}" /></li>
				</c:forEach>
			</ul>
		</li>
	</ul>
	
</form>
<br /><br/>
<p>
<a href="/user/crmuser/form?id=<c:out value="${crmUser.id}" />" class="open-on-page bluebtn">Edit</a>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="/user/crmuser/delete?id=<c:out value="${crmUser.id}" />" class="bluebtn">Delete</a>
</p>
<script>
$(".open-on-page").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	$('#mainbody').load(url);
	return false;
});
</script>