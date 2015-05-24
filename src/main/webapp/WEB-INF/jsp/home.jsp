<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
	<head>
		<title>Hello Facebook</title>
	</head>
	<body>
		<h3>Hello, <c:out value="${facebookProfile.name}" /></h3>
		
		<h4>Here is your home feed:</h4>
		
	      <c:forEach items="${feed}" var="post">
	      	<div>
	          <b><c:out value="${post.from.name}" /></b>
	          <b><c:out value="${post.message}" /></b>
	          <hr/>
	        </div>
	      </c:forEach>
	</body>
</html>