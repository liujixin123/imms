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
            elem: '#msgUserList',
            url:'/msg/getSignMsgList',
            method: 'post', //默认：get请求
            cellMinWidth: 80,
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
                {type:'numbers'}
                ,{field:'userName', title:'订阅人员',align:'center'}
                ,{field:'moduleName', title:'订阅模块',align:'center'}
                ,{field:'lv', title: '消息级别',align:'center'}
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
                delDic(data,data.id,data.userName,data.moduleName);
            } else if(obj.event === 'edit'){
                //编辑
                openMsgUser(data,"编辑");
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
});

function renderForm() {
    layui.use('form', function() {
        var form = layui.form(); //高版本建议把括号去掉，有的低版本，需要加()
        form.render();
    });
}


//提交表单
function formSubmit(obj){
    $.ajax({
        type: "POST",
        data: $("#msgSignForm").serialize(),
        url: "/dic/setDic",
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
}

//开通用户
function addMsgUser(){
    openMsgUser(null,"开通用户");
}
function openMsgUser(data,title){
    var pageNum = $(".layui-laypage-skip").find("input").val();
    $("#pageNum").val(pageNum);

    layer.open({
        type: 2,
        skin: 'layui-layer-demo', //样式类名
        title: '标题',
        // closeBtn: 1, //不显示关闭按钮
        anim: 2,
        area: ['893px', '600px'],
        shadeClose: true, //开启遮罩关闭
        content: "/user/userManageInner"
        ,maxmin: true
        ,btn: [ '关闭']
        ,btn1: function(index, layero){
        layer.close(index);
    }

    });

}

function delDic(obj,id,name,moduleName) {
    layer.confirm('您确定要删除 '+name+'订阅的 "'+moduleName+'"模块消息吗？', {
        btn: ['确认','返回'] //按钮
    }, function(){
        $.post("/msg/msgUserDel",{"id":id},function(data){
            if (data.code == 1) {
                layer.alert(data.msg,function(){
                    layer.closeAll();
                    load(obj);
                });
            } else {
                layer.alert(data.msg);
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

function cleanDic(){
    $("#codevalue1").val("");
    $("#lable1").val("");
    $("#columntype1").val("");
    $("#remark1").val("");
}
