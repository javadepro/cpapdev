<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Configuration</span>
	</h1>
</div>
<div style="clear: both" />
<p>Admin : Configuration</p>
<form>
	<ul>
		<li><label>Name:</label> <c:out value="${config.name}" /></li>
		<li><label>Value:</label> <c:out value="${config.value}" /></li>
	</ul>

</form>
	<br />
	<a href="/admin/config/form?id=<c:out value="${config.id}" />" class="open-on-page bluebtn">Edit</a>
	
<script>
$(".open-on-page").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	$('#mainbody').load(url);
	return false;
});
</script>




