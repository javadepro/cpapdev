<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

 <style type="text/css">
      .labeled-li {height:20px;}
 </style>


 <form>
<ul>
	<li class="labeled-li"><span><c:out value="${customerWrapper.customer.firstname}" />&nbsp;
		<c:out value="${customerWrapper.customer.lastname}" /></span>
	</li>
	<li class="labeled-li"><span><c:out value="${customerWrapper.customerExtendedInfo.address.line1}" />&nbsp;</span></li>
	<li class="labeled-li"><span><c:out value="${customerWrapper.customerExtendedInfo.address.line2}" />&nbsp;</span></li>
	<li class="labeled-li"><span><c:out value="${customerWrapper.customerExtendedInfo.address.city}" />,&nbsp; 
		<c:out value="${customerWrapper.customerExtendedInfo.address.province}" /></span>
	</li>
	<li class="labeled-li"><span><c:out value="${customerWrapper.customerExtendedInfo.address.postalCode}" />&nbsp;</span></li>
	<li class="labeled-li"><span><c:out value="${customerWrapper.customerExtendedInfo.address.country}" />&nbsp;</span></li>
</ul>
<br />
</form>
