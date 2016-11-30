<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Manufacturer</span>
	</h1>
</div>
<div style="clear: both" />
<p>Reference data : Manufacturer</p>
<table class="listing">
<tr>
<th class="minorinfo">Id</th>
<th>Name</th>
<th>Action</th>
</tr>
<c:forEach var="manufacturer" items="${manufacturers}">
<tr>
<td class="minorinfo"><c:out value="${manufacturer.value.id}"/></td>
<td><c:out value="${manufacturer.value.name}"/></td>

<td><a href="/refdata/manufacturer/view?id=<c:out value="${manufacturer.value.id}"/>" style="color:#333" class="open-on-tab">View</a>&nbsp;|&nbsp;
<a href="/refdata/manufacturer/form?id=<c:out value="${manufacturer.value.id}"/>" style="color:#333" class="open-on-tab">Edit</a></td>

</tr>
</c:forEach>
</table>
<br />	
<a href="#" class="bluebtn" id="toggle-info">Toggle hidden info</a>
<a href="/refdata/manufacturer/form" class="bluebtn open-on-tab">Add</a>
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


