<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<form:form id="reportForm" commandName="reportForm" action="/report/range-formsubmit" method="POST" >

<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Generate Report</span></h1>
</div>
<div style="clear:both" />
<p class="pageDesc">Pick the report type you wish to generate, and then a date range for which you wish to query upon.  
The max date range is 2 months</p>
<h2>Report Criteria</h2>
<form:errors path="*" cssClass="errorblock"/>
<ul>
<li><label>Report Name:</label><form:select
							path="reportName">
							<form:options items="${reportTypes}" />
						</form:select></li>
<li><label>From Date:</label><span><form:input
			path="fromDate" class="datepicker" placeholer="dd/MM/YYYY" /></span></li>
<li><label>End Date:</label><span><form:input
			path="endDate" class="datepicker" placeholer="dd/MM/YYYY" /></span></li>					
</ul>
<br />
<input type="submit" value="Generate Report" class="bluebtn" formtarget="_blank" />

</form:form>

<script>

$(".datepicker").datepicker({
		dateFormat : "dd/mm/yy",
		changeMonth: true,
		changeYear: true,
		yearRange: "-1:+0",
		defaultDate: new Date()
});
</script>

