<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="formwrapper">
<form:form id="product-form" commandName="productType"
	action="/refdata/product-type/formsubmit" method="POST" >
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Product Type</span></h1>
</div>
<div style="clear:both" />
<p>Reference data : Each product belongs in a Product Type and Product Subtype.  You will need at least one product subtype for the product type.  If there is none, just add a default sub type so that the products can be grouped properly.</p>
<form:hidden path="id" />
	
	<form:errors path="*" cssClass="errorblock" element="div" />
	<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>
	
	<h2>Basic Details</h2>
	<ul>
		<li><label>Product Type:</label> <form:input path="type" size="40"
				cssErrorClass="form-error-field" /> <form:errors cssClass="error"
				path="type" /></li>
	</ul>

	
	<input type="submit" value="Save" class="bluebtn" />
	<br /><br /><br />
	<c:if test="${subtypes!=null}">
	<h2>Product Subtype</h2>
	<table class="listing">
		<tr>
			<th>Id</th>
			<th>Product Subtype</th>
		</tr>
		<c:forEach items="${subtypes}" var="subtype" varStatus="x">
			<tr>
				<td><c:out value="${subtype.id}" /></td>
				<td><c:out value="${subtype.type}" /></td>
			</tr>
		</c:forEach>
	</table>
	<br />
	<a id="add-type" href="/refdata/product-type/subtype-form?parentId=<c:out value="${productType.id}" />" class="bluebtn open-on-tab">Add a Subtype</a> 
	</c:if>
	<br /><br />
	
	<script>
		setupAjaxFormReplace("product-form","formwrapper");
		$(".open-on-tab").click(function(e){
			e.preventDefault();
			var url = $(this).attr('href');
			var title = $(this).attr('title');
			$('#mainbody').load(url);
			return false;
		});
	</script>
</form:form>
</div>