<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

 <style type="text/css">
      .labeled-li {height:20px; font-weight: bold;}
 </style>

<form>
 <c:if test="${customerMedicalInfo!=null}">
	<ul>	
		<li class="labeled-li">Setup Date: <fmt:formatDate value="${customerMedicalInfo.cpapPurchaseDate}" type="DATE" pattern="dd/MM/yyyy"/></li>
		<li class="labeled-li"><c:out value="${products[customerMedicalInfo.currentCpapMachine].name}" />&nbsp;&nbsp;[S/N:<c:out value="${customerMedicalInfo.currentCpapMachineSerial}" />]</li>
		<li class="labeled-li">Humidifier&nbsp;&nbsp;[S/N:<c:out value="${customerMedicalInfo.currentCpapMachineHumidifierSerial}" />]</li>	
		<li class="labeled-li"><c:out value="${products[customerMedicalInfo.currentMask].name}" /></li>
		<li class="labeled-li"><c:out value="${customerMedicalInfo.rampMins}" /> mins ramp from
			<c:out value="${customerMedicalInfo.ramp}" />cm H20 
		</li>
		<li class="labeled-li">EPR [<c:out value="${customerMedicalInfo.eprCFlexDesc}" />]</li>	
	</ul>
</c:if>
</form>