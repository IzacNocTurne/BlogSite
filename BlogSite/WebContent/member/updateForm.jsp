<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

<script>
function goPopup(){
	// �ּҰ˻��� ������ �˾� �������� ȣ���մϴ�.
	// ȣ��� ������(jusopopup.jsp)���� ���� �ּҰ˻�URL(http://www.juso.go.kr/addrlink/addrLinkUrl.do)�� ȣ���ϰ� �˴ϴ�.
	var pop = window.open("/popup/jusoPopup.jsp","pop","width=570,height=420, scrollbars=yes, resizable=yes"); 
	
	// ����� ���� ���, ȣ��� ������(jusopopup.jsp)���� ���� �ּҰ˻�URL(http://www.juso.go.kr/addrlink/addrMobileLinkUrl.do)�� ȣ���ϰ� �˴ϴ�.
    //var pop = window.open("/popup/jusoPopup.jsp","pop","scrollbars=yes, resizable=yes"); 
}

function jusoCallBack(roadFullAddr){
		// �˾����������� �ּ��Է��� ������ �޾Ƽ�, �� �������� ������ ����մϴ�.
		document.form.roadFullAddr.value = roadFullAddr;	
}
</script>
</head>
<body>
	<!-- Navigation -->
	<jsp:include page="/include/navigation.jsp" />

	<div class="container">
		<!-- Login Form -->
		<div class="row">
			<!-- Blog Entries Column -->
			<div class="col-md-8 my-order">
				<div class="content-section">
					<form id="form" name="form" method="POST" action="<%=request.getContextPath()%>/member?cmd=member_updateProc" onsubmit="return accountCheck(this)">
						<fieldset class="form-group">
							<legend class="border-bottom mb-4">Account</legend>

							<div class="form-group">
								<label class="form-control-label">ID</label> <input class="form-control form-control-lg" type="text" name="id" value="${member.id}" maxlength="20" required readonly>
							</div>
							<div class="form-group">
								<label class="form-control-label">Password</label> <input class="form-control form-control-lg" type="password" name="password" value="" maxlength="20" required autofocus>
							</div>
							<div class="form-group">
								<label class="form-control-label">Confirm_password</label> <input class="form-control form-control-lg" type="password" name="confirm" value="" maxlength="20" required>
							</div>
							<div class="form-group">
								<label class="form-control-label">Username</label> <input class="form-control form-control-lg" type="text" name="username" value="${member.username}" maxlength="20" required readonly>
							</div>
							<div id="list"></div>
							<div id="callBackDiv">
								<div class="form-group">
									<label class="form-control-label">Address</label> <button class="btn btn-outline-info float-right" type="button" onclick="goPopup()">Search Korean Address</button> <input class="form-control form-control-lg" type="text" id="roadFullAddr" name="roadFullAddr" value="${member.roadFullAddr}" maxlength="20" required> 
								</div>
								</div>
							<div class="form-group">
								<label class="form-control-label">Email</label> <input class="form-control form-control-lg" type="email" name="email" value="${member.email}" maxlength="20" required>
							</div>
							<div class="form-group">
								<button class="btn btn-outline-info" type="submit">Modified</button>
								<c:if test="${member.emailcheck == false}">
									<a href="<%=request.getContextPath()%>/gmail/emailSendAction.jsp" class="btn btn-outline-danger">Authenticate</a>
								</c:if>
							</div>
						</fieldset>
					</form>
				</div>
			</div>

			<!-- SideBar -->
			<jsp:include page="/include/sidebar.jsp" />
		</div>
		<!-- ./row -->
	</div>
	<!-- ./container -->
</body>
</html>