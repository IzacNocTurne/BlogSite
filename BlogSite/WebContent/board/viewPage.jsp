<%@page import="com.cos.dao.SecretDAO"%>
<%@page import="com.cos.dto.SecretVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	SecretVO secret = new SecretDAO().getSecret("naver");
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
<script type="text/javascript" src="https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=<%=secret.getApikey() %>&submodules=geocoder"></script>
</head>

<body>
	<!-- Navigation -->
	<jsp:include page="/include/navigation.jsp" />

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
						<p class="lead">
							by <a href="#">${board.id}</a>
						</p>
						<hr>
						<c:if test="${board.id eq sessionScope.id}">
							<a class="btn btn-warning" href="<%=request.getContextPath()%>/board?cmd=board_update&num=${board.num}">수정</a>
							<a class="btn btn-danger" href="<%=request.getContextPath()%>/board?cmd=board_delete&num=${board.num}">삭제</a>
							<hr>
						</c:if>
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
								<div class="form-group">
									<textarea class="form-control" id="replyData" rows="3"></textarea>
								</div>
								<c:choose>
									<c:when test="${empty sessionScope.id}">
										<input type="button" class="btn btn-primary" onclick="alert('로그인 후 이용가능합니다.')" value="Submit">
									</c:when>
									<c:otherwise>
										<input type="button" class="btn btn-primary" onclick="sendReply()" value="Submit">
									</c:otherwise>
								</c:choose>

							</div>
						</div>

						<div id="reply">
							<!-- Comment  -->
							<c:forEach var="item" items="${reboards}">
								<div class="media mb-4">
									<c:choose>
										<c:when test="${item.id == sessionScope.id}">
											<a href="<%=request.getContextPath()%>/board?cmd=reboard_delete&renum=${item.renum}&num=${item.num}"> <img class="d-flex mr-3 rounded-circle" src="<%=request.getContextPath()%>/img/clear.png">
											</a>
										</c:when>
										<c:otherwise>
											<img class="d-flex mr-3 rounded-circle" src="<%=request.getContextPath()%>/img/reply.png">
										</c:otherwise>
									</c:choose>
									<div class="media-body">
										<h5 class="mt-0">${item.id}</h5>
										${item.recontent}
									</div>
								</div>
							</c:forEach>
						</div>

					</div>
				</div>
			</div>
		</div>
		<!-- /.row -->

	</div>
	<!-- /.container -->
	<script>
	function addDiv(renum, num, id, recontent) {  //append 
		var newDiv = document.createElement("div"); 
		newDiv.className = 'media mb-4'; 
		newDiv.innerHTML = "<a href='<%=request.getContextPath()%>/board?cmd=reboard_delete&renum="+renum+"&num="+num+"'><img class='d-flex mr-3 rounded-circle' src='<%=request.getContextPath()%>/img/clear.png'><a/> <div class='media-body'> <h5 class='mt-0'>"+ id + "</h5>" + recontent + "</div></div>";
			document.getElementById('reply').prepend(newDiv); //appendChild(newDIv); 
		}

		function sendReply() {
			var replyData = document.getElementById("replyData");
			var recontent = replyData.value;
			if (recontent == '') {
				alert('글을 입력하세요.');
				return false;
			}
			var jsonData = {
				"recontent" : recontent,
				"id" : "${sessionScope.id}",
				"num" : "${board.num}"
			};		
			var result = JSON.stringify(jsonData);

			replyData.value = '';

			$.ajax({
				type : "POST",
				url : "board?cmd=reboard_reply",
				dataType : "text",
				contentType : 'application/text:charset=utf-8',
				data : result,
				success : function(data) {			
					var result = JSON.parse(data);			
					addDiv(result.renum, result.num, result.id, result.recontent);							
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log("에러 발생~~ \n" + textStatus + " : " + errorThrown);
				}
			});
		}
	</script>

</body>
</html>

<!-- function sendReply(){	
	var myData = {"key":"jooho"};
	$.ajax({
		type:"POST",
		url:"board",
		dataType: "json",
		contentType: 'application/json:charset=utf-8', 
		data : JSON.stringify(myData),
		success:function(data){
			alert('성공');
		},
		error:function(jqXHR, textStatus, errorThrown){
  	alert("에러 발생~~ \n" + textStatus + " : " + errorThrown);
  }
	});
} -->
