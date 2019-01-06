<%@page import="com.cos.dao.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${empty list}">
	<%
		RequestDispatcher dis = request.getRequestDispatcher("board?cmd=board_list");
			dis.forward(request, response);
	%>
</c:if>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">
<title>WJ Blog</title>
<!-- Bootstrap core CSS -->
<link href="<%=request.getContextPath()%>/css/bootstrap.css"
	rel="stylesheet">
<!-- Custom styles for this template -->
<link href="<%=request.getContextPath()%>/css/blog-home.css"
	rel="stylesheet">
<!-- Bootstrap core JavaScript -->
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/bootstrap.bundle.min.js"></script>
<script src="<%=request.getContextPath()%>/js/validation.js"></script>
</head>
<body>
	<!-- Navigation -->
	<jsp:include page="/include/navigation.jsp" />
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
							<a
								href="<%=request.getContextPath()%>/board?cmd=board_view&num=${item.num}"
								class="btn btn-primary">Read More &rarr;</a>
						</div>
						<div class="card-footer text-muted">
							Posted on ${item.writedate} <a href="#">${item.id}</a>
						</div>
					</div>
				</c:forEach>



				<!--  Paging -->
				<%
					int pageNum = 0;
					if (request.getAttribute("pageNum") != null) {
						pageNum = (Integer) request.getAttribute("pageNum");
					}

					BoardDAO dao = new BoardDAO();
					int result = dao.nextPage(pageNum + 3);
					pageContext.setAttribute("result", result);
				%>
				<c:choose>
					<c:when test="${pageNum == 0}">
						<ul class="pagination justify-content-center mb-4">
							<li class="page-item disabled"><a class="page-link"
								href="<%=request.getContextPath()%>/board?cmd=board_list&pageNum=${pageNum-3}">&larr;
									Prev</a></li>
							<li class="page-item"><a class="page-link"
								href="<%=request.getContextPath()%>/board?cmd=board_list&pageNum=${pageNum+3}">Next
									&rarr;</a></li>
						</ul>
					</c:when>
					<c:when test="${result == -1}">
						<ul class="pagination justify-content-center mb-4">
							<li class="page-item"><a class="page-link"
								href="<%=request.getContextPath()%>/board?cmd=board_list&pageNum=${pageNum-3}">&larr;
									Prev</a></li>
							<li class="page-item disabled"><a class="page-link"
								href="<%=request.getContextPath()%>/board?cmd=board_list&pageNum=${pageNum+3}">Next
									&rarr;</a></li>
						</ul>
					</c:when>
					<c:when test="${!empty search}">
						<ul class="pagination justify-content-center mb-4">
							<li class="page-item disabled"><a class="page-link" href="#">&larr;
									Prev</a></li>
							<li class="page-item disabled"><a class="page-link" href="#">Next
									&rarr;</a></li>
						</ul>
					</c:when>
					<c:otherwise>
						<ul class="pagination justify-content-center mb-4">
							<li class="page-item"><a class="page-link"
								href="<%=request.getContextPath()%>/board?cmd=board_list&pageNum=${pageNum-3}">&larr;
									Prev</a></li>
							<li class="page-item"><a class="page-link"
								href="<%=request.getContextPath()%>/board?cmd=board_list&pageNum=${pageNum+3}">Next
									&rarr;</a></li>
						</ul>
					</c:otherwise>
				</c:choose>
			</div>

			<!-- SideBar -->
			<jsp:include page="/include/sidebar.jsp" />
		</div>
		<!-- /.row -->

	</div>
	<!-- /.container -->

	<!-- 웹소켓 통신 -->
	<script>
		var list = document.querySelectorAll('.list-group-item');
		var hotTitle = document.querySelector('#hotTitle');
		//var textarea = document.getElementById("messageWindow");
		var webSocket = new WebSocket('ws://localhost:8080/BlogSite/broadcasting');
		//var inputMessage = document.getElementById('inputMessage');
		webSocket.onerror = function(event) {
			onError(event)
		};
		webSocket.onopen = function(event) {
			onOpen(event)
		};
		webSocket.onmessage = function(event) {
			onMessage(event)
		};
		function onMessage(event) {
			var data = JSON.parse(event.data);
			hotTitle.append('.');
			list[0].textContent = '조회' + data[0].readcount + ' '
					+ data[0].title;
			list[1].textContent = '조회' + data[1].readcount + ' '
					+ data[1].title;
			list[2].textContent = '조회' + data[2].readcount + ' '
					+ data[2].title;
			// textarea.value += "상대 : " + event.data + "\n";
		}
		function onOpen(event) {
			textarea.value += "연결 성공\n";
		}
		function onError(event) {
			alert(event.data);
		}
		/*
		 * function send() { textarea.value += "나 : " + inputMessage.value + "\n";
		 * webSocket.send(inputMessage.value); inputMessage.value = ""; }
		 */
	</script>

	<!--  Ajax 핫 Post 확인 페이지 호출 최초에만 ajax로 한번 불러옴-->
	<!-- <script>
	$(document).ready(function(){
		getAjaxList();
	});
		
	function getAjaxList(){
		$.ajax({
			type:"GET",
			url:"board?cmd=board_ajax",
			dataType: "json",
			success:function(data){
				var list = document.querySelectorAll('.list-group-item');
				list[0].textContent = '조회'+data[0].readcount+' '+data[0].title;
				list[1].textContent = '조회'+data[1].readcount+' '+data[1].title;
				list[2].textContent = '조회'+data[2].readcount+' '+data[2].title;
			}
		});
	}
</script> -->

</body>
</html>