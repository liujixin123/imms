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
            elem: '#immsCRoomList',
            url:'/room/getImmsCRoomList',
            method: 'post', //默认：get请求
            cellMinWidth: 100,
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
                ,{field:'cabinetName', title:'机柜名称',align:'center'}
                ,{field:'devHeight', title:'设备高度',align:'center'}
                ,{field:'devName', title:'设备名称',align:'center'}
                ,{field:'devManu', title: '制造商',align:'center'}
                ,{field:'devModel', title: '设备型号',align:'center'}
                ,{field:'devNumber', title:'序列号',align:'center',width:'15%'}
                ,{field:'system', title:'所属系统',align:'center'}
                ,{field:'safeArea', title:'安全区',align:'center'}
                ,{field:'maintainLevel', title: '运维等级',align:'center'}
                ,{field:'useTime', title: '投运日期',align:'center',width:'10%'}
                ,{field:'responsibilityDpt', title:'责任处室',align:'center'}
                ,{field:'responsibilityMan', title:'责任人',align:'center'}
                ,{field:'maintainManu', title:'运维厂商',align:'center'}
                ,{field:'maintainMan', title: '运维人员',align:'center'}
                ,{field:'maintainManPhone', title: '联系电话',align:'center',width:'15%'}
                ,{field:'ip', title: 'IP',align:'center',width:'15%'      ,templet: function (d) {
                if(d.ip !=null ){
                    // var v_ip=d.ip.replaceAll(/\n/g,"<br/>").replaceAll(";","<br/>");
                    var v_ip=d.ip.replace(/;/g,"<br/>");
                    return v_ip;
                    }
                    return d.ip;
                    }
                    }
                ,{field:'room', title: '所属机房',align:'center'}
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
                // $("[data-field='ip']").children().each(function(){
                    // $(this).css("white-space",'pre-wrap');
                // });
                // autoFixedHeight(this.elem[0]);
                //得到数据总量
                //console.log(count);
                pageCurr=curr;
            }
        });

        //监听工具条
        table.on('tool(devTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                delRoom(data,data.id,data.devNumber);
            } else if(obj.event === 'edit'){
                openRoom(data,"编辑");
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
            elem: '#useTime2'
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

//固定行随单元格自动调整
function autoFixedHeight(tableElem) {
    // 获取表格div
    var $tableView = $(tableElem).next(".layui-table-view");
    // 获取两侧浮动栏
    var $fixed = $tableView.find(".layui-table-fixed");
    var dataIndex;
    var trHeight;
    // 遍历tr 修正浮动栏行高
    $tableView.find(".layui-table-main").find("tr").each(function () {
        dataIndex = $(this).attr("data-index");
        trHeight = $(this).css("height");
        $fixed.find("tr[data-index=" + dataIndex + "]").css("height", trHeight);
    });
}

//提交表单
function formSubmit(obj){
    $.ajax({
        type: "POST",
        data: $("#devForm").serialize(),
        url: "/room/setImmsCRoom",
        success: function (data) {
            if (data.code == 1) {
                layer.alert(data.msg,function(){
                    layer.closeAll();
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

function addRoomBath(){
    layer.open({
        type: 2,
        skin: 'layui-layer-demo', //样式类名
        title: "提示",
        // closeBtn: 1, //不显示关闭按钮
        anim: 2,
        area: ['580px', '400px'],
        shadeClose: true, //开启遮罩关闭
        content:"/room/importWarehouse"
        ,maxmin: true
        ,btn: [ '关闭']
        ,btn1: function(index, layero){
            layer.close(index);
            window.location.reload();
        }

    });
}

//开通用户
function addRoom(){
    openRoom(null,"开通用户");
}
function openRoom(data,title){
    if(data==null || data==""){
        $("#id").val("");
        $("#cabinetName2").val("");
        $("#devHeight2").val("");
        $("#devName2").val("");
        $("#devManu2").val("");
        $("#useTime2").val("");
        $("#devModel2").val("");
        $("#devNumber2").val("");
        $("#system2").val("");
        $("#safeArea2").val("");
        $("#maintainLevel2").val("");
        $("#responsibilityDpt2").val("");
        $("#responsibilityMan2").val("");
        $("#maintainManu2").val("");
        $("#maintainMan2").val("");
        $("#maintainManPhone2").val("");
        $("#ip2").val("");
        $("#room2").val("");
        $("#remarks").val("");
    }else{
        $("#id").val(data.id);
        $("#cabinetName2").val(data.cabinetName);
        $("#devHeight2").val(data.devHeight);
        $("#devName2").val(data.devName);
        $("#devManu2").val(data.devManu);
        $("#useTime2").val(data.useTime);
        $("#devModel2").val(data.devModel);
        $("#devNumber2").val(data.devNumber);
        $("#system2").val(data.system);
        $("#safeArea2").val(data.safeArea);
        $("#maintainLevel2").val(data.maintainLevel);
        $("#responsibilityDpt2").val(data.responsibilityDpt);
        $("#responsibilityMan2").val(data.responsibilityMan);
        $("#maintainManu2").val(data.maintainManu);
        $("#maintainMan2").val(data.maintainMan);
        $("#maintainManPhone2").val(data.maintainManPhone);
        $("#ip2").val(data.ip);
        $("#room2").val(data.room);
        $("#remarks").val(data.remarks);
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
        content:$('#setRoom'),
        end:function(){
            cleanRoom();
        }
    });
}

function delRoom(obj,id,name) {
    layer.confirm('您确定要删除序列是 '+name+' 的设备吗？', {
        btn: ['确认','返回'] //按钮
    }, function(){
        $.post("/room/delRoom",{"id":id},function(data){
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


function createTwo(obj,id){
    if(null!=id){
        var url = "/room/two/dimension/code/create?id="+id;
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

function queryTwo(obj,id){
    if(null!=id){
        var url = "/room/two/dimension/code/query?id="+id;
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

function cleanRoom(){
    $("#cabinetName2").val("");
    $("#devName2").val("");
    $("#devHeight2").val("");
    $("#devManu2").val("");
    $("#useTime2").val("");
    $("#devModel2").val("");
    $("#devNumber2").val("");
    $("#system2").val("");
    $("#safeArea2").val("");
    $("#maintainLevel2").val("");
    $("#responsibilityDpt2").val("");
    $("#responsibilityMan2").val("");
    $("#maintainManu2").val("");
    $("#maintainMan2").val("");
    $("#maintainManPhone2").val("");
    $("#ip2").val("");
    $("#room2").val("");
    $("#remarks").val('');
}


function deleteRoomBatch(){
    var data = layui.table.checkStatus('checkboxTable').data;
    var long = data.length;
    var ids = '';
    if(long <= 0){
        layer.alert('请选择要删除的设备', {
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

    layer.confirm('您确定要批量删除设备吗？', {
        btn: ['确认','返回'] //按钮
    }, function(){

        $.ajax({
            type: "POST",
            data: { ids:ids },
            url: "/room/deleteRoomBatch",
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
function doexport(obj){
    obj.href = "/room/getImmsCRoomList/export?data="+$("#userSearch").serialize();
}