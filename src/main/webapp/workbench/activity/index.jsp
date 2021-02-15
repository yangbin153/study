<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" +
request.getServerName() + ":" + request.getServerPort() +
request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href=<%=basePath%>/>

    <meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <link href="jquery/bs_pagination/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>

<script type="text/javascript">

	$(function(){

        //时间拾取器
        $(".time").datetimepicker({
            minView: "month",
            language:  'zh-CN',
            format: 'yyyy-mm-dd',
            autoclose: true,
            todayBtn: true,
            pickerPosition: "bottom-left"
        });


        $("#addBtn").click(function () {
            $.ajax({
                url:"workbench/activity/getUserList.do",
                type:"get",
                dataType:"json",
                success:function (data) {
                    var html = "<option></option>";
                    $.each(data,function (i,n) {
                        html += "<option value='"+n.id+"'>"+n.name+"</option>"
                    })

                    $("#create-owner").html(html);


                    var id = "${user.id}";
                    $("#create-owner").val(id);

                    $("#createActivityModal").modal("show");

                }
            })
        })

        $("#saveBtn").click(function () {
            if ($("#create-owner").val()==""||$("#create-name").val()==""){
                alert("所有者及名称不能为空");
            } else {
                $.ajax({
                    url:"workbench/activity/saveActivity.do",
                    data:{
                        "owner":$("#create-owner").val(),
                        "name":$("#create-name").val(),
                        "startDate":$("#create-startDate").val(),
                        "endDate":$("#create-endDate").val(),
                        "cost":$("#create-cost").val(),
                        "description":$("#create-description").val()
                    },
                    type:"post",
                    dataType:"json",
                    success:function (data) {
                        if (data){

                            //更新市场活动列表（分页）
                            pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

                            $("#createActivityModal").modal("hide");

                        } else {
                            alert("创建市场活动失败")
                        }
                    }
                })
            }

        })

        $("#searchBtn").click(function () {
            $("#hidden-name").val($.trim($("#search-name").val()));
            $("#hidden-owner").val($.trim($("#search-owner").val()));
            $("#hidden-startDate").val($.trim($("#search-startDate").val()));
            $("#hidden-endDate").val($.trim($("#search-endDate").val()));

            pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
        })

        $("#qx").click(function () {
            $("input[name=xz]").prop("checked",this.checked);
        })

        $("#activityBody").on("click",$("input[name=xz]"),function () {
            $("#qx").prop("checked",$("input[name=xz]").length == $("input[name=xz]:checked").length);
        })


        $("#editBtn").click(function () {
            
            if ($("input[name=xz]:checked").length == 0){
                alert("请选中需要修改的市场活动");
            } else if ($("input[name=xz]:checked").length > 1) {
                alert("请选择其中的一条进行修改");
            }else {
                $.ajax({
                    url:"workbench/activity/getActivityAndUserList.do",
                    data:{
                        "id":$("input[name=xz]:checked").val()
                    },
                    type:"post",
                    dataType:"json",
                    success:function (data) {

                        var html = "<option></option>";
                        $.each(data.uList,function (i,n) {
                            html += "<option value='"+n.id+"'>"+n.name+"</option>";
                        })
                        $("#edit-owner").html(html);

                        $("#edit-id").val(data.activity.id);
                        $("#edit-owner").val(data.activity.owner);
                        $("#edit-name").val(data.activity.name);
                        $("#edit-startDate").val(data.activity.startDate);
                        $("#edit-endDate").val(data.activity.endDate);
                        $("#edit-cost").val(data.activity.cost);
                        $("#edit-description").val(data.activity.description);

                        $("#editActivityModal").modal("show");
                    }
                })
            }



        })

        $("#updateBtn").click(function () {
            if ($("#create-owner").val()==""||$("#create-name").val()==""){
                alert("所有者及名称不能为空");
            } else {
                $.ajax({
                    url:"workbench/activity/updateActivity.do",
                    data:{
                        "id":$("#edit-id").val(),
                        "owner":$("#edit-owner").val(),
                        "name":$("#edit-name").val(),
                        "startDate":$("#edit-startDate").val(),
                        "endDate":$("#edit-endDate").val(),
                        "cost":$("#edit-cost").val(),
                        "description":$("#edit-description").val()
                    },
                    type:"post",
                    dataType:"json",
                    success:function (data) {
                        if (data){

                            //更新市场活动列表（分页）
                            pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
                                ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

                            $("#editActivityModal").modal("hide");

                        } else {
                            alert("更新市场活动失败")
                        }
                    }
                })
            }

        })

        $("#deleteBtn").click(function () {
            var $xz = $("input[name=xz]:checked");
            if ($xz.length==0){
                alert("请选择删除的数据");
            }else {
                if (confirm("是否删除选中的数据")){
                var param = "";
                for (var i = 0; i < $xz.length; i++) {
                    param += "id="+$($xz[i]).val();
                    if (i<$xz.length-1){
                        param += "&";
                    }
                }
                    $.ajax({
                        url:"workbench/activity/deleteActivity.do",
                        data:param,
                        type:"post",
                        dataType:"json",
                        success:function (data) {
                            if (data){
                                pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                            } else {
                                alert("删除数据失败")
                            }
                        }
                    })
                }
            }
        })




        pageList(1,2);
	});

	function pageList(pageNo,pageSize){
        $("#qx").prop("checked",false);

	    $.ajax({
            url:"workbench/activity/getPageList.do",
            data:{
                "pageStrNo":pageNo,
                "pageStrSize":pageSize,
                "name":$.trim($("#search-name").val()),
                "owner":$.trim($("#search-owner").val()),
                "startDate":$.trim($("#search-startDate").val()),
                "endDate":$.trim($("#search-endDate").val())
            },
            type:"get",
            dataType:"json",
            success:function (data) {

                var html = "";
                $.each(data.dateList,function (i,n) {
                    html += '<tr class="active">'
                    html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>'
                    html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>'
                    html += '<td>'+n.owner+'</td>'
                    html += '<td>'+n.startDate+'</td>'
                    html += '<td>'+n.endDate+'</td>'
                    html += '</tr>'
                })
                $("#activityBody").html(html);

                var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;

                //数据处理完毕后，结合分页查询，对前端展现分页
                $("#activityPage").bs_pagination({
                    currentPage: pageNo, // 页码
                    rowsPerPage: pageSize, // 每页显示的记录条数
                    maxRowsPerPage: 20, // 每页最多显示的记录条数
                    totalPages: totalPages, // 总页数
                    totalRows: data.total, // 总记录条数

                    visiblePageLinks: 3, // 显示几个卡片

                    showGoToPage: true,
                    showRowsPerPage: true,
                    showRowsInfo: true,
                    showRowsDefaultInfo: true,

                    //该回调函数是在点击分页组件的时候触发的
                    onChangePage : function(event, data){
                        pageList(data.currentPage , data.rowsPerPage);
                    }
                });
            }
        })
    }
	
</script>
</head>
<body>

<input type="hidden" id="hidden-name">
<input type="hidden" id="hidden-owner">
<input type="hidden" id="hidden-startDate">
<input type="hidden" id="hidden-endDate">
<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">
                    <input type="hidden" id="edit-id">

                    <div class="form-group">
                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-owner">
                                <%--<option>zhangsan</option>
                                <option>lisi</option>
                                <option>wangwu</option>--%>
                            </select>
                        </div>
                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-name" value="发传单">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-startDate" value="2020-10-10">
                        </div>
                        <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-endDate" value="2020-10-20">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost" value="5,000">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-description">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="updateBtn">更新</button>
            </div>
        </div>
    </div>
</div>


<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">
								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	

	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;" >
                <div id="activityPage">
                </div>
				<%--<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>--%>
			</div>
			
		</div>
		
	</div>
</body>
</html>