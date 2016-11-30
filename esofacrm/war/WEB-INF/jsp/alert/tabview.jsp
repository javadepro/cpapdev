<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div id="tab-view">
	<div id="tabs">
		<ul>
			<li><a href="#tabs-1">Alert Summary</a></li>
			<li><a href="/alert/tabview/appointment-alerts">Appointment Alerts</a></li>
			<li><a href="/alert/tabview/contact-alerts">Contact Alerts</a></li>
			<li><a href="/alert/tabview/payment-alerts">Payment Alerts</a></li>

		</ul>
		<div id="tabs-1">
			<%@include file="view-summary.jsp"%>
		</div>
	</div>
</div>
<br />

<script>
$(document).ready(function(){
	$("#tabs").tabs();	
});
</script>

