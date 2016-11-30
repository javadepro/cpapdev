<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<form:form id="search-form" commandName="customer"
	action="/customer/search/formsubmit" method="POST">

	<div id="title">
		<h1>
			<div id="icon-users" class="icon32"></div>
			<span style="float: left; margin-left: 5px; margin-top: 6px;">Customer
				Search</span>
		</h1>
	</div>
	<div style="clear: both">
	<p class="pageDesc">Use this page to search for customers. It is not a case
		sensitive search. You can do a wildcard match in any of the fields by
		using a * (ie First Name: John*).</p>

	<div>
	<h2>Search Criteria</h2>
	<form:errors path="*" cssClass="errorblock" id="errorblock" />
	<ul>
		<li><label>First Name:</label>
		<form:input path="firstname" /></li>
		<li><label>Last Name</label>
		<form:input path="lastname" /></li>
		<li><label>Health Card Number</label>
		<form:input path="healthCardNumber" placeholder="xxxx xxx xxx" /></li>
		<li><label>Phone (Home)</label>
		<form:input path="phoneHome" placeholder="xxx-xxx-xxxxx" /></li>
		<li><label>Phone (Mobile)</label>
		<form:input path="phoneMobile" placeholder="xxx-xxx-xxxxx" /></li>
		<li><label>Phone (Work)</label>
		<form:input path="phoneOffice" placeholder="xxx-xxx-xxxxx" /></li>
		<li><label>Status</label>
		<form:radiobutton path="active" value="true" />Active&nbsp;<form:radiobutton
				path="active" value="false" />Inactive</li>
	</ul>
	<br />
	<br />
	<input type="submit" value="Search" class="bluebtn" />

	<h2>Search Result</h2>
	<c:choose>
		<c:when test="${customers!=null}">
			<table class="listing">
				<tr>
					<th>Id</th>
					<th>Name</th>
					<th>Clinician</th>
					<th>Action</th>
				</tr>
				<c:forEach var="customer" items="${customers}">
					<tr>
						<td><c:out value="${customer.value.id}" /></td>
						<td><c:out value="${customer.value.name}" /></td>
						<td><c:out
								value="${clinicians[customer.value.clinician].name}" /></td>

						<td><a
							href="/customer/tabview?id=<c:out value="${customer.value.id}" />"
							style="color: #333; text-decoration: none;" class="open-on-tab"><img
								src="/images/view.png" alt="View" />&nbsp;View</a>&nbsp;|&nbsp; <a
							href="/customer/tabedit?id=<c:out value="${customer.value.id}" />"
							style="color: #333; text-decoration: none;" class="open-on-tab"><img
								src="/images/form.png" alt="Edit" />&nbsp;Edit</a>&nbsp;|&nbsp;
								<a href="/pos/invoice-form?customerId=<c:out value="${customer.value.id}" />"  style="color: #333; text-decoration: none;" >
								<img src="/images/pos.png" style="height:16px; width: 16px;" alt="Edit" />&nbsp;POS</a>&nbsp;&nbsp;

						<%--&nbsp;|&nbsp;
			OnePager&nbsp;<a href="/customer/onepagerview?id=<c:out value="${customer.value.id}" />" style="color:#333" class="open-on-tab"><image src="/images/view.png" /></a>&nbsp;
			<a href="/customer/onepageredit?id=<c:out value="${customer.value.id}" />" style="color:#333" class="open-on-tab"><image src="/images/form.png" /></a> --%>
						</td>

					</tr>
				</c:forEach>

			</table>
			<br />
		</c:when>
		<c:when test="${customers==null}">
No Result!
</c:when>
	</c:choose>
</form:form>
<div id="access-deneid" title="Access Denied">
	<p>You do not have access to view or edit this customer. Please ask reception/admin to add to the exception list</p>
</div>
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

