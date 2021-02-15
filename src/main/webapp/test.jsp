<%@ page contentType="text/html;charset=UTF-8"
         language="java" %>
<%
    String basePath = request.getScheme() + "://" +
            request.getServerName() + ":" + request.getServerPort() +
            request.getContextPath() + "/";
%>
<html>
<head>
    <title>Title</title>
    <base href=<%=basePath%>/>
</head>
<body>

//时间拾取器
$(".time").datetimepicker({
minView: "month",
language:  'zh-CN',
format: 'yyyy-mm-dd',
autoclose: true,
todayBtn: true,
pickerPosition: "bottom-left"
});

//分页展示
xxxPageList($("#cluePage").bs_pagination('getOption', 'currentPage')
,$("#cluePage").bs_pagination('getOption', 'rowsPerPage'));


$.ajax({
url:"",
data:{

},
type:"",
dataType:"",
success:function (data) {

}
})

</body>
</html>