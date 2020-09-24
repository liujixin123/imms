/**
 * 用户管理
 */
var pageCurr;
var form;
$(function() {

    layui.use('table', function(){
        var table = layui.table;
        form = layui.form;
        var laypage = layui.laypage;

        tableIns=table.render({
            elem: '#dicList',
            // count:10,
            url:'/dic/getDicList',
            method: 'post', //默认：get请求
            cellMinWidth: 80,
            page: true,
            request: {
                pageName: 'pageNum', //页码的参数名称，默认：pageNum
                limitName: 'pageSize' //每页数据量的参数名，默认：pageSize limit
            },
            response:{
                statusName: 'code', //数据状态的字段名称，默认：code
                statusCode: 200, //成功的状态码，默认：0
                countName: 'totals', //数据总数的字段名称，默认：count
                dataName: 'list' //数据列表的字段名称，默认：data
            },
            cols: [[
                {type:'numbers'}
                ,{field:'codevalue', title:'字典编码',align:'center'}
                ,{field:'lable', title:'字典名称',align:'center'}
                ,{field:'columntype', title:'字典类型',align:'center'}
                ,{field:'remark', title: '备注',align:'center'}
                ,{title:'操作',align:'center', toolbar:'#optBar'}
            ]],
            done: function(res, curr, count){
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                //console.log(res);
                //得到当前页码
                console.log('==='+curr);

                //得到数据总量
                console.log('==='+count);
                pageCurr=curr;
            }
        });



        //监听工具条
        table.on('tool(dicTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                //删除
                delDic(data,data.id,data.lable);
            } else if(obj.event === 'edit'){
                //编辑
                openDic(data,"编辑");
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
});

//提交表单
function formSubmit(obj){
    $.ajax({
        type: "POST",
        data: $("#dicForm").serialize(),
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
function addDic(){
    openDic(null,"开通用户");
}
function openDic(data,title){
    if(data==null || data==""){
        $("#id").val("");
        $("#codevalue1").val("");
        $("#lable1").val("");
        $("#columntype1").val("");
        $("#remark1").val("");
    }else{
        $("#id").val(data.id);
        $("#codevalue1").val(data.codevalue);
        $("#lable1").val(data.lable);
        $("#columntype1").val(data.columntype);
        $("#remark1").val(data.remark);
    }
    var pageNum = $(".layui-laypage-skip").find("input").val();
    $("#pageNum").val(pageNum);

    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['550px'],
        content:$('#setDic'),
        end:function(){
            cleanDic();
        }
    });
}

function delDic(obj,id,name) {
    layer.confirm('您确定要删除 '+name+' 吗？', {
        btn: ['确认','返回'] //按钮
    }, function(){
        $.post("/dic/delDic",{"id":id},function(data){
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
