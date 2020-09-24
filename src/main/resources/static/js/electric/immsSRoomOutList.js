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
            elem: '#immsSRoomList',
            url:'/device/getImmsSRoomOutList',
            method: 'post', //默认：get请求
            cellMinWidth: 100,
            page: true,
            where:{ devStatus:'OUT'},
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
                ,{field:'devType', title:'设备类型',align:'center'}
                ,{field:'devManu', title:'制造商',align:'center'}
                ,{field:'devModel', title:'设备型号',align:'center'}
                ,{field:'devNumber', title:'设备序列号',align:'center',width:'15%'}
                ,{field:'inRoomTime', title: '入库时间',align:'center',width:'10%'}
                ,{field:'useType', title: '类别',align:'center'}
                ,{field:'receiver', title:'接收人',align:'center'}
                ,{field:'devAddress', title:'存放地点',align:'center',width:'10%'}
                ,{field:'responsibilityDpt', title:'责任处室',align:'center'}
                ,{field:'responsibilityMan', title: '责任人',align:'center'}
                ,{field:'project', title: '所属项目',align:'center',width:'10%'}
                ,{field:'devStatus', title:'设备状态',align:'center'}
                ,{field:'outRoomTime', title:'出库时间',align:'center',width:'10%'}
                ,{field:'outRoomUser', title:'出货人',align:'center'}
                ,{field:'devGo', title: '设备去处',align:'center'}
                ,{field:'remarks', title: '备注',align:'center'}
                ,{title:'二维码',align:'center', toolbar:'#twoBar'}
                ,{field:'version',title:'版本', align:'center'}
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
                pageCurr=curr;
            }
        });

        //监听工具条
        table.on('tool(devTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                delDev(data,data.id,data.devNumber);
            } else if(obj.event === 'edit'){
                openDev(data,"编辑");
            }else if(obj.event === 'createTwo'){
                createTwo(data,data.id);
            }else if(obj.event === 'queryTwo'){
                queryTwo(data,data.id);
            }
        });

        //监听提交
        form.on('submit(devSubmit)', function(data){
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
        laydate.render({
            elem: '#inRoomTime2'
        });
        laydate.render({
            elem: '#outRoomTime2'
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
        data:{"type":"DEVTYPE"},
        dataType:'json',
        async: true,
        success:function(data){
            $.each(data,function(index,item){
                var option = new Option(item.lable,item.codevalue);
                $('#devType2').append(option);//往下拉菜单里添加元素
                layui.form.render('select');
            })
        }
    });

    $.ajax({
        url:'/dic/getDicListByType',
        data:{"type":"USETYPE"},
        dataType:'json',
        async: true,
        success:function(data){
            $.each(data,function(index,item){
                var option = new Option(item.lable,item.codevalue);
                $('#useType2').append(option);//往下拉菜单里添加元素
                layui.form.render('select');
            })
        }
    });
});

//提交表单
function formSubmit(obj){
    $.ajax({
        type: "POST",
        data: $("#devForm").serialize(),
        url: "/device/setImmsSRoom",
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
function addDev(){
    openDev(null,"新增库房设备");
}
function openDev(data,title){
    if(data==null || data==""){
        $("#id").val("");
        // $("#devType2").val("");
        $("#devType2").find("option").eq(0).prop("selected",true)
        $("#devModel2").val("");
        $("#devManu2").val("");
        $("#devNumber2").val("");
        $("#inRoomTime2").val("");
        // $("#useType2").val("");
        $("#useType2").find("option").eq(0).prop("selected",true)
        $("#receiver2").val("");
        $("#devAddress2").val("");
        $("#responsibilityDpt2").val("");
        $("#responsibilityMan2").val("");
        $("#project2").val("");
        $("#devStatus2 option:contains('出库')").prop("selected",true);
        $("#outRoomTime2").val("");
        $("#outRoomUser2").val("");
        $("#devGo2").val("");
        $("#remarks").val("");
        $("#outroom").hide();
        form.render('select');
    }else{
        $("#id").val(data.id);
        $("#devType2").val(data.devType);
        $("#devModel2").val(data.devModel);
        $("#devManu2").val(data.devManu);
        $("#devNumber2").val(data.devNumber);
        $("#inRoomTime2").val(data.inRoomTime);
        $("#useType2").val(data.useType);
        $("#receiver2").val(data.receiver);
        $("#devAddress2").val(data.devAddress);
        $("#responsibilityDpt2").val(data.responsibilityDpt);
        $("#responsibilityMan2").val(data.responsibilityMan);
        $("#project2").val(data.project);
        $("#devStatus2 option:contains('出库')").prop("selected",true);
        $("#outRoomTime2").val(data.outRoomTime);
        $("#outRoomUser2").val(data.outRoomUser);
        $("#devGo2").val(data.devGo);
        $("#remarks").val(data.remarks);
        $("#outroom").show();
        form.render('select');
    }
    var pageNum = $(".layui-laypage-skip").find("input").val();
    $("#pageNum").val(pageNum);

    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['1000px'],
        content:$('#setDev'),
        end:function(){
            cleanDev();
        }
    });
}

function delDev(obj,id,name) {
    layer.confirm('您确定要删除序列是 '+name+' 的设备吗？', {
        btn: ['确认','返回'] //按钮
    }, function(){
        $.post("/device/delDev",{"id":id},function(data){
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
function queryTwo(obj,id){
    if(null!=id){
        var url = "/device/two/dimension/code/query?id="+id;
        layer.open({
            type: 2,
            title: "二维码查看",
            area:  ['500px', '500px'],
            maxmin: true, //开启最大化最小化按钮
            shadeClose: true,
            content: url
        });
    }
}

function createTwo(obj,id){
    if(null!=id){
        var url = "/device/two/dimension/code/create?id="+id;
        layer.open({
            type: 2,
            title: "二维码生成",
            area:  ['500px', '500px'],
            maxmin: true, //开启最大化最小化按钮
            shadeClose: true,
            content: url
        });
    }
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

function cleanDev(){
    $("#devType2").val("");
    $("#devManu2").val("");
    $("#devModel2").val("");
    $("#devNumber2").val("");
    $("#inRoomTime2").val("");
    $("#useType2").val("");
    $("#Receiver2").val("");
    $("#devAddress2").val("");
    $("#responsibilityDpt2").val("");
    $("#responsibilityMan2").val("");
    $("#project2").val("");
    $("#devStatus2").val("");
    $("#outRoomTime2").val("");
    $("#outRoomUser2").val("");
    $("#devGo2").val("");
    $("#remarks").val('');
}

function doexport(obj){
    obj.href = "/device/getImmsSRoomList/export2?data="+$("#userSearch").serialize()+"&status=OUT";
}


function deleteDevBatch(){
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
            url: "/device/deleteDevBatch",
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