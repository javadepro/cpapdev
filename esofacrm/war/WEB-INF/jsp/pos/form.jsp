<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">POS</span></h1>


</div>
<p>Daily Sales Screen to capture daily inventory changes and create alert for the sales.<br />
<p>What it actually does:<ol style="margin-left: 20px;"><li>Adjust Inventory level on particular shop</li>
<li>Create a sales event for customer</li>
<li>Create a 6 month followup event for customer</li></ol></p>
<div style="clear:both" />
<form:form commandName="salesForm"
	action="/pos/formsubmit" method="POST">
	<h2>Basic Details</h2>
	<ul>
		<li><label>Sales Date:</label><form:input path="date"  class="datepicker"/></li>
		<li><label>Clinic:</label><form:select path="shop" >
		<form:option value="" label="--" />
		<form:options items="${shops}" itemLabel="name"/>
		</form:select></li>
	</ul>
	<h2>Sale Items</h2>
<table class="listing">
<tr>
<th></th><th>Customer</th><th>Product</th><th>Qty</th><th>Status</th>
</tr>
<c:forEach var="item" items="${salesForm.items}" varStatus="x">
<tr>
<td>${x.index+1}</td>
<td><form:select path="items[${x.index}].customer" cssClass="combobox">
<form:option value="" label="--" />
<form:options items="${customers}" itemLabel="name" />
</form:select></td>
<td><form:select path="items[${x.index}].product" cssClass="combobox">
<form:option value="" label="--" />
<form:options items="${products}" itemLabel="name" />
</form:select>
</td>
<td><form:input path="items[${x.index}].qty" /></td>
<td><c:out value="${salesForm.items[x.index].status}" /></td>
</tr>
</c:forEach>
</table>
<br />
<input type="submit" value="Submit" class="bluebtn" />
</form:form>
<script>
jQuery.fn.log = function (msg) {
	  console.log("%s: %o", msg, this);
	  return this;
	};

	$(".datepicker").datepicker({
		dateFormat : "dd/mm/yy"
	});
	$(".open-on-tab").click(function(e){
		e.preventDefault();
		var url = $(this).attr('href');
		var title = $(this).attr('title');
		$('#mainbody').load(url);
		return false;
	});
	$(".combobox").combobox();
	
	</script>