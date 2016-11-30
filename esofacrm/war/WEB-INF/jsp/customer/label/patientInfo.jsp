<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

 <style type="text/css">
      .labeled-li {height:20px;}
 </style>

<form>

<ul>
	<li class="labeled-li"><label style="width:250px;"><c:out value="${customerWrapper.customer.firstname}" />&nbsp;
		<c:out value="${customerWrapper.customer.lastname}" /></label>
		<span><c:out 	value="${customerWrapper.customerExtendedInfo.gender}" /></span>
	</li>
	<li class="labeled-li">
	<label style="width:250px; font-weight:normal;"><c:out value="${customerWrapper.customerExtendedInfo.address.line1}" /></label>
	<span><fmt:formatDate value="${customerWrapper.customerExtendedInfo.dateOfBirth}" type="DATE" pattern="dd/MM/yyyy" />&nbsp;</span></li>
	<li class="labeled-li" style="font-weight: normal;">
		<span>
			<c:out value="${customerWrapper.customerExtendedInfo.address.line2}" />,&nbsp;
			<c:out value="${customerWrapper.customerExtendedInfo.address.city}" />,&nbsp;
			<c:out value="${customerWrapper.customerExtendedInfo.address.province}" />&nbsp;
			<c:out value="${customerWrapper.customerExtendedInfo.address.postalCode}" />&nbsp;
			
	</span></li>	
	<li class="labeled-li"  style="font-weight: normal;"><c:out value="${customerWrapper.customerExtendedInfo.address.country}" />&nbsp;</li>
	<li class="labeled-li"><b>OHIP:</b>&nbsp;<span style="font-weight: normal;"><c:out 
				value="${customerWrapper.customer.healthCardNumber}" />&nbsp;-&nbsp;<c:out 
				value="${customerWrapper.customer.healthCardVersion}" />
	</span></li>
		<li class="labeled-li" style="font-weight:bold;"><c:out value="${sleepDoctors[customerMedicalInfo.sleepDoctor].name}" />&nbsp;</li>
		<li class="labeled-li" style="font-weight:bold;"><c:out value="${clinics[customerMedicalInfo.clinic].name}" />&nbsp;</li>				
</ul>
 
<br />
</form>
