<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Users</span>
	</h1>
</div>
<div style="clear: both" />
<p>Users Role Management</p>

<form>
	<h2>Basic Details</h2>

	<ul>
		<li><label>User:</label> <c:out value="${user.user}" /></li>
		<li><label>Role:</label> <c:out value="${user.role}" /></li>
		
	</ul>
	
</form>
<br />
<a href="/user/role/form?id=<c:out value="${user.id}" />" class="open-on-page bluebtn">Edit</a>
<script>
$(".open-on-page").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	$('#mainbody').load(url);
	return false;
});
</script>