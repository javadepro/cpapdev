<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page session="true" %>
	<c:if test="${sessionScope.companyMode == 'CPAPDIRECT' }">
		<c:set var="isCpapMode">true</c:set>
	</c:if>
	
<div id="tab-view">
	<div id="tabs">
		<ul id="tab-links">
			<li><a href="#tabs-1">General Information</a></li>
			<li><a href="/customer/tabview/view-insurance?id=${customerWrapper.customer.id}">Insurance
					Information</a></li>
			<li><a href="/customer/tabview/view-medical?id=${customerWrapper.customer.id}">Medical Information
			<c:if test="${hasSpecialMedicalNote==true}">&nbsp;<span class="blinking-star">*</span></c:if>
			</a></li>
			<c:if test="${isCpapMode =='true'}">		
			<li><a href="/customer/tabview/view-cpap?customerId=${customerWrapper.customer.id}">Cpap Information</a></li>
			</c:if>
			<!-- <li><a href="/customer/tabview/view-payment?customerId=${customerWrapper.customer.id}">Payment Information</a></li> -->
			<li><a href="/customer/tabview/view-alert?customerId=${customerWrapper.customer.id}">Alerts</a></li>
			<li><a href="/customer/tabview/view-event?customerId=${customerWrapper.customer.id}">Note/Client History</a></li>
			<li><a href="/customer/tabview/view-file?customerId=${customerWrapper.customer.id}">Files</a></li>
		</ul>
		<div id="tab-patient-info">${customerWrapper.customer.firstname} ${customerWrapper.customer.lastname} | ${customerWrapper.customerExtendedInfo.formattedDateOfBirth} | ${customerWrapper.customer.formattedHealthCardNumber} | ${customerWrapper.customer.phoneHome} </div>
		<div id="tabs-1">
			<%@include file="view-basic.jsp"%>
		</div>
	</div>
</div>
<br />
<div id="button-links">
<a href="/customer/tabview?id=${customerWrapper.customer.id}" style="color:#FFF" class="bluebtn open-on-page">Refresh</a>&nbsp;&nbsp;
<a href="/customer/tabedit?id=${customerWrapper.customer.id}" style="color:#FFF" class="bluebtn open-on-page">Edit</a>&nbsp;&nbsp;

<!-- <a href="/customer/onepageredit?id=${customerWrapper.customer.id}" style="color:#FFF" class="bluebtn open-on-page">Onepager Edit</a>&nbsp;&nbsp; -->
<%-- <a href="/customer/onepagerview?id=${customerWrapper.customer.id}" style="color:#FFF" class="bluebtn open-on-page">Onepager View (For Printing)</a>&nbsp;&nbsp;  --%>
<a href="#" onClick="window.print();return false" style="color:#FFF;" class="bluebtn">&nbsp;Print&nbsp;</a>&nbsp;&nbsp;

<a href="/pos/invoice-form?customerId=<c:out value="${customerWrapper.customer.id}" />"  class="bluebtn" style="color:#FFF;">POS</a>&nbsp;&nbsp;
<c:if test="${isCpapMode == 'true' }">
<a href="/pos/invoice-form?invoiceType=TRIAL&customerId=<c:out value="${customerWrapper.customer.id}" />"  class="bluebtn" style="color:#FFF;">Trial POS</a>&nbsp;&nbsp;

<a href="/customer/label/addressInfo?customerId=<c:out value="${customerWrapper.customer.id}" />" id="addrModal" title="Address Label" class="bluebtn" style="color:#FFF;">Address Label</a>&nbsp;&nbsp;
<a href="/customer/label/patientInfo?customerId=<c:out value="${customerWrapper.customer.id}" />" id="generalModal" title="General Info Label" class="bluebtn" style="color:#FFF;">General Info Label</a>&nbsp;&nbsp;
<a href="/customer/label/cpapInfo?customerId=<c:out value="${customerWrapper.customer.id}" />" id="cpapModal" title="Cpap Info Label" class="bluebtn" style="color:#FFF;">Cpap Info Label</a>&nbsp;&nbsp;

