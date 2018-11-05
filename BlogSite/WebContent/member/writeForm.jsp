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
	<!-- Smart Editor -->
	<script type="text/javascript" src="<%=request.getContextPath()%>/editor/js/HuskyEZCreator.js" charset="utf-8"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/editor/photo_uploader/plugin/hp_SE2M_AttachQuickPhoto.js" charset="utf-8"></script>
</head>
<body>
<!-- Navigation -->
<jsp:include page="../include/navigation.jsp"/>
 
<div class="container">
	<!-- Login Form -->
	<div class="row">
  <!-- Blog Entries Column -->
	  <div class="col-md-12 my-order">
		<div class="content-section">
			<form name="w_form" action="#" method="post">
				<!-- filepath : 이미지업로드 경로 -->
				<input type="hidden" name="filepath" value="/upImages/" />
				<fieldset class="form-group">
					<legend class="border-bottom mb-4">New Post</legend>
					<div class="form-group">
							<label class="form-control-label">Title</label>
							<input class="form-control form-control-lg" type="text">
					</div>
					<div class="form-group">
							<label class="form-control-label">Content</label>
							<!-- <textarea class="form-control" rows="10" cols="50" style="background-image:url('/Blog/img/background.png');"></textarea> -->
					 		<textarea style="width: 100%" rows="10" name="content" id="textAreaContent" cols="80"></textarea>
					</div>
					<div class="form-group">
							<button class="btn btn-outline-info" type="submit" >Update</button>
					</div>		
				</fieldset>
			</form>
		</div>
		</div>
  </div>
  <!-- ./row -->
</div>
<!-- ./container -->

<!-- Naver Smart Editor 2 -->
<script>
  var oEditors = [];
  nhn.husky.EZCreator.createInIFrame({
      oAppRef: oEditors,
      elPlaceHolder: "textAreaContent",
      sSkinURI: "<%=request.getContextPath()%>/editor/SmartEditor2Skin.html",
      fCreator: "createSEditor2"
  });
   
  //‘저장’ 버튼을 누르는 등 저장을 위한 액션을 했을 때 submitContents가 호출된다고 가정한다.
  function submitContents(elClickedObj) {
      // 에디터의 내용이 textarea에 적용된다.
      oEditors.getById["textAreaContent"].exec("UPDATE_CONTENTS_FIELD", [ ]);
   
      // 에디터의 내용에 대한 값 검증은 이곳에서
      // document.getElementById("textAreaContent").value를 이용해서 처리한다.
    
      try {
          elClickedObj.form.submit();
      } catch(e) {
       
      }
  }
   
  // textArea에 이미지 첨부
  function pasteHTML(filepath){
      var sHTML = '<img src="<%=request.getContextPath()%>/editor/upload/'+filepath+'">';
      oEditors.getById["textAreaContent"].exec("PASTE_HTML", [sHTML]);
  }
</script>

  
<!-- Bootstrap core JavaScript -->
<script src="../js/jquery.min.js"></script>
<script src="../js/bootstrap.bundle.min.js"></script>
<script src="../js/validation.js"></script>
</body>
</html>