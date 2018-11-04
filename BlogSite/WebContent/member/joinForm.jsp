<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">
  <title>WJ Blog</title>
  <!-- Bootstrap core CSS -->
  <link href="../css/bootstrap.css" rel="stylesheet">
  <!-- Custom styles for this template -->
  <link href="../css/blog-home.css" rel="stylesheet">
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
			<form method="POST" action="/BlogSite/member?cmd=joinAction" onsubmit="return joinCheck(this)">
				<fieldset class="form-group">
					<legend class="border-bottom mb-4">Join</legend>
					<div class="form-group">
							<label class="form-control-label">ID</label>
							<input class="form-control form-control-lg" type="text" name="id" maxlength="20" required>
					</div>
					<div class="form-group">
							<label class="form-control-label">Password</label>
							<input class="form-control form-control-lg" type="password" name="password" maxlength="20" required>
					</div>
					<div class="form-group">
							<label class="form-control-label">Confirm_password</label>
							<input class="form-control form-control-lg" type="password" name="confirm" maxlength="20" required>
					</div>
					<div class="form-group">
							<label class="form-control-label">Username</label>
							<input class="form-control form-control-lg" type="text" name="username" maxlength="20" required>
					</div>
					<div class="form-group">
							<label class="form-control-label">Email</label>
							<input class="form-control form-control-lg" type="email" name="email" maxlength="20" required>
					</div>
					<div class="border-top pt-3">
				  	<small class="text-muted">
					  	Already Have An Account? <a class="ml-2" href="/BlogSite/member?cmd=loginForm">login</a>
				  	</small>
			  	</div>
					<div class="form-group">
							<button class="btn btn-outline-info" type="submit">Sign in</button>
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
	<!-- Bootstrap core JavaScript -->
  <script src="../js/jquery.min.js"></script>
  <script src="../js/bootstrap.bundle.min.js"></script>
  <script src="../js/validation.js"></script>  
</body>
</html>