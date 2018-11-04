<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">
  <title>Cos Blog</title>
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
			<form method="POST" action="#">
				<fieldset class="form-group">
					<legend class="border-bottom mb-4">New Post</legend>
					<div class="form-group">
							<label class="form-control-label">Title</label>
							<input class="form-control form-control-lg" type="text">
					</div>
					<div class="form-group">
							<label class="form-control-label">Content</label>
							<textarea class="form-control" rows="10" cols="50" style="background-image:url('/Blog/img/background.png');"></textarea>
					</div>
					<div class="form-group">
							<button class="btn btn-outline-info" type="submit" >Update</button>
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
</body>
</html>