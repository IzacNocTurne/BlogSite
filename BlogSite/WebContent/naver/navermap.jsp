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
	<!-- Smart Editor -->
	<script type="text/javascript" src="<%=request.getContextPath()%>/editor/js/HuskyEZCreator.js" charset="utf-8"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/editor/photo_uploader/plugin/hp_SE2M_AttachQuickPhoto.js" charset="utf-8"></script>
	<!-- Bootstrap core JavaScript -->
  <script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
  <script src="<%=request.getContextPath()%>/js/bootstrap.bundle.min.js"></script>
  <script src="<%=request.getContextPath()%>/js/validation.js"></script>
  <script type="text/javascript" src="https://openapi.map.naver.com/openapi/v3/maps.js?clientId=fXTTZsAX4gQL_nf4W10W&submodules=geocoder"></script>
</head>
<body>
<!-- Navigation -->
<jsp:include page="/include/navigation.jsp"/>
 
<div class="container">
	  <h2>오시는길</h2>
	  <h6>수원역</h6> 
</div>

<div class="container">
	<!-- Login Form -->
	<div class="row">
  <!-- Blog Entries Column -->
  <div class="col-md-12 my-order">
	<div class="content-section">
	 <div id="map" style="width:100%;height:400px;"></div>
	    <script>
	      var map = new naver.maps.Map('map');
	      var myaddress = '영통로498';// 도로명 주소나 지번 주소만 가능 (건물명 불가!!!!)
	      naver.maps.Service.geocode({address: myaddress}, function(status, response) {
	          if (status !== naver.maps.Service.Status.OK) {
	              return alert(myaddress + '의 검색 결과가 없거나 기타 네트워크 에러');
	          }
	          var result = response.result;
	          // 검색 결과 갯수: result.total
	          // 첫번째 결과 결과 주소: result.items[0].address
	          // 첫번째 검색 결과 좌표: result.items[0].point.y, result.items[0].point.x
	          var myaddr = new naver.maps.Point(result.items[0].point.x, result.items[0].point.y);
	          map.setCenter(myaddr); // 검색된 좌표로 지도 이동
	          // 마커 표시
	          var marker = new naver.maps.Marker({
	            position: myaddr,
	            map: map
	          });
	          // 마커 클릭 이벤트 처리
	          naver.maps.Event.addListener(marker, "click", function(e) {
	            if (infowindow.getMap()) {
	                infowindow.close();
	            } else {
	                infowindow.open(map, marker);
	            }
	          });
	          // 마크 클릭시 인포윈도우 오픈
	          var infowindow = new naver.maps.InfoWindow({
	              content: '<h4> [네이버 개발자센터]</h4><a href="https://developers.naver.com" target="_blank"><img src="https://developers.naver.com/inc/devcenter/images/nd_img.png"></a>'
	          });
	      });
	      </script>
	</div>
	</div>
		
  </div>
  <!-- ./row -->
</div>
<!-- ./container -->

</body>
</html>