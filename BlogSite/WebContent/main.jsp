<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
<jsp:include page="include/navigation.jsp"/>
  <!-- Page Content -->
  <div class="container">
    <div class="row">
      <!-- Blog Entries Column -->
      <div class="col-md-8">
      
      <c:forEach var="item" items="${list}">
         <!-- Blog Post -->
        <div class="card mb-4">
          <!--<img class="card-img-top" src="http://placehold.it/750x300" alt="Card image cap"> -->
          <div class="card-body main">
            <h2 class="card-title">${item.title}</h2>
            <p class="card-text">${item.content}</p>
            <a href="board?cmd=board_view&num=${item.num}" class="btn btn-primary">Read More &rarr;</a>
          </div>
          <div class="card-footer text-muted">
             Posted on ${item.writedate}
            <a href="#">${item.id}</a>
          </div>
        </div>
		  </c:forEach>

        <!-- Pagination -->
        <ul class="pagination justify-content-center mb-4">
          <li class="page-item">
            <a class="page-link" href="#">&larr; Older</a>
          </li>
          <li class="page-item disabled">
            <a class="page-link" href="#">Newer &rarr;</a>
          </li>
        </ul>
      </div>
      
      <!-- SideBar -->
      <jsp:include page="include/sidebar.jsp"/>
    </div>
    <!-- /.row -->
    
  </div>
  <!-- /.container -->
</body>
</html>