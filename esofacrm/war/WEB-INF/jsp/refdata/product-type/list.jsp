<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Product Type</span></h1>
</div>
<div style="clear:both" />
<p>Reference data : Each product belongs in a Product Type and Product Subtype.  You will need at least one product subtype for the product type.  If there is none, just add a default sub type so that the products can be grouped properly. .</p>
<table class="listing">
<tr>
<th class="minorinfo">Id</th>
<th width="150">Product Type</th>
<th>Action</th>
</tr>

<c:forEach var="product" items="${productTypes}">
<tr>
<td class="minorinfo"><c:out value="${product.value.id}"/></td>
<td><c:out value="${product.value.type}"/></td>

<td>
	<a href="/refdata/product-type/view?id=<c:out value="${product.value.id}"/>" title="Product Type - Edit" class="open-on-tab" style="color:#333">View</a>&nbsp;|&nbsp;
	<a href="/refdata/product-type/form?id=<c:out value="${product.value.id}"/>" title="Product Type - Edit" class="open-on-tab" style="color:#333">Edit</a>
</td>
</tr>
</c:forEach>
</table>

<br />
<a href="#" class="bluebtn" id="toggle-info">Toggle hidden info</a>
<a class="bluebtn open-on-tab" href="/refdata/product-type/form" title="Product Type - Add" id="add">Add A New Product Type</a>

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