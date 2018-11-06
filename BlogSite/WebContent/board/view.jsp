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
	<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet">
	<!-- Custom styles for this template -->
	<link href="<%=request.getContextPath()%>/css/blog-home.css" rel="stylesheet">
	 <!-- Bootstrap core JavaScript -->
  <script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
  <script src="<%=request.getContextPath()%>/js/bootstrap.bundle.min.js"></script>
	
</head>

<body>
	<!-- Navigation -->
	<jsp:include page="../include/navigation.jsp"/>

  <!-- Page Content -->
  <div class="container">
    <div class="row">

		<div class="col-md-12">
	  	<div class="card mb-4">
	          <!--<img class="card-img-top" src="http://placehold.it/750x300" alt="Card image cap"> -->
	          <!-- Post Content Column -->
	          <div class="card-body main">
	          	<!-- Title -->
	          	<h1 class="mt-4">${board.title}</h1>
	          	<!-- Author -->
        			<p class="lead">by<a href="#">${board.id}</a></p>
        			<hr>
        			<!-- Date/Time -->
        			<p>Posted on ${board.writedate}</p>
        			<hr>
        			<!-- Post Content -->
							<div id="viewContent">${board.content}</div>
        			<hr>
        			<!-- Comments Form -->
			        <div class="card my-4">
			          <h5 class="card-header">Leave a Comment:</h5>
			          <div class="card-body">
			            <form>
			              <div class="form-group">
			                <textarea class="form-control" rows="3"></textarea>
			              </div>
			              <button type="submit" class="btn btn-primary">Submit</button>
			            </form>
			          </div>
			        </div>
			
			        <!-- Single Comment -->
			        <div class="media mb-4">
			          <img class="d-flex mr-3 rounded-circle" src="http://placehold.it/50x50" alt="">
			          <div class="media-body">
			            <h5 class="mt-0">Commenter Name</h5>
			            Cras sit amet nibh libero, in gravida nulla. Nulla vel metus scelerisque ante sollicitudin. Cras purus odio, vestibulum in vulputate at, tempus viverra turpis. Fusce condimentum nunc ac nisi vulputate fringilla. Donec lacinia congue felis in faucibus.
			          </div>
			        </div>
			
			        <!-- Comment with nested comments -->
			        <div class="media mb-4">
			          <img class="d-flex mr-3 rounded-circle" src="http://placehold.it/50x50" alt="">
			          <div class="media-body">
			            <h5 class="mt-0">Commenter Name</h5>
			            Cras sit amet nibh libero, in gravida nulla. Nulla vel metus scelerisque ante sollicitudin. Cras purus odio, vestibulum in vulputate at, tempus viverra turpis. Fusce condimentum nunc ac nisi vulputate fringilla. Donec lacinia congue felis in faucibus.
			
			            <div class="media mt-4">
			              <img class="d-flex mr-3 rounded-circle" src="http://placehold.it/50x50" alt="">
			              <div class="media-body">
			                <h5 class="mt-0">Commenter Name</h5>
			                Cras sit amet nibh libero, in gravida nulla. Nulla vel metus scelerisque ante sollicitudin. Cras purus odio, vestibulum in vulputate at, tempus viverra turpis. Fusce condimentum nunc ac nisi vulputate fringilla. Donec lacinia congue felis in faucibus.
			              </div>
			            </div>
			
			            <div class="media mt-4">
			              <img class="d-flex mr-3 rounded-circle" src="http://placehold.it/50x50" alt="">
			              <div class="media-body">
			                <h5 class="mt-0">Commenter Name</h5>
			                Cras sit amet nibh libero, in gravida nulla. Nulla vel metus scelerisque ante sollicitudin. Cras purus odio, vestibulum in vulputate at, tempus viverra turpis. Fusce condimentum nunc ac nisi vulputate fringilla. Donec lacinia congue felis in faucibus.
			              </div>
			            </div>
			
			          </div>
			        </div>
	          </div>
	   		</div>
   		</div>	
    </div>
    <!-- /.row -->

  </div>
  <!-- /.container -->

</body>
</html>
