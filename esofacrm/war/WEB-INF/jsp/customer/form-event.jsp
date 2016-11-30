<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="eventformwrapper" style="display:hidden">
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Customer</span></h1>
</div>
<div style="clear:both" />
<p>
Select the type that best describes the note that you wish to take.   <br><br>Certain note types like appointment notes will automatically create future dated alerts for this customer.
</p>
<form:form id="event-form" commandName="event"
	action="/customer/event/formsubmit" method="POST">
	<form:hidden path="customer" />
	<form:hidden path="id" />
	<form:errors path="*" cssClass="errorblock" element="div" />
	<ul>
		<li><label>Note/Client Activity History Type:</label>
		<form:select path="eventSubType">
				<c:forEach var="eventType" items="${eventTypeMap}" varStatus="x">
					<optgroup label="${eventType.key.type}">
						<form:options items="${eventType.value}" itemLabel="type" /> 
					</optgroup>
				</c:forEach>
			</form:select>
		</li>
		<li><label>Note Date:</label><form:input
					path="date" cssClass="datepicker"/>&nbsp;</li>
		<li><label>Details:</label><span><form:textarea 
					path="details" cols="40" rows="3"/>&nbsp;</span></li>
		

	</ul>
	<br /><br />
	<input type="submit" value="Save Note" class="bluebtn" />

</form:form>
<script>
$(".datepicker").datepicker({
	dateFormat : "dd/mm/yy",
	changeMonth : true,
	changeYear : true,
	yearRange: "-1:+2"
});
setupAjaxFormReplace("event-form","eventformwrapper");
</script>
</div>