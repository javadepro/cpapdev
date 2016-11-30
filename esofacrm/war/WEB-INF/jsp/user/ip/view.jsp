<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Users</span>
	</h1>
</div>
<div style="clear: both" />
<p>IP Address Restriction Management</p>

<form>
	<h2>Basic Details</h2>

	<ul>
		<li><label>IP address:</label> <c:out value="${allowedIP.ipAddress}" /></li>
		<li><label>Description:</label> <c:out value="${allowedIP.desc}" /></li>
		
	</ul>
	
</form>
<br />
<a href="/user/ip/form?id=<c:out value="${allowedIP.id}" />" class="open-on-page bluebtn">Edit</a>
<script>
$(".open-on-page").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	$('#mainbody').load(url);
	return false;
});
</script>