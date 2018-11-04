function joinCheck(form){
	
	var id = form.id.value;
	var password = form.password.value;
	var confirm = form.confirm.value;
	var username = form.username.value;
	var email = form.email.value;
	
	var check = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/;
	if(check.test(id)){
	    alert("id는 한글을 입력할 수 없습니다.");
	    return false;
	}
	
	if (password != confirm) {
		alert("비밀번호를 확인하세요");
		return false;
	}
	
	return true;
}

function loginCheck(form){
	
	var id = form.id.value;
	var password = form.password.value;
	
	var check = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/;
	if(check.test(id)){
	    alert("id는 한글을 입력할 수 없습니다.");
	    return false;
	}

	return true;
}