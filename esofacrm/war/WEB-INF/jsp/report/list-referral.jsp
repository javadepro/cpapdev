<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="report-list-referral">
	<div id="title">
		<h1><div id="icon-bubble" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Reports</span></h1>
	</div>
	<div style="clear:both" />
	<h2>Referral Report</h2>
	<br />
	<form:form id="referral-filter" commandName="referralSearch"
		action="/report/referral" method="POST">
		<b>Period:</b>&nbsp;<form:select path="numMths">
			<form:options items="${periodFilter}" />
		</form:select>&nbsp;
	<b>Referral Type:</b>&nbsp;<form:select path="referralType">
			<form:option value="Family Doctor" label="Family Doctor" />
			<form:option value="Sleep Doctor" label="Sleep Doctor" />
		</form:select>&nbsp;

		
		<input type="submit" value="Filter" id="refresh-btn" class="bluebtn" />
	</form:form>

	<br /><br />
	
<table class="listing">
<tr><th>Doctor</th><th style="width:200px;">Number of Referrals</th></tr>
<c:forEach var="referral" items="${referrals}" >
<tr>
	<td>
		<c:if test="${referralSearch.referralType eq 'Family Doctor'}">
			<c:out value="${familyDoctors[referral.referralKey].firstName}" /> <c:out value="${familyDoctors[referral.referralKey].lastName}" />
		</c:if>
		<c:if test="${referralSearch.referralType eq 'Sleep Doctor'}">
			<c:out value="${sleepDoctors[referral.referralKey].firstName}" /> <c:out value="${sleepDoctors[referral.referralKey].lastName}" />
		</c:if>		
	</td>
	<td><c:out value="${referral.count}"/></td>
</tr>
</c:forEach>
</table>
<br /> 
<div id="access-deneid" title="Access Denied">
	<p>You do not have access to view or edit this customer. Please ask reception/admin to add to the exception list</p>
</div>
<script>
	$("#access-deneid").dialog({ autoOpen: false,modal: true });
	jQuery.fn.log = function (msg) {
	  console.log("%s: %o", msg, this);
	  return this;
	};
function refreshList(){
	$("#refresh-btn").click();
} 
</script>
</div><!-- End div:id=alert-list -->
