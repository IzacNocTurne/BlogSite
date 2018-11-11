<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Sidebar -->
<div class="col-md-4">
  <!-- Search Widget -->
  <div class="card my-4">
    <h5 class="card-header">Search</h5>
    <div class="card-body">
      <div class="input-group">
        <input type="text" class="form-control" placeholder="Search for...">
        <span class="input-group-btn">
          <button class="btn btn-primary" type="button">Go!</button>
        </span>
      </div>
    </div>
  </div>

  <!-- Sidebar -->
  <div class="card my-4">
    <h5 class="card-header">Hot Post</h5>
    <div class="card-body">
      <div class="list-group">
      	<c:forEach var="item" items="${hotPost}">
         	<a href="<%=request.getContextPath()%>/board?cmd=board_view&num=${item.num}" class="list-group-item list-group-item-action">조회${item.readcount} ${item.title}</a>
		  	</c:forEach>
      </div>
    </div>
  </div>
</div>

<!--  Ajax 핫 Post 확인 -->
<script>
		$(document).ready(function(){
			getAjaxList();
		});
		
		function getAjaxList(){
			$.ajax({
				type:"GET",
				url:"board?cmd=board_ajax",
				dataType: "json",
				ifModified: true,
				success:function(data){
					var list = document.querySelectorAll('.list-group-item');
					list[0].textContent = '조회'+data[0].readcount+' '+data[0].title;
					list[1].textContent = '조회'+data[1].readcount+' '+data[1].title;
					list[2].textContent = '조회'+data[2].readcount+' '+data[2].title;
				}
			});
		}
		
		setInterval(getAjaxList,60000); //1분 마다 실행
</script>