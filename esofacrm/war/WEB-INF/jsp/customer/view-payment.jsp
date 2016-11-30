<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<form>
<br />
<h2>Payment Details</h2>
<ul>
	<li><label>Balance:</label><span><c:out
				value="${customerPaymentInfo.balance}" /></span></li>
</ul>

<br />

<br />
<h2>Credit Card Details</h2>
<ul>
	<li><label>Holders Name:</label><span><c:out
				value="${customerPaymentInfo.holdersName}" />&nbsp;</span></li>
	<li><label>Credit Card Number:</label><span><c:out
				value="${customerPaymentInfo.creditCardNumber}" />&nbsp;</span></li>
	<li><label>Exp Date:</label><span><c:out
				value="${customerPaymentInfo.expMonth}" />/<c:out
				value="${customerPaymentInfo.expYear}" /></span></li>
	<li><label>Security Number:</label><span><c:out
				value="${customerPaymentInfo.securityNumber}" />&nbsp;</span></li>
	<li><label>Credit Card Type:</label><span><c:out
				value="${customerPaymentInfo.cardType}" />&nbsp;</span></li>
</ul>
<br />
</form>
