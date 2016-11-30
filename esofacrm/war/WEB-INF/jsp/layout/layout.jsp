<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<title>	<c:if test="${sessionScope.companyMode == 'CPAPDIRECT' }">CPAP Direct</c:if><c:if test="${sessionScope.companyMode != 'CPAPDIRECT' }">SleepMed</c:if>
 - <tiles:insertAttribute name="title"
		ignore="true" /></title>
<tiles:insertAttribute name="head" />
</head>
<body>
	
	<div id="header"
		style="background-color: #666; color: #DDD; display: block; width: 100%; height: 30px; padding: 0px">
		<tiles:insertAttribute name="header" />
	</div>
	<div style="clear: both"></div>
	<div id="bodyWrapper">
		<div style="display: inline-block; vertical-align: top;">
			<tiles:insertAttribute name="navi" />
		</div>
		<div id="content" style="display: inline-block; padding: 10px 20px;">
			<div id="mainbody">
				<tiles:insertAttribute name="body" />
			</div><!-- End Mainbody -->
		</div>
	</div>
	<br style="clear:both"/>
	<div id="footer">
		<tiles:insertAttribute name="footer" />
	</div>
</body>
</html>