<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page session="true" %>
	<c:if test="${sessionScope.companyMode == 'CPAPDIRECT' }">
		<c:set var="isCpapMode">true</c:set>
	</c:if>
	

<div id="tab-view">
	<div id="tabs">
		<ul id="tab-links">
			<li><a href="#tabs-1">General Information</a></li>
			<li><a href="/customer/tabview/form-insurance?id=${customerWrapper.customer.id}">Insurance
					Information</a></li>
			<li><a href="/customer/tabview/form-medical?id=${customerWrapper.customer.id}">Medical Information</a></li>
			<c:if test="${isCpapMode =='true'}">
			<li><a href="/customer/tabview/form-cpap?customerId=${customerWrapper.customer.id}">Cpap Information</a></li>
			</c:if>
			<!-- <li><a href="/customer/tabview/form-payment?customerId=${customerWrapper.customer.id}">Payment Information</a></li>-->
			<li><a href="/customer/tabview/view-alert?customerId=${customerWrapper.customer.id}">Alerts</a></li>
			<li><a href="/customer/tabview/view-event?customerId=${customerWrapper.customer.id}">Note/Client History</a></li>
			<li><a href="/customer/tabview/view-file?customerId=${customerWrapper.customer.id}">Files</a></li>
		</ul>
		<div id="tab-patient-info">${customerWrapper.customer.firstname} ${customerWrapper.customer.lastname} | ${customerWrapper.customerExtendedInfo.formattedDateOfBirth} | ${customerWrapper.customer.formattedHealthCardNumber} | ${customerWrapper.customer.phoneHome} </div>		
		<div id="tabs-1">
			<%@include file="form-basic.jsp"%>
		</div>
	</div>
	<br/><br/>
	<a href="/customer/tabview?id=${customerWrapper.customer.id}" style="color:#FFF" class="bluebtn open-on-page" id='open-view-on-tab'>Switch to View</a>&nbsp;
</div>
<script>
$("#tabs").tabs();
$("#open-view-on-tab").click(function(e) {
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
</script>