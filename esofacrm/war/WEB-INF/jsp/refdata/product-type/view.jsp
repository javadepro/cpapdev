<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Product Type</span></h1>
</div>
<div style="clear:both" />
<p>Reference data : Each product belongs in a Product Type and Product Subtype.  You will need at least one product subtype for the product type.  If there is none, just add a default sub type so that the products can be grouped properly.</p>
	<form>
	<h2>Basic Details</h2>
	<ul>
		<li><label>Product Group:</label> <c:out value="${productType.type}" /></li>
	</ul>
	
	<br />
	<a id="add-type" href="/refdata/product-type/form?id=<c:out value="${productType.id}" />" class="bluebtn open-on-same-page">Edit</a> 
	<br />
	</fieldset>
	</form>
	<br /><br /><br />
	<c:if test="${subtypes!=null}">
	<h2>Product Type</h2>
	<table class="listing">
		<tr>
			<th>Id</th>
			<th>Product SubType</th>
			<th>Action</th>
		</tr>
		<c:forEach items="${subtypes}" var="subtype" varStatus="x">
			<tr>
				<td><c:out value="${subtype.id}" /></td>
				<td><c:out value="${subtype.type}" /></td>
				<td>
					<a href="/refdata/product-type/subtype-form?id=<c:out value="${subtype.id}"/>&parentId=<c:out value="${productType.id}" />" class="open-on-same-page" style="color:#333">Edit</a>
				</td>
			</tr>
		</c:forEach>
	</table>
	<br />
	<a id="add-type" href="/refdata/product-type/subtype-form?parentId=<c:out value="${productType.id}" />" title="Add a Subtype" class="bluebtn open-on-same-page">Add a Subtype</a> 
	<br />
	</c:if>
	<br />
	
	<script>
		$(".open-on-same-page").click(function(e){
			e.preventDefault();
			var url = $(this).attr('href');
			var title = $(this).attr('title');
			$('#mainbody').load(url);
			return false;
		});
	</script>
