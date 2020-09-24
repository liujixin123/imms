/**
 * 用户管理
 */
var pageCurr;
var form;
$(function() {
    layui.use('table', function(){
        var table = layui.table;
        form = layui.form;

        tableIns=table.render({
            id:'checkboxTable',
            elem: '#dicList',
            url:'/msg/getMsgList',
            method: 'post', //默认：get请求
            cellMinWidth: 150,
            page: true,
            request: {
                pageName: 'pageNum', //页码的参数名称，默认：pageNum
                limitName: 'pageSize' //每页数据量的参数名，默认：pageSize
            },
            response:{
                statusName: 'code', //数据状态的字段名称，默认：code
                statusCode: 200, //成功的状态码，默认：0
                countName: 'totals', //数据总数的字段名称，默认：count
                dataName: 'list' //数据列表的字段名称，默认：data
            },
            cols: [[
                {type:'checkbox'}
                ,{field:'title', title:'标题',align:'center',templet: function (d) {
                       /* if (d.actions == "新增" || d.actions == "修改") {
                            return '<a href="javascript:void(0)" style="text-decoration:underline; color:blue;"  onclick="goUrl(\'' + d.url  + '\', \'' + d.sid+ '\', \''+ d.title+ '\', \''+ d.rid+ '\')" >' + d.title + '</a>';

                        }else if (d.actions == "批量新增" ){
                            return '<a href="javascript:void(0)" style="text-decoration:underline; color:blue;"  onclick="goUrlBatch(\'' + d.url  + '\', \'' + d.version+ '\', \''+ d.title+ '\', \''+ d.rid+ '\')" >' + d.title + '</a>';

                        }*/
                        return '<a href="javascript:void(0)" style="text-decoration:underline; color:blue;"  onclick="goUrlBatch(\'' + d.url  + '\', \'' + d.sid + '\', \'' + d.version+ '\', \''+ d.title+ '\', \''+ d.rid+ '\')" >' + d.title + '</a>';

                    }}
                ,{field:'isRead', title:'是否阅读',align:'center', templet: function (d) {
                        if (d.isRead == "未读") {
                            return '<span style="color:red">' + d.isRead + '</span>'
                        }
                        if (d.isRead == "已读") {
                            return '<span style="color:green">' + d.isRead + '</span>'
                        }

                    }}
                ,{field:'rid', title:'rid',align:'center',hide:true}
                ,{field:'actionUserName', title:'操作人',align:'center'}
                ,{field:'moduleCode', title:'模块',align:'center',hide:true}
                ,{field:'moduleName', title:'模块',align:'center'}
                ,{field:'url', title:'菜单地址',align:'center'}
                ,{field:'actions', title:'动作',align:'center'}
                ,{field:'sid', title:'ID',align:'center',hide:true, templet : function(row) {
                        if(row.sid==null){
                            return "";
                        }
                        return row.sid;
                    }}
                ,{field:'sname', title:'名称(序列)',align:'center'}
                ,{field:'version', title:'批量操作版本号',align:'center', templet : function(row) {
                        if(row.version==null){
                            return "";
                        }
                        return row.version;
                    }}
              /*  ,{field:'title', title:'标题',align:'center'}*/
                ,{field:'oncreate', title:'操作时间',align:'center'}
                ,{title:'操作',align:'center', toolbar:'#optBar'}
            ]],
            done: function(res, curr, count){
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                //console.log(res);
                //得到当前页码
                console.log(curr);
                $("[data-field='userStatus']").children().each(function(){
                    if($(this).text()=='1'){
                        $(this).text("有效")
                    }else if($(this).text()=='0'){
                        $(this).text("失效")
                    }
                });
                //得到数据总量
                //console.log(count);
                pageCurr=curr;
            }
        });

        //监听工具条
        table.on('tool(dicTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                //删除
                msgDel(data,data.id);
            }
        });

        //监听提交
        form.on('submit(dicSubmit)', function(data){
            // TODO 校验
            formSubmit(data);
            return false;
        });
    });

    //搜索框
    layui.use(['form','laydate'], function(){
        var form = layui.form ,layer = layui.layer
            ,laydate = layui.laydate;
        //日期
        laydate.render({
            elem: '#startTime'
        });
        laydate.render({
            elem: '#endTime'
        });
        //TODO 数据校验
        //监听搜索框
        form.on('submit(searchSubmit)', function(data){
            //重新加载table
            load(data);
            return false;
        });
    });

    $.ajax({
        url:'/dic/getDicListByType',
        data:{"type":"MSG"},
        dataType:'json',
        async: true,
        success:function(data){
            $.each(data,function(index,item){
                var option = new Option(item.lable,item.codevalue);
                /* $("#moduleCode").append("<option value='' selected>请选择</option>");*/
                $('#moduleCode').append(option);//往下拉菜单里添加元素
                layui.form.render('select');
                // form.render('select'); //这个很重要

            })
        }
    });
    $.ajax({
        url:'/dic/getDicListByType',
        data:{"type":"MSG_IS_READ"},
        dataType:'json',
        async: true,
        success:function(data){
            $.each(data,function(index,item){
                var option = new Option(item.lable,item.codevalue);
                /* $("#moduleCode").append("<option value='' selected>请选择</option>");*/
                $('#isRead').append(option);//往下拉菜单里添加元素
                layui.form.render('select');
                // form.render('select'); //这个很重要

            })
        }
    });

});

