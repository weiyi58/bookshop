function _change() {
	$("#vCode").attr("src", "/bookshop/VerifyCodeServlet?" + new Date().getTime());
}