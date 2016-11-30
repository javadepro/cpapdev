<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.esofa.crm.common.model.Address" %>
<form>
<div id="title">
	<h1>
		<div id="icon-users" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Customer</span>
	</h1>
</div>
<div style="clear: both" />
<a href="/customer/onepageredit?id=<c:out value="${customerwrapper.customer.id}"/>" class="bluebtn open-on-tab">Edit</a>
<%@ include file="view-basic.jsp" %>
<br />
<%@ include file="view-medical.jsp" %>
<br />
<%@ include file="view-insurance.jsp" %>
<br />
<a href="/customer/onepageredit?id=<c:out value="${customerwrapper.customer.id}"/>" class="bluebtn open-on-tab">Edit</a>
<br />
<%--
<%@ include file="view-alerts.jsp" %>
<br />
<%@ include file="view-events.jsp" %>
 --%>
<script>
	$(".datepicker").datepicker({
		dateFormat : "dd/mm/yy",
		changeMonth: true,
		changeYear: true
	});
	$(".open-on-tab").click(function(e){
		e.preventDefault();
		var url = $(this).attr('href');
		var title = $(this).attr('title');
		$('#mainbody').load(url);
		return false;
	});
</script>