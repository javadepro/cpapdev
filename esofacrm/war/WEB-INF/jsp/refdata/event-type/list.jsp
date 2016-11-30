<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Note / Client  Activity History Type</span></h1>
</div>
<div style="clear:both" />
<p>Reference data : Note / Customer Activity History Type.  Each note that you add to a customer's profile belongs to a note type and note sub type. You will need at least one note subtype for the note type.  If there is none, just add a default sub type.. </p>
<table class="listing">
<tr>
<th class="minorinfo">Id</th>
<th width="150">Note Type</th>
<th>Action</th>
</tr>

<c:forEach var="event" items="${eventTypes}">
<tr>
<td class="minorinfo"><c:out value="${event.value.id}"/></td>
<td><c:out value="${event.value.type}"/></td>

<td>
	<a href="/refdata/event-type/view?id=<c:out value="${event.value.id}"/>" title="Note Type - Edit" class="open-on-tab" style="color:#333">View</a>&nbsp;|&nbsp;
	<a href="/refdata/event-type/form?id=<c:out value="${event.value.id}"/>" title="Note Type - Edit" class="open-on-tab" style="color:#333">Edit</a>
</td>
</tr>
</c:forEach>
</table>

<br />
<a href="#" class="bluebtn" id="toggle-info">Toggle hidden info</a>
<a class="bluebtn open-on-tab" href="/refdata/event-type/form" title=" Type - Add" id="add">Add A New Note Type</a>

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