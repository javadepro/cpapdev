<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<form:form id="reportForm" commandName="reportForm" action="/report/snapshot-formsubmit" method="POST" >

<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Snapshot Report</span></h1>
</div>
<div style="clear:both" />

<h2>Automated Reports</h2>
<p class="pageDesc">These reports are pre-generated monthly</p>
<form:errors path="*" cssClass="errorblock"/>
<ul>
<li><label>Report Name:</label><form:select
							path="reportId">
							<form:options items="${reportsAvailable}" itemLabel="reportId" itemValue="reportId" />
						</form:select></li>				
</ul>
<br />
<input type="submit" value="Fetch Report" class="bluebtn" formtarget="_blank" />

</form:form>
<br/><br/><br/><br/><br/>
<form:form id="dataDumpForm" commandName="dataDumpForm" action="/report/datadumpview-formsubmit" method="POST" >

<div style="clear:both" />

<h2>Adhoc Requested Reports</h2>
<p class="pageDesc">These reports are data dumps generated upon request. </p>
<form:errors path="*" cssClass="errorblock"/>
<ul>
<li><label>Download Report:</label><form:select
							path="requestedFileId">
							<form:options items="${dataDumpFiles}" itemLabel="title" itemValue="id" />
						</form:select></li>				
</ul>
<br />
<input type="submit" value="Fetch Report" class="bluebtn" formtarget="_blank" />

</form:form>
<br/><br/><br/><br/>
<p class="pageDesc">These are the reports available for adhoc generation.  Click on the link to kick off the report
generation.  It will take while depending on the type of report.  When the report is ready, it will be available in the above drop down list.</p>

<ul>
	<li><label><a href="/report/generateDataDump?reportId=customerReportTask">Customer Report</a></label></li>
</ul>
<script>

$(".datepicker").datepicker({
		dateFormat : "dd/mm/yy",
		changeMonth: true,
		changeYear: true,
		yearRange: "-1:+0",
		defaultDate: new Date()
});
</script>

