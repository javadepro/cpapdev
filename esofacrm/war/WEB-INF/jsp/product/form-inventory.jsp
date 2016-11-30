<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form:form id="inventory-form" commandName="inventories" action="/product/inventory-formsubmit"
	method="POST">
		
	<form:errors path="*" cssClass="errorblock" element="div" />
		<c:if test="${errors!=null}">hi
			<div class=errorblock>${errors}</div>
		</c:if>
		<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>
<table>
	<tr>
		<td>		
			<table class="listing">
				<tr>
					<th>Clinic</th>
					<th>Qty</th>
					<th>Threshold</th>
				</tr>
				<c:forEach var="shop" items="${shops}" varStatus="x">
					<tr>
						<td><c:out value="${shop.shortName}" /></td>
						<form:hidden path="inventories[${x.index}].id" />
						<form:hidden path="inventories[${x.index}].shop" />
						<form:hidden path="inventories[${x.index}].product" />
						<td><form:input path="inventories[${x.index}].qty" size="5"/></td> 
						<td><form:input path="inventories[${x.index}].threshold" size="5"/></td>
					</tr>
				</c:forEach>
			</table>
		</td>
		<td style="padding-left: 30px;">
			<h3>Enter Reason for Updating Inventory:</h3><br/>
			<form:textarea path="inventoryChangeComment" rows="7" cols="50" />		
		</td>
	</tr>
</table>
<br />
<input type="submit" value="Save Inventory Information" class="bluebtn"/>
<script>
			setupAjaxForm("inventory-form");
		</script>
</form:form>
