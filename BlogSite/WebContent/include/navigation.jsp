<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Navigation -->
<nav class="navbar navbar-expand-lg navbar-dark bg-steel fixed-top">
  <div class="container">
    <a class="navbar-brand" href="<%=request.getContextPath()%>/board?cmd=board_list">WJ Blog</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarResponsive">
      <div class="navbar-nav">
          <a class="nav-item active nav-link mr-auto" href="<%=request.getContextPath()%>/board?cmd=board_list">Home
            <span class="sr-only">(current)</span>
          </a>
          <a class="nav-item nav-link" href="#">About</a>
          <a class="nav-item nav-link" href="#" data-toggle="modal" data-target="#myModal">Contact</a>
      </div>
      <div class="navbar-nav ml-auto"> 	  
          <a class="nav-item nav-link" href="<%=request.getContextPath()%>/board/writeForm.jsp">New Post</a>
          <c:choose>
          <c:when test="${empty sessionScope.id}">
          	<a class="nav-item nav-link" href="<%=request.getContextPath()%>/member/loginForm.jsp">Login</a>
          	<a class="nav-item nav-link" href="<%=request.getContextPath()%>/member/joinForm.jsp">Register</a>
          </c:when>
          <c:otherwise>
          	<a class="nav-item nav-link" href="<%=request.getContextPath()%>/member?cmd=member_update">Account</a>
          	<a class="nav-item nav-link" href="<%=request.getContextPath()%>/member?cmd=member_logout">Logout</a>
          </c:otherwise>
          </c:choose>
      </div>
    </div>
  </div>
</nav>

<!-- The Modal -->
  <div class="modal fade" id="myModal">
    <div class="modal-dialog">
      <div class="modal-content">
      
      <form method="POST" action="#">
        <!-- Modal Header -->
        <div class="modal-header">
          <h4 class="modal-title">Send SMS</h4>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>
        
        <!-- Modal body -->
        <div class="modal-body">
          Put the Message
    
        
        <div class="form-group">
						<textarea class="form-control" autofocus></textarea>
				</div>
				</div>
        
        <!-- Modal footer -->
        <div class="modal-footer">
        	<button type="submit" class="btn btn-info" data-dismiss="modal">Send</button>
          <button type="button" class="btn btn-info" data-dismiss="modal">Close</button>
        </div>
        </form>
      </div>
    </div>
  </div>