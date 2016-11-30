<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Funding Option</span></h1>
</div>
<div style="clear:both" />
<p>Reference data : Funding Option</p>
<form>
	<h2>Basic Details</h2>
	<ul>
		<li><label>Option:</label> <c:out value="${fundingOption.option}" /></li>
		<li><label>Funding Details Type:</label> <c:out value="${fundingOption.fundingDetailsType}" /></li>
		<li><label>ADP Funding Percentage:</label> <c:out value="${fundingOption.adpPercentage}" /></li>
		<li><label>In BenefitCode Dropdown:</label> <c:out value="${fundingOption.benefitCodeApplicable}" /></li>
		
		<li><label>Comment:</label></li>
	</ul>
	<br style="clear:both" />
	<textarea style="clear:both" cols="60" rows="5" disabled="disabled">
		<c:out value="${fundingOption.description}" />
		</textarea>
	<br /><br />
	<a href="/refdata/funding-option/form?id=<c:out value="${fundingOption.id}" />" class="bluebtn open-on-same-page">Edit</a>
</form>
<script>
$(".open-on-same-page").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	$('#mainbody').load(url);
	return false;
});
</script>