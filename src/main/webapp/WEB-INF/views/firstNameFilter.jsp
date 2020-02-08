<%@ page session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <title>Username Filter</title>
</head>
<body>
<spring:url value="/users" var="searchFirstName"/>

<form:form method="get" action="${searchFirstName}">
    <input type="text" name="firstName"/>
    <input type="checkbox" name="matchExact"/>
    <input type="submit"/>
</form:form>
</body>
</html>
