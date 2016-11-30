<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Discount Reason</span>
	</h1>
</div>
<div style="clear: both" />
<p>Reference data : Discount Reason</p>
<form>
	<ul>
		<li><label>Reason:</label> <c:out value="${discountReason.reason}" /></li>
	</ul>

</form>
	<br />
	<a href="/refdata/discount-reason/form?id=<c:out value="${discountReason.id}" />" class="open-on-page bluebtn">Edit</a>
	
<script>
$(".open-on-page").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	$('#mainbody').load(url);
	return false;
});
</script>




