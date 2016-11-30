<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page session="true" %>
	<c:if test="${sessionScope.companyMode == 'CPAPDIRECT' }">
		<c:set var="isCpapMode">true</c:set>
	</c:if>

<div id="accordion" style="width: 195px" class="navimenu">

	<h3>
		<a href="#">Customer</a>
	</h3>
	<div>
		<ul>
			<li><a href="/customer/search">Customer Search</a></li>
			<li><a href="/customer/onepager/new" class="ajax-anchor">Customer
					Add</a></li>
			<%--<li><a href="/customer/list">Customer List</a></li> --%>
		</ul>
	</div>
	<h3>
		<a href="#">Product</a>
	</h3>
	<div>
		<ul>
			<li><a href="/product/search">Product Search</a></li>
			
			<c:if test="${isCpapMode == 'true' }">			
			<li><a href="/product/inventory/transfer-view">View Transfers</a></li>
			</c:if>
			
			<sec:authorize access="hasRole('ROLE_SUPER') or hasRole('ROLE_INVENTORY_ADMIN')">
			<br/>
			<h4>Product Admin</h4>	
				<li><a href="/product/add" class="ajax-anchor">New Product</a></li>
			</sec:authorize>
		</ul>
	</div>
	<h3>
		<a href="#">Alert</a>
	</h3>
	<div>
		<ul>
			<li><a href="/alert/customer">Customer Alerts</a></li>
			
			<c:if test="${isCpapMode == 'true' }">
			<li><a href="/alert/product/list">Product Alerts</a></li>
			</c:if>
		</ul>
	</div>
	<h3>
		<a href="#">POS</a>
	</h3>
	<div>
		<ul>
			<li><a href="/pos/invoice-search">Invoice Search</a></li>
			<c:if test="${isCpapMode == 'true' }">
				<sec:authorize access="hasRole('ROLE_ADP_RECEIVER') or hasRole('ROLE_ADMIN')">
				<li><a href="/pos/invoice-adpbulk-form">Invoice ADP Search</a></li>
				</sec:authorize>
			<li><a href="/pos/invoice-form">Generic Invoice</a></li>
			</c:if>
		</ul>
		
		<h4>&nbsp;</h4>
		<ul>
			<li><a href="/pos/trial/list">Trial/Rental Search</a></li>
			<sec:authorize access="hasRole('ROLE_SUPER') or hasRole('ROLE_INVENTORY_ADMIN')  or hasRole('ROLE_POS_ADMIN')">
			<li><a href="/pos/trial/add">Add to Trial/Rental Pool</a></li>
			</sec:authorize>
		</ul>
	</div>
	<h3>
		<a href="#">Reference Data</a>
	</h3>
	<div>
		<h4>Doctors/Clinics</h4>	
		<ul>
			<li><a href="/refdata/sleep-clinic">Sleep Clinic</a></li>
			<li><a href="/refdata/sleep-doctor">Sleep Doctor</a></li>
			<li><a href="/refdata/family-doctor">Family Doctor</a></li>
			<c:if test="${isCpapMode !='true' }">
			<li><a href="/refdata/dental-clinic">Dental Clinic</a></li>
			<li><a href="/refdata/dentist">Dentist</a></li>
			
			</c:if>
		</ul>
		
		<h4>Common Data</h4>	
		<ul>
			
			<li><a href="/refdata/shop">${sessionScope.companyMode} Clinic</a></li>
			
			<c:if test="${isCpapMode == 'true' }">
			<li><a href="/refdata/shop/pai-list">Primary Adp Numbers</a></li>
			</c:if>
			
			<li><a href="/refdata/insurance-provider">Insurance Provider</a></li>
			
			<sec:authorize access="hasRole('ROLE_SUPER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_INVENTORY_ADMIN')">
			<li><a href="/refdata/manufacturer">Manufacturer</a></li>
			</sec:authorize>
			
			
			<sec:authorize access="hasRole('ROLE_SUPER') or hasRole('ROLE_ADMIN')">
			<c:if test="${isCpapMode == 'true' }">
			<li><a href="/refdata/funding-option">Funding Option</a></li>
			</c:if>			
			
			<li><a href="/refdata/appointment-preference">Appointment
					Preference</a></li>
			<li><a href="/refdata/contact-preference">Contact Preference</a></li>
			<li><a href="/refdata/cpap-diagnosis">CPAP Diagnosis</a></li>
			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_SUPER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_POS_ADMIN')">
			<li><a href="/refdata/discount-reason">Discount Reason</a></li>			
			</sec:authorize>
		</ul>
		
		
		<h4>Types</h4>	
		<ul>
			<sec:authorize access="hasRole('ROLE_INVENTORY_ADMIN') ">
			<li><a href="/refdata/product-type">Product Type</a></li>
			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_SUPER') ">
			<li><a href="/refdata/alert-type">Alert Type</a></li>
			<li><a href="/refdata/event-type">Note Type</a></li>
			</sec:authorize>
		</ul>
		
		<sec:authorize access="hasRole('ROLE_SUPER')">
		<h4>Config</h4>
		<ul>
			<li><a href="/user/role/list">Role Definition</a></li>
			<li><a href="/admin/config">Configuration</a></li>
			<li><a href="/admin/rule-engine">Rule Engine</a></li>
		</ul>
		</sec:authorize>
	</div>
	<sec:authorize access="hasRole('ROLE_REPORTS') or hasRole('ROLE_OWNER') or hasRole('ROLE_SUPER')  or hasRole('ROLE_INVENTORY_ADMIN')">
		<h3>
			<a href="#">Report</a>
		</h3>
		<div>
			<ul>
			
				<sec:authorize access="hasRole('ROLE_SUPER') or hasRole('ROLE_OWNER') or hasRole('ROLE_REPORTS')">
				
				<li><a href="/report/snapshot-select">Snapshot Reports</a></li>
				<li><a href="/report/range-select">Standard Reports</a></li>
				<li><a href="/report/referral">Referral Report</a></li>
				
				</sec:authorize>
				<h4>&nbsp;</h4>
				
				<c:if test="${isCpapMode == 'true' }">
				<sec:authorize access="hasRole('ROLE_INVENTORY_ADMIN') or hasRole('ROLE_REPORTS') or hasRole('ROLE_POS_MGR')">
				<li><a href="/report/inventoryCountReport">Inventory Count Report</a></li>
				</sec:authorize>
				</c:if>
							
			</ul>
		</div>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER') or hasRole('ROLE_USER') or hasRole('ROLE_POS_MGR')">
		<h3>
			<a href="#">System Admin</a>
		</h3>
		<div>
			<h4>User Admin</h4>
				<ul>
					<sec:authorize access="hasRole('ROLE_POS_MGR')">
					<c:if test="${isCpapMode == 'true' }">
					<li><a href="/user/passcode/form">POS Mgr Passcode</a></li>
					</c:if>						
					</sec:authorize>						
					<li><a href="/user/temp-access/list">Customer Profile Temp Access</a></li>
					<h4>&nbsp;</h4>
					
					<sec:authorize access="hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER')">
					<li><a href="/user/crmuser/list?getActiveOnly=true">User Management</a></li>
					<li><a href="/user/ip/list">IP Restriction Management</a></li>
					<li><a href="/audit/list">Audit Log</a></li>
					</sec:authorize>

				</ul>
			<sec:authorize access="hasRole('ROLE_SUPER')">
			<h4>Data Admin</h4>
			<ul>
				
			
				<li><a href="/admin/uploader/form">Import</a></li>
				<li><a href="/admin/export/form">Export</a></li>
				<li><a href="/admin/data-delete/form">Data Delete</a></li>
			</ul>
			</sec:authorize>
		</div>
	</sec:authorize>

</div>
<script>
	$(".ajax-anchor").click(function(e) {
		e.preventDefault();
		var url = $(this).attr('href');
		var title = $(this).attr('title');
		$('#mainbody').load(url);
		return false;
	});
	$(function() {
		$("#accordion").accordion({
			autoHeight : false,
			navigation : true
		});
	});
</script>
