<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Rule
			Engine</span>
	</h1>
</div>
<div style="clear: both" />
<p>Admin : Rule Engine</p>
<form>
	<ul>
		<li><label>Name:</label> <c:out value="${rule.name}" /></li>
		<li><label>Input Class:</label> <c:out value="${rule.inputClass}" /></li>
		<li><label>Condition:</label> <c:out value="${rule.condition}" /></li>
		<li><label>Action:</label> <c:out value="${rule.action}" /></li>
		<li><label>Resources:</label> <c:out value="${rule.resources}" /></li>
		<li><label>Attribute:</label> <c:out value="${rule.attribute}" /></li>
		
		<li><label>Priority:</label> <c:out value="${rule.priority}" /></li>
		<li><label>Date Offset (in days):</label> <c:out value="${rule.dateOffset}" /></li>
	</ul>
</form>
<br />
<a href="/admin/rule-engine/form?id=<c:out value="${rule.id}" />"
	class="open-on-page bluebtn">Edit</a>

<script>
	$(".open-on-page").click(function(e) {
		e.preventDefault();
		var url = $(this).attr('href');
		$('#mainbody').load(url);
		return false;
	});
</script>