<a href="/customer/subMail?customerId=<c:out value="${customerWrapper.customer.id}" />"  id="mailerModal" class="bluebtn" style="color:#FFF;">Sub Newsletters</a>&nbsp;&nbsp;
<a href="/customer/unsubMail?customerId=<c:out value="${customerWrapper.customer.id}" />" id="mailerModal" class="bluebtn" style="color:#FFF;">UnSub Newsletters</a>&nbsp;&nbsp;
</c:if>
</div>
<br/><br/>
<c:choose>
	<c:when test="${isCpapMode =='true'}">
		<div id="button-links">
		<a href="/customer/generatePdf?pdfName=AdpForm&customerId=<c:out value="${customerWrapper.customer.id}"/>" class="bluebtn" style="color:#FFF;">ADP Form</a>&nbsp;&nbsp;
		<a href="/customer/generatePdf?pdfName=PatientProgress&customerId=<c:out value="${customerWrapper.customer.id}"/>" class="bluebtn" style="color:#FFF;">Patient Progress</a>&nbsp;&nbsp;
		<a href="/customer/generatePdf?pdfName=TrialAgreement&customerId=<c:out value="${customerWrapper.customer.id}"/>" class="bluebtn" style="color:#FFF;">Trial Agreement</a>&nbsp;&nbsp;
		<a href="/customer/generatePdf?pdfName=SetupNotificationCpap&customerId=<c:out value="${customerWrapper.customer.id}"/>" class="bluebtn" style="color:#FFF;">CPAP Setup Notification</a>&nbsp;&nbsp;
		<a href="/customer/generatePdf?pdfName=SetupNotificationApap&customerId=<c:out value="${customerWrapper.customer.id}"/>" class="bluebtn" style="color:#FFF;">AutoPAP Setup Notification</a>&nbsp;&nbsp;
		<a href="/customer/generatePdf?pdfName=SetupNotificationBipap&customerId=<c:out value="${customerWrapper.customer.id}"/>" class="bluebtn" style="color:#FFF;">BiPAP Setup Notification</a>&nbsp;&nbsp;
		<a href="/customer/generatePdf?pdfName=SetupCheckList&customerId=<c:out value="${customerWrapper.customer.id}"/>" class="bluebtn" style="color:#FFF;">Setup Checklist</a>&nbsp;&nbsp;
		</div>
	</c:when>
	<c:otherwise>
		<div id="button-links">
		<a href="/customer/generatePdf?pdfName=SMedClinicialChklist&customerId=<c:out value="${customerWrapper.customer.id}"/>" class="bluebtn" style="color:#FFF;">Clinicial Checklist</a>&nbsp;&nbsp;
		<a href="/customer/generatePdf?pdfName=SMedHstAgreement&customerId=<c:out value="${customerWrapper.customer.id}"/>" class="bluebtn" style="color:#FFF;">HST Agreement</a>&nbsp;&nbsp;
		<a href="/customer/generatePdf?pdfName=SMedHstReturnChklst&customerId=<c:out value="${customerWrapper.customer.id}"/>" class="bluebtn" style="color:#FFF;">Hst Return Checklist</a>&nbsp;&nbsp;
		<a href="/customer/generatePdf?pdfName=SMInDepth_SleepAssess&customerId=<c:out value="${customerWrapper.customer.id}"/>" class="bluebtn" style="color:#FFF;">In Depth Assessment</a>&nbsp;&nbsp;	
		</div>
	</c:otherwise>
</c:choose>


<script>
$("#tabs").tabs();
(function( $ ){
	  $.fn.flashing = function() {
		  return this.each(function(){
		      var $this = $(this);
		      setInterval(function(){
					$this.fadeOut(function(){
						$this.fadeIn();
					});
				},1000);
		  });
		 
	  };
	})( jQuery );
$(".blinking-star").flashing();
$(".open-on-page").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	var title = $(this).attr('title');
	$('#mainbody').load(url);
	return false;
});

$(function (){
    $('#addrModal,#generalModal,#cpapModal,#mailerModal,a.modal').click(function() {
        var url = this.href;
        var title = this.title;
        // show a spinner or something via css
        var dialog = $('<div class="loading">&nbsp;</div>').appendTo('body');
        // open the dialog
        dialog.dialog({
            // add a close listener to prevent adding multiple divs to the document
            close: function(event, ui) {
                // remove div with all data and events
                dialog.log("close");
                refreshList();
                dialog.remove();
            },
            modal: true,
            //position: center,
            title: title,
            width: "auto",
            height: "auto",
        });
        // load remote content
        dialog.load(
            url, 
         	 // omit this param object to issue a GET request instead a POST request, otherwise you may provide post parameters within the object
            function (responseText, textStatus, XMLHttpRequest) {
                // remove the loading class
                dialog.removeClass('loading');
            }
        )
       
        //prevent the browser to follow the link
        return false;
    });
});
</script>