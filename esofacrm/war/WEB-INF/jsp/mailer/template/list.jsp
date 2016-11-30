<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Mail Template</span></h1>
</div>
<div style="clear:both" />
<p>Mail Template - Use this to send email to customers or product provider.</p>

<table class="listing">
<tr>
<tr>
<th>Id</th>
<th>Template</th>
<th width="70">Action</th>
</tr>
<c:forEach var="template" items="${mailTemplates}">
<tr>
<td><c:out value="${template.value.id}"/></td>
<td><c:out value="${template.value.name}"/></td>
<td>
<a href="/mailer/template/view?id=<c:out value="${template.value.id}"/>" class="open-on-tab" style="color:#333">View</a>&nbsp;|&nbsp;
<a href="/mailer/template/form?id=<c:out value="${template.value.id}"/>" style="color:#333">Edit</a>
</td>
</tr>
</c:forEach>
</table>
<br />
<a href="/mailer/template/form" class="bluebtn">Add</a>
<script>
$(".open-on-tab").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	var title = $(this).attr('title');
	$('#mainbody').load(url);
	return false;
});
</script>