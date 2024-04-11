<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Display JSON Data</title>
</head>
<body>
    <h1>JSON Data Display</h1>
    
    <c:set var="jsonData" value='{"names":["John","Alice","Bob"],"ages":[30,25,28],"cities":["New York","Los Angeles","Chicago"]}'/>
    <%-- Replace the above line with your JSON data. --%>
    
    <table border="1">
        <tr>
            <th>Name</th>
            <th>Age</th>
            <th>City</th>
        </tr>
        
        <c:forEach items="${jsonData.names}" var="name" varStatus="nameIndex">
            <tr>
                <td>${name}</td>
                <td>${jsonData.ages[nameIndex.index]}</td>
                <td>${jsonData.cities[nameIndex.index]}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>