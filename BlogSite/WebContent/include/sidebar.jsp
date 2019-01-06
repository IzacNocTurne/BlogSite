<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Sidebar -->
<div class="col-md-4">
	<!-- Search Widget -->
	<div class="card my-4">
		<h5 class="card-header">Search</h5>
		<div class="card-body">
			<form method="GET" action="<%=request.getContextPath()%>/board">
				<div class="input-group">
					<input type="hidden" name="cmd" value="board_search"> <input type="text" class="form-control" name="search" placeholder="Search for..."> <span class="input-group-btn">
						<Button class="btn btn-primary" type="submit">Go!</Button>
					</span>
				</div>
			</form>
		</div>
	</div>

	<!-- Sidebar -->
	<c:choose>
		<c:when test="${!empty hotPost}">
			<div class="card my-4">
				<h5 class="card-header" id="hotTitle">Hot Post</h5>
				<div class="card-body">
					<div class="list-group">
						<c:forEach var="item" items="${hotPost}">
							<a href="<%=request.getContextPath()%>/board?cmd=board_view&num=${item.num}" class="list-group-item list-group-item-action">조회${item.readcount} ${item.title}</a>
						</c:forEach>
					</div>
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<div class="card my-4">
				<h5 class="card-header">Banner</h5>
				<div class="card-body">
					<a href="http://blog.naver.com/codingspecialist" class="list-group-item list-group-item-action" style="text-align: center;"> <img src="<%=request.getContextPath()%>/img/banner.png" width="80%" height="200px"></img>
					</a>
				</div>
			</div>
		</c:otherwise>
	</c:choose>
</div>