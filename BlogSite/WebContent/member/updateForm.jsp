<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">
  <title>Cos Blog</title>
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
<jsp:include page="../include/navigation.jsp"/>

<div class="container">
	<!-- Login Form -->
	<div class="row">
  <!-- Blog Entries Column -->
	  <div class="col-md-8 my-order">
		<div class="content-section">
			<form method="POST" action="<%=request.getContextPath()%>/member?cmd=member_updateProc" onsubmit="return accountCheck(this)">
				<fieldset class="form-group">
					<legend class="border-bottom mb-4">Update</legend>
					<div class="form-group">
							<label class="form-control-label">ID</label>
							<input class="form-control form-control-lg" type="text" name="id" value="${member.id}" maxlength="20" required readonly>
					</div>
					<div class="form-group">
							<label class="form-control-label">Password</label>
							<input class="form-control form-control-lg" type="password" name="password" value="${member.password}" maxlength="20" required autofocus>
					</div>
					<div class="form-group">
							<label class="form-control-label">Confirm_password</label>
							<input class="form-control form-control-lg" type="password" name="confirm" value="${member.password}" maxlength="20" required>
					</div>
					<div class="form-group">
							<label class="form-control-label">Username</label>
							<input class="form-control form-control-lg" type="text" name="username" value="${member.username}" maxlength="20" required readonly>
					</div>
					<div class="form-group">
							<label class="form-control-label">Email</label>
							<input class="form-control form-control-lg" type="email" name="email" value="${member.email}" maxlength="20" required>
					</div>
					<div class="form-group">
							<button class="btn btn-outline-info" type="submit">Update</button>
					</div>		
				</fieldset>
			</form>
		</div>
		</div>

	<!-- SideBar -->
  <jsp:include page="../include/sidebar.jsp"/>
  </div>
  <!-- ./row -->
  </div>
<!-- ./container -->
</body>
</html>