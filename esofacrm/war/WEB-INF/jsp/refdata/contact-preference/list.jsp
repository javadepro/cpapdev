<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Contact Preference</span>
	</h1>
</div>
<div style="clear: both" />
<p>Reference data : entries in the contact preference drop down.</p>
<table class="listing">
<tr>
<th class="minorinfo">Id</th>
<th>Name</th>
<th>Action</th>
</tr>
<c:forEach var="contactPreference" items="${contactPreferences}">
<tr>
<td class="minorinfo"><c:out value="${contactPreference.value.id}"/></td>
<td><c:out value="${contactPreference.value.preference}"/></td>

<td><a href="/refdata/contact-preference/view?id=<c:out value="${contactPreference.value.id}"/>" style="color:#333" class="open-on-tab">View</a>&nbsp;|&nbsp;
<a href="/refdata/contact-preference/form?id=<c:out value="${contactPreference.value.id}"/>" style="color:#333" class="open-on-tab">Edit</a></td>

</tr>
</c:forEach>
</table>
<br />	
<a href="#" class="bluebtn" id="toggle-info">Toggle hidden info</a>
<a href="/refdata/contact-preference/form" class="bluebtn open-on-tab">Add</a>
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


