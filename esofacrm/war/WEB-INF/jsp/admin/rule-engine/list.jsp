<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Rule
			Engine Setting</span>
	</h1>
</div>
<div style="clear: both" />
<p>Admin : Rule Engine Setting</p>
<table class="listing">
	<tr>
		<th>Id</th>
		<th>Name</th>
		<th>Input Class - Action Class</th>
		<th>Condition</th> 
		<th>Priority</th>
		<th style="width: 70px;">Action</th>
	</tr>
	<c:forEach var="rule" items="${rules}">
		<tr>
			<td><c:out value="${rule.value.id}" /></td>
			<td><c:out value="${rule.value.name}" /></td>
			<td>
				<c:out value="${rule.value.inputClass}" /><br/>
				<c:out value="${rule.value.action}" />			
			</td>
			<td><c:out value="${rule.value.condition}" /></td> 
			<td><c:out value="${rule.value.priority}" /></td>
			<td><a
				href="/admin/rule-engine/view?id=<c:out value="${rule.value.id}"/>"
				style="color: #333" class="open-on-tab">View</a> &nbsp;|&nbsp; <a
				href="/admin/rule-engine/form?id=<c:out value="${rule.value.id}"/>"
				style="color: #333" class="open-on-tab">Edit</a></td>

		</tr>
	</c:forEach>
</table>
<br />
<a href="/admin/rule-engine/form" class="bluebtn open-on-tab">Add</a>
<script>
	$(".open-on-tab").click(function(e) {
		e.preventDefault();
		var url = $(this).attr('href');
		var title = $(this).attr('title');
		$('#mainbody').load(url);
		return false;
	});
</script>


