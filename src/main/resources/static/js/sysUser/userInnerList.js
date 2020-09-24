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
            elem: '#uesrList',
            url:'/user/getUserMsgSignList',
            method: 'post', //默认：get请求
            cellMinWidth: 80,
            page: true,
            // data:{ "moduleCode":$("#moduleCode").val()},
            request: {
                pageName: 'pageNum', //页码的参数名称，默认：pageNum
                limitName: 'pageSize'  //每页数据量的参数名，默认：pageSize

            },
            response:{
                statusName: 'code', //数据状态的字段名称，默认：code
                statusCode: 200, //成功的状态码，默认：0
                countName: 'totals', //数据总数的字段名称，默认：count
                dataName: 'list' //数据列表的字段名称，默认：data
            },
            cols: [[
                {type:'checkbox'}
                ,{field:'id', title:'id',align:'center',hide:true}
                ,{field:'sysUserName', title:'登录账号',align:'center'}
                ,{field:'userName', title:'姓名',align:'userName'}
                ,{field:'roleName', title:'角色类型',align:'center'}
                ,{field:'userPhone', title:'手机号',align:'center'}
                ,{field:'regTime', title: '注册时间',align:'center'}
                ,{field:'userStatus', title: '是否有效',align:'center'}
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
        table.on('tool(userTable)', function(obj){
            var data = obj.data;

        });

        //监听提交
        form.on('submit(userSubmit)', function(data){
            // TODO 校验
            formSubmit(data);
            return false;
        });

        $.ajax({
            url:'/role/getRoles',
            dataType:'json',
            async: true,
            success:function(data){
                $.each(data,function(index,item){
                    if(!roleId){
                        var option = new Option(item.roleName,item.id);
                    }else {
                        var option = new Option(item.roleName,item.id);
                        // // 如果是之前的parentId则设置选中
                  /*      if(item.id == roleId) {
                            option.setAttribute("selected",'true');
                        }*/
                    }
                    $('#roleId').append(option);//往下拉菜单里添加元素

                    layui.form.render('select');
                })
            }
        });


        $.ajax({
            url:'/dic/getDicListByType',
            data:{"type":"MSG"},
            dataType:'json',
            async: true,
            success:function(data){
                $.each(data,function(index,item){
                    var option = new Option(item.lable,item.codevalue);
                    $('#moduleCode').append(option);//往下拉菜单里添加元素

                    layui.form.render('select');
                })
            }
        });

        $.ajax({
            url:'/dic/getDicListByType',
            data:{"type":"MSGTYPE"},
            dataType:'json',
            async: true,
            success:function(data){
                $.each(data,function(index,item){
                    var option = new Option(item.lable,item.codevalue);
                    if(item.codevalue == '3') {
                        option.setAttribute("selected",'true');
                    }
                    $('#lv').append(option);//往下拉菜单里添加元素

                    layui.form.render('select'); //这个很重要

                })
            }
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

/*    layui.use(['layer', 'jquery', 'form'], function () {
        var layer = layui.layer,
            $ = layui.jquery,
            form = layui.form;

        form.on('select(moduleCode)', function(data){
            // load(data);
            //重新加载table

        });
    });*/

});

function load(obj){
    //重新加载table
    tableIns.reload({
        where: obj.field
        , page: {
            curr: pageCurr //从当前页码开始
        }
    });
}

function addMsg(){
    var data = layui.table.checkStatus('checkboxTable').data;
    var moduleCode =$("#moduleCode  option:selected").val();
    var module =$("#moduleCode  option:selected").text();
    //选中的个数
    var long = data.length;
    var ids = '';
    var names='';
    console.log(long);

    if(moduleCode =='' || moduleCode==null){
        layer.alert('请选择模块', {
            icon: 5,
            title: "提示"
        });

        return;
    }

    if(long <= 0){
        layer.alert('请选择用户', {
            icon: 5,
            title: "提示"
        });

        return;
     }

    //给数组赋值选中的id
    for(var i=0;i<long;i++){
        if(i==(long-1)){
            ids += data[i].id;
            names +=data[i].sysUserName;
        }else{
            ids += data[i].id+",";
            names +=data[i].sysUserName+",";
        }
    }
    // window.parent.location.reload();//刷新父页面

    layer.confirm('您确定要订阅 "'+module+'" 模块吗？', {
        btn: ['确认','返回'] //按钮
    }, function(){

        $.ajax({
            type: "POST",
            data: {moduleCode:$("#moduleCode").val(),moduleName:module,  lv:$("#lv").val(),ids:ids,names:names},
            url: "/msg/addMsg",
            success: function (data) {
                if (data.code == 1) {
                    layer.alert(data.msg,function(){
                        layer.closeAll();
                        // load(obj);
                        // window.location.reload();
                        window.parent.location.reload();//刷新父页面
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