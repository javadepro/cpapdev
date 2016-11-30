<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<form:form id="payment-form" commandName="customerPaymentInfo"
	action="/customer/payment-form-save" method="POST">
	<form:hidden path="id" />
	<form:hidden path="customer" />

	<h2>Payment Info</h2>

	<p style="">Please try to obtain as much information about the
		customer as possible. Any additional contact information can go into
		the Contact Notes.</p>
	<form:errors path="*" cssClass="errorblock"></form:errors>
	<c:if test="${message!=null}">
		<div class="messageblock">${message}</div>
	</c:if>
	<ul>
		<li><label>Balance:</label><span>
		<c:out value="${customerPaymentInfo.balance }"/>
		<form:hidden
					path="balance" cssErrorClass="form-error-field" class="numeric" /></span></li>
	</ul>
	<br />
	<h2>Credit Card Information</h2>
	<ul>
		<li><label>Holders Name:</label><span><form:input
					path="holdersName" />&nbsp;</span></li>
		<li><label>Credit Card Number:</label><span><form:input
					path="creditCardNumber" title="" /></span></li>
		<li><label>Exp Date:</label><span><form:input
					path="expMonth" size="2" placeholder="MM"/>&nbsp;/&nbsp;<form:input
					path="expYear" size="2" placeholder="YY" /></span></li>
		<li><label>Security Number:</label><span><form:input
					path="securityNumber" /></span></li>
		<li><label>Credit Card Type:</label><span><form:select
					path="cardType">
					<form:options items="${cardType}" />
					</form:select>
					</span></li>

	</ul>

<br /><br />
	<input type="submit" value="Save Payment Info" class="bluebtn" />
	<script>
		$('form').bind(
				'submit',
				function(e) {
					$(this).find('input:submit').attr('disabled', 'disabled')
							.attr('class', 'orangebtn').val("Working...");
				});

		$('input[title],textarea[title]').inputHints();
		$('.timepicker').timepicker();
		setupAjaxForm("payment-form");
		$(document).ready(function(){
		    $(".numeric").numeric();
		});
	</script>
</form:form>