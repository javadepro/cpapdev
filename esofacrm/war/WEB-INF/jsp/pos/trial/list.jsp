<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="trial-list">
	<div id="title">
		<h1><div id="icon-bubble" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">POS</span></h1>
	</div>
	<div style="clear:both" />
	<h2>Trial/Rental Loaner Machines</h2>
	<br />
	<form:form id="trial-list" commandName="trialItemSearch"
		action="/pos/trial/list" method="GET">
		<b>Location:</b>&nbsp;<form:select path="location">
			<form:option value="" label="ALL" />		
			<form:options items="${shops}" itemLabel="name" />
		</form:select>&nbsp;
	<b>Trial Status:</b>&nbsp;<form:select path="trialStatus">
			<form:option value="" label="ALL" />
			<form:options items="${trialStatuses}"/>
		</form:select>&nbsp;

		<input type="submit" value="Filter" id="refresh-btn" class="bluebtn" />
	</form:form>

	<br /><br />
<table class="listing">
	<tr>
		<th style="width:200px;">Product</th>
		<th style="width:150px;">Product Serial</th>
		<th style="width:100px;">Location</th>
		<th style="width:200px;">Customer Name</th>
		<th>Status</th>
		<th style="width:120px;">Action</th>
	</tr>
	<c:forEach var="item" items="${trialItems}" >
	<tr>
		<td><a href="/product/view?id=${productMap[item.product].id}"  style="color:#333;"><c:out value="${productMap[item.product].name}" /></a></td>
		<td><c:out value="${item.serialNumber}"/></td>
		<td><c:out value="${shops[item.location].name}" /></td>  
		<td><a href="/customer/tabview?id=${customers[item.customerKey].id}"  style="color:#333;" class="open-on-tab"><c:out value="${item.customerFullName }" /></a></td>
		<td><c:out value="${item.trialStatus}" /></td>
		<td>
			<a href="/pos/trial/view?id=${item.id}" style="color:#333;text-decoration:none;" title="View"><img src="/images/view.png" alt="View" />&nbsp;View</a>&nbsp;|&nbsp;
			<a href="/pos/trial/edit?id=${item.id}" style="color:#333;text-decoration:none;" title="edit"><img src="/images/form.png" alt="Edit"/>&nbsp;Edit</a>			
		</td>
	</tr>
	</c:forEach>
</table>
<br />
<br />
<br />
<script>
	$("#access-deneid").dialog({ autoOpen: false,modal: true });
	$(".open-on-tab").click(function(e) {
		e.preventDefault();
		var url = $(this).attr('href');
		var title = $(this).attr('title');
		$('#mainbody').load(url,function(response, status, xhr) {
			  if (status == "error") {
				  	if(xhr.status==403){
				  		//alert($("#access-deneid"));
				  		$("#access-deneid").dialog("open");
				  	}
				  }
				});
		return false;
	});
	setupAjaxForm("search-form");
</script>

</div>
