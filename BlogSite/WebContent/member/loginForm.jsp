<%@page import="com.cos.util.MyUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 
	String myCookie = MyUtil.getMyCookie(request); 
	pageContext.setAttribute("myCookie", myCookie);
%>
<!DOCTYPE html>
<html> 
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">
  <title>WJ Blog</title>
  <!-- Bootstrap core CSS -->
  <link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet">
  <!-- Custom styles for this template -->
  <link href="<%=request.getContextPath()%>/css/blog-home.css" rel="stylesheet">
  <!-- Bootstrap core JavaScript -->
  <script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
  <script src="<%=request.getContextPath()%>/js/bootstrap.bundle.min.js"></script>
  <script src="<%=request.getContextPath()%>/js/validation.js"></script>
</head>
<body>
<!-- Navigation -->
<jsp:include page="/include/navigation.jsp"/>

<div class="container">
	<!-- Login Form -->
	<div class="row">
  <!-- Blog Entries Column -->
	  <div class="col-md-8 my-order">
		<div class="content-section">
			<form method="POST" action="<%=request.getContextPath()%>/member?cmd=member_login" onsubmit="return loginCheck(this)">
				<fieldset class="form-group">
					<legend class="border-bottom mb-4">Login</legend>
					<div class="form-group">
							<label class="form-control-label">username</label>
							<c:choose>
								<c:when test="${empty myCookie}">
									<input class="form-control form-control-lg" type="text" name="id" maxlength="20" required autofocus>
								</c:when>
								<c:otherwise>
									<input class="form-control form-control-lg" type="text" value="${myCookie}" name="id" maxlength="20" required autofocus>
								</c:otherwise>
							</c:choose>
					</div>
					<div class="form-group">
							<label class="form-control-label">password</label>
							<input class="form-control form-control-lg" type="password" name="password" maxlength="20" required>
					</div>
					<div class="border-top pt-3">
				  	<small class="text-muted">
					  	Remember Me? <input type="checkbox" name="idsave" value="on">
				  	</small>
					</div>
					<div class="form-group">
							<button class="btn btn-outline-info" type="submit" >Login</button>
					</div>		
				</fieldset>
			</form>
		</div>
		</div>

	<!-- SideBar -->
  <jsp:include page="/include/sidebar.jsp"/>
  </div>
  <!-- ./row -->
  </div>
<!-- ./container -->
</body>
</html>