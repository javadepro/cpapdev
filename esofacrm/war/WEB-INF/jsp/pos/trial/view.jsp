<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">POS</span>
	</h1>
</div>
<div style="clear: both" />
<h2>View Trial/Rental Pool</h2>
<p class="pageDesc">To lend out a machine, click the edit button, search for the customer, and set the store which the customer is lending from.</p>

<form>
	<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
	</c:if>
	<br/><br/>
	<ul>	
		<li><label>Product:</label><c:out value="${productMap[trialItem.product].name}" /></li>
		<li><label>Product Serial:</label> <c:out value="${trialItem.serialNumber}" /></li>
		<li><label>Location:</label><c:out value="${shops[trialItem.location].name }"/></li>
		<li><label>Item Status</label><c:out value="${trialItem.trialStatus}"/></li>
	<c:if test="${trialItem.customerKey != null }">
		<li><label>Customer :</label><c:out value="${trialItem.customerFullName}"/></li>
	</c:if>		
		<li><label>Last Updated :</label><c:out value="${trialItem.lastUpdatedDate}"/></li>		
	</ul>
</form>
	<br /><br />
	<a href="/pos/trial/edit?id=<c:out value="${trialItem.id}"/>" style="color:#FFF;" class="bluebtn">Edit</a>&nbsp;&nbsp;
	<a href="/pos/trial/delete-form?id=<c:out value="${trialItem.id}"/>" id="delete" style="color:#FFF;" class="bluebtn">Delete</a>&nbsp;&nbsp;
	</div>
	
	<script>
	$(function (){
		  $('#delete').click(function() {
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

