<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div id="event-view">
<h2>Contact Alerts&nbsp;&nbsp;<a href="#" class="reload-event-page"><img src="/images/reload_icon.png" /></a></h2>
<p>You are required to meet with these clients</p>
<br><br>Remind your clients that you guys have an appointment</p>

<table class="listing">
<tr><th>&nbsp;</th><th>id</th><th>Date</th><th>Type</th><th>Customer</th><th>Action</th></tr>
<tr>
	<td><input type="checkbox" /></td>
	<td>1</td>
	<td>25/07/2012</td>
	<td>Contact - Part Replacement Reminder</td>
	<td>Mary Yan</td>
	<td><a href="">Dismiss</a>&nbsp;|&nbsp;<a href="">Reschedule</a></td>
</tr>
<tr>
	<td><input type="checkbox" /></td>
	<td>2</td>
	<td>26/07/2012</td>
	<td>Contact - Part Replacement Reminder</td>
	<td>John Thomas</td>
	<td><a href="">Dismiss</a>&nbsp;|&nbsp;<a href="">Reschedule</a></td>
</tr>
<tr>
	<td><input type="checkbox" /></td>
	<td>3</td>
	<td>26/07/2012</td>
	<td>Contact - Part Replacement Reminder</td>
	<td>Ray Chu</td>
	<td><a href="">Dismiss</a>&nbsp;|&nbsp;<a href="">Reschedule</a></td>
</tr>
<tr>
	<td><input type="checkbox" /></td>
	<td>4</td>
	<td>26/07/2012</td>
	<td>Contact - Part Replacement Reminder</td>
	<td>Leo King</td>
	<td><a href="">Dismiss</a>&nbsp;|&nbsp;<a href="">Reschedule</a></td>
</tr>
</table>
<br />
<input type="button" value="Dismiss"  class="bluebtn"/>&nbsp;<input type="button" value="email reminder"  class="bluebtn"/>&nbsp;<input type="button" value="print reminder" class="bluebtn"/>&nbsp;<input type="button" value="use default option"  class="bluebtn"/>



