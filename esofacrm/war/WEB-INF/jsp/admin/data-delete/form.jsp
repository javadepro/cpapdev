<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
 

<form:form action="/admin/data-delete/formsubmit" modelAttribute="uploadItem" method="post" enctype="multipart/form-data">
            <fieldset>
                <legend>Data Delete</legend>
 
                <p>
                    <form:label for="name" path="className">Name</form:label><br/>
                    <form:select path="className">
                    	<form:options items="${types}" />
                    </form:select>
                </p>
 
 				
      
 
                <p>
                    <input type="submit" />
                </p>
 
            </fieldset>
        </form:form>