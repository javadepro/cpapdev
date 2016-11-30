<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div id="title">
	<h1>
		<div id="icon-users" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Customer</span>
	</h1>
</div>
<div style="clear: both" />

<div id="form-basic" class="onepager-section">
	<%@include file="form-basic.jsp"%>
</div>
<div><br/>
	<a href="/customer/tabview?id=${customerWrapper.customer.id}" style="color:#FFF" class="bluebtn open-on-page" id='open-view-on-tab'>Switch to View</a>&nbsp;
	<br/><br/><br/><br/>
</div>
<div id="form-medical" class="onepager-section">
	<%@include file="form-medical.jsp"%>
</div>
<div id="form-insurance" class="onepager-section">
	<%@include file="form-insurance.jsp"%>
</div>
<div><br/><br/>
</div>
<script>

$("#open-view-on-tab").click(function(e) {
	e.preventDefault();
	var url = $(this).attr('href');
	var title = $(this).attr('title');
	$('#mainbody').load(url,function(response, status, xhr) {
		  if (status == "error") {
			  	if(xhr.status==403){
			  		//alert($("#access-deneid"));
			  		$("#access-deneid").dialog("open");
			  	}
			  }
			});
	return false;
});
</script>