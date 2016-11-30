<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Primary Adp Numbers</span></h1>
</div>
<span style="clear:both;" />
<p>Adp numbers are shared between shares.  Primary Adp Number are used to identify which store's address to use for a given
adp number. 
</p>
			<c:if test="${message!=null}">
				<div class="messageblock">${message}</div>
			</c:if>
<table class="listing">
<tr>
<th class="minorinfo">Id</th>
<th width="100">Adp Number</th>
<th>Shop Name</th>
<th width="90">Action</th>
</tr>
<c:forEach var="p" items="${pais}">
<tr>
<td class="minorinfo"><c:out value="${p.value.id}"/></td>
<td><c:out value="${p.value.adpNumber}"/></td>
<td><c:out value="${shops[p.value.shopKey].shortName}"/></td>
<td>

<sec:authorize access="hasRole('ROLE_SUPER') or hasRole('ROLE_ADMIN')">
<a href="/refdata/shop/pai-form?id=<c:out value="${p.value.id}"/>" class="open-on-tab" style="color:#333">Edit</a>
&nbsp;|&nbsp;<a href="/refdata/shop/pai-delete?id=<c:out value="${p.value.id}"/>" class="open-on-tab" style="color:#333">Remove</a>
</sec:authorize>
</td>
</tr>
</c:forEach>
</table>
<br />
<a href="#" class="bluebtn" id="toggle-info">Toggle hidden info</a>
<a href="/refdata/shop/pai-list" class="bluebtn">Refresh</a>
<sec:authorize access="hasRole('ROLE_SUPER') or hasRole('ROLE_ADMIN')">
<a href="/refdata/shop/pai-form" class="open-on-tab bluebtn">Add</a>
</sec:authorize>
<script>
$("#toggle-info").click(function(e){
	$(".minorinfo").toggle();
});

</script>