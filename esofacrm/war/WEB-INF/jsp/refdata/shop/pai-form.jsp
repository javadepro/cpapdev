<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="formwrapper">
	<div id="title">
		<h1>
			<div id="icon-tools" class="icon32"></div>
			<span style="float: left; margin-left: 5px; margin-top: 6px;">Primary Adp Number</span>
		</h1>
	</div>
	<div style="clear: both" />
	<p>Identifies an adp number to a store so that the address displayed in the ADP Forms are associated with the primary store.</p>

	<form:form id="pai-form" commandName="pai"
		action="/refdata/shop/pai-formsubmit" method="POST">
		<form:hidden path="id" />
			<c:if test="${message!=null}">
				<div class="messageblock">${message}</div>
			</c:if>
		<form:errors path="*" cssClass="errorblock" element="div" />
		
		<ul>
			<li><label>Shop name:</label>
			
				<form:select path="shopKey" cssErrorClass="form-error-field">
					<form:options items="${shops}" itemLabel="name" />
				</form:select></li>
			<li><label>Adp Number:</label> <form:input path="adpNumber" size="40"
					cssErrorClass="form-error-field" /></li>
				
				
			<li>
				<input type="submit" value="Save" class="bluebtn" />
			</li>
		</ul>
	</form:form>
</div>