function goUrl(url,id,title,rid) {
    // window.location=url+"?id="+id;
    $.post("/msg/isRead",{"rid":rid},function(data){

    });
    layer.open({
        type: 2,
        skin: 'layui-layer-demo', //样式类名
        title: title,
        // closeBtn: 1, //不显示关闭按钮
        anim: 2,
        area: ['893px', '600px'],
        shadeClose: true, //开启遮罩关闭
        content: url+"?id="+id
        ,maxmin: true
        ,btn: [ '关闭']
        ,btn1: function(index, layero){
            layer.close(index);
            window.location.reload();
        }

    });
}

function goUrlBatch(url, id,version,title,rid) {
    // window.location=url+"?version="+version;
    $.post("/msg/isRead",{"rid":rid},function(data){

    });
    if(id == null){
        id='';
    }

    if(version == null){
        version='';
    }

    layer.open({
        type: 2,
        skin: 'layui-layer-demo', //样式类名
        title: title,
        // closeBtn: 1, //不显示关闭按钮
        anim: 2,
        area: ['893px', '600px'],
        shadeClose: true, //开启遮罩关闭
        content: url+"?id="+id+"&version="+version
        ,maxmin: true
        ,btn: [ '关闭']
        ,btn1: function(index, layero){
            layer.close(index);
            window.location.reload();
        }

    });
}

function msgDel(obj,id) {
    $.post("/msg/msgDel",{"id":id},function(data){
        if (data.code == 1) {
            layer.alert(data.msg,function(){
                layer.closeAll();
                load(obj);
            });
        } else {
            layer.alert(data.msg);
        }
    });
}

function deleteMsgBatch(){
    var data = layui.table.checkStatus('checkboxTable').data;
    var long = data.length;
    var ids = '';
    if(long <= 0){
        layer.alert('请选择要删除的消息', {
            icon: 5,
            title: "提示"
        });

        return;
    }

    //给数组赋值选中的id
    for(var i=0;i<long;i++){
        if(i==(long-1)){
            ids += data[i].id;
        }else{
            ids += data[i].id+",";
        }
    }

    layer.confirm('您确定要批量删除消息吗？', {
        btn: ['确认','返回'] //按钮
    }, function(){

        $.ajax({
            type: "POST",
            data: { ids:ids },
            url: "/msg/deleteMsgBatch",
            success: function (data) {
                if (data.code == 1) {
                    layer.alert(data.msg,function(){
                        layer.closeAll();
                        // load(obj);
                        window.location.reload();
                    });
                } else {
                    layer.alert(data.msg);
                }
            },
            error: function () {
                layer.alert("操作请求错误，请您稍后再试",function(){
                    layer.closeAll();
                    //加载load方法
                    load(obj);//自定义
                });
            }
        });
    }, function(){
        layer.closeAll();
    });
}


function load(obj){
    //重新加载table
    tableIns.reload({
        where: obj.field
        , page: {
            curr: pageCurr //从当前页码开始
        }
    });
}

