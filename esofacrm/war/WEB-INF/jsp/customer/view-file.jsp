<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div id="file-view">
<h2>Files&nbsp;&nbsp;</h2>
<p>You can use this page to manage customer files.</p>




<form:form id="upload-form" action="/customer/files/customerFileUpload?customerId=${customerId}" modelAttribute="uploadItem" method="post" enctype="multipart/form-data">
	<form:input path="fileData" type="file"/>
	<input type="submit" value="Upload a file" />
</form:form>
<br/>

<table class="listing">
<tr><%--<th>File id</th>--%><th style="width:600px;">Filename</th><th style="width:200px">Last modified</th><th>Delete</th></tr>
<c:forEach var="file" items="${files}" >
<tr>
<%--<td><c:out value="${file.id}" /></td>--%>
<td><a href="/customer/files/view-file?customerId=${customerId}&requestedFileId=${file.id}"><c:out value="${file.title}" escapeXml="false"/></a></td>
<td><c:out value="${file.modifiedDate}" /></td>
<td><a href="/customer/files/delete-file?customerId=${customerId}&requestedFileId=${file.id}" class="deleteFile">Delete</a></td>
</tr>
</c:forEach>
</table>

<script>
setupAjaxFormReplaceWithAttachment("upload-form","file-view");

$(".deleteFile").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	$('#file-view').load(url);
	return false;
});
</script>
</div>