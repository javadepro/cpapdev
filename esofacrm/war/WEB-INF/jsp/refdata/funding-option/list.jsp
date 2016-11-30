<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Funding Option</span></h1>
</div>
<div style="clear:both" />
<p>Reference data : Funding Options</p>

<table class="listing">
<tr>
<th class="minorinfo">Id</th>
<th>Option</th>
<th>Funding Details Type</th>
<th>Description</th>
<th>Action</th>
</tr>
<c:forEach var="option" items="${fundingOptions}">
<tr>
<td class="minorinfo"><c:out value="${option.value.id}"/></td>
<td><c:out value="${option.value.option}"/></td>
<td><c:out value="${option.value.fundingDetailsType}"/></td>
<td><c:out value="${option.value.description}"/></td>
<td>
<a href="/refdata/funding-option/view?id=<c:out value="${option.value.id}"/>" class="open-on-same-page" style="color:#333;">View</a>&nbsp;|&nbsp;
<a href="/refdata/funding-option/form?id=<c:out value="${option.value.id}"/>" class="open-on-same-page" style="color:#333;">Edit</a>
</td>
</tr>
</c:forEach>
</table>
<br />
<a href="#" class="bluebtn" id="toggle-info">Toggle hidden info</a>
<a href="/refdata/funding-option/form" class="bluebtn open-on-same-page">Add</a>
<script>
$(".open-on-same-page").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	$('#mainbody').load(url);
	return false;
});
$("#toggle-info").click(function(e){
	$(".minorinfo").toggle();
});
</script>
