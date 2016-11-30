<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Alert Type</span></h1>
</div>
<div style="clear:both" />
<p>Reference data : Alert Type.  Each alert that you add in the system belongs to a alert type and alert sub type. You will need at least one alert subtype for the alert type.  If there is none, just add a default sub type. </p>
<table class="listing">
<tr>
<th class="minorinfo">Id</th>
<th width="200">Alert Type</th>
<th>Color</th>
<th>Category</th>
<th>Action</th>
</tr>

<c:forEach var="alert" items="${alertTypes}">
<tr>
<td  class="minorinfo"><c:out value="${alert.value.id}"/></td>
<td><c:out value="${alert.value.type}"/></td>
<td><span style="color:<c:out value="${alert.value.color}"/>"><c:out value="${alert.value.color}"/></span></td>
<td><c:out value="${alert.value.alertCategory}"/></td>


<td>
	<a href="/refdata/alert-type/view?id=<c:out value="${alert.value.id}"/>" title="Alert Type - Edit" class="open-on-tab" style="color:#333">View</a>&nbsp;|&nbsp;
	<a href="/refdata/alert-type/form?id=<c:out value="${alert.value.id}"/>" title="Alert Type - Edit" class="open-on-tab" style="color:#333">Edit</a>
</td>
</tr>
</c:forEach>
</table>

<br />
<a href="#" class="bluebtn" id="toggle-info">Toggle hidden info</a>
<a class="bluebtn open-on-tab" href="/refdata/alert-type/form" title=" Type - Add" id="add">Add A New Alert Type</a>

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