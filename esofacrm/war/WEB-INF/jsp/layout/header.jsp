<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page session="true" %>

<div style="float:left; padding: 5px;text-shadow: 1px 1px #999;"><b style="font-size: 20px">
	<a href="/" style="text-decoration:none;color: #DDD;padding: 5px;text-shadow: 1px 1px #999;">${sessionScope.companyMode} CRM</a></b>
</div>
<div style="float:right;padding-top:9px;padding-right:5px;">
Welcome Back <b>
 <sec:authorize access="isAuthenticated()">  
<sec:authentication property="principal" />(<sec:authentication property="initial" />)
</sec:authorize></b>

<sec:authorize access="hasRole('ROLE_USER')">
-USER
</sec:authorize>
<sec:authorize access="hasRole('ROLE_CLINICIAN')">
-CLINICIAN
</sec:authorize>
<sec:authorize access="hasRole('ROLE_ADMIN')">
-ADMIN
</sec:authorize>
<sec:authorize access="hasRole('ROLE_POS_ADMIN')">
-POS_ADMIN
</sec:authorize>
<sec:authorize access="hasRole('ROLE_POS_CD_ADMIN')">
-POS_CD_ADMIN
</sec:authorize>
<sec:authorize access="hasRole('ROLE_POS_MGR')">
-POS_MGR
</sec:authorize>
<sec:authorize access="hasRole('ROLE_REPORTS')">
-REPORTS
</sec:authorize>
<sec:authorize access="hasRole('ROLE_ADP_RECEIVER')">
-ADP_RECEIVER
</sec:authorize>
<sec:authorize access="hasRole('ROLE_INVENTORY_ADMIN')">
-INVENTORY_ADMIN
</sec:authorize>
&nbsp;&nbsp;&nbsp;
<sec:authorize access="isAuthenticated()"> 
<a href="/bye" style="color:#ccc; text-decoration:underline; font-style:italic">Logout</a>
</sec:authorize>
</div>