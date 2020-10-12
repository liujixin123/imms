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
            elem: '#immsServerList',
            url:'/server/getImmsServerList',
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
                ,{field:'powerRate', title:'电源额定功率',align:'center'}
                ,{field:'powerNum', title: '电源数量',align:'center'}
                ,{field:'systemType', title:'操作系统类型',align:'center'}
                ,{field:'systemVersion', title:'操作系统版本',align:'center'}
                ,{field:'cpuNum', title: 'CPU数量',align:'center'}
                ,{field:'cpuXh', title: 'CPU型号',align:'center'}
                ,{field:'cpuZp', title: 'CPU主频',align:'center'}
                ,{field:'cpuHs', title:'单CPU核数',align:'center'}
                ,{field:'romNum', title:'内存条数量',align:'center'}
                ,{field:'romSize', title:'内存大小',align:'center'}
                ,{field:'diskType', title:'硬盘类型',align:'center'}
                ,{field:'diskNum', title:'硬盘数量',align:'center'}
                ,{field:'diskSize', title:'硬盘容量',align:'center'}
                ,{field:'hbaNum', title:'HBA卡数',align:'center'}
                ,{field:'netcardNum', title:'网卡数',align:'center'}
                ,{field:'raidDetail', title:'RAID方式',align:'center'}
                ,{field:'externalRom', title:'外接存储',align:'center'}
                ,{field:'doubleVersion', title:'双机版本',align:'center'}
                ,{field:'gks', title:'光口数',align:'center'}
                ,{field:'roomName', title:'所属机房',align:'center'}
                ,{field:'remarks', title:'备注',align:'center'}
                ,{field:'createTime', title: '创建时间',align:'center'}
                ,{title:'二维码',align:'center', toolbar:'#twoBar'}
                ,{title:'操作',align:'center', toolbar:'#optBar'}
            ]],
            done: function(res, curr, count){
                console.log(curr);
                pageCurr=curr;
            }
        });

        //监听工具条
        table.on('tool(devTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                delServer(data,data.id,data.code);
            } else if(obj.event === 'edit'){
                openServer(data,"编辑");
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
    $.ajax({
        url:'/server/getImmsRoom',
        dataType:'json',
        async: true,
        success:function(data){
            $.each(data,function(index,item){
                var option = new Option(item.roomName,item.roomId);
                $('#roomId2').append(option);//往下拉菜单里添加元素
                layui.form.render('select');
            })
        }
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
        url: "/server/setImmsServer",
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

function addServerBath(){
    layer.open({
        type: 2,
        skin: 'layui-layer-demo', //样式类名
        title: "提示",
        // closeBtn: 1, //不显示关闭按钮
        anim: 2,
        area: ['580px', '400px'],
        shadeClose: true, //开启遮罩关闭
        content:"/server/importServer"
        ,maxmin: true
        ,btn: [ '关闭']
        ,btn1: function(index, layero){
            layer.close(index);
            window.location.reload();
        }

    });
}

//开通用户
function addServer(){
    openServer(null,"新增屏柜");
}
function openServer(data,title){
    if(data==null || data==""){
        $("#id").val("");
        $("#powerRate2").val("");
        $("#powerNum2").val("");
        $("#systemType2").val("");
        $("#systemVersion2").val("");
        $("#devType2").val("");
        $("#roomId2").find("option").eq(0).prop("selected",true);
        $("#cpuNum2").val("");
        $("#cpuXh2").val("");
        $("#cpuZp2").val("");
        $("#cpuHs2").val("");
        $("#romNum2").val("");
        $("#romSize2").val("");
        $("#diskType2").val("");
        $("#diskNum2").val("");
        $("#diskSize2").val("");
        $("#hbaNum2").val("");
        $("#netcardNum2").val("");
        $("#raidDetail2").val("");
        $("#externalRom2").val("");
        $("#doubleVersion2").val("");
        $("#gks2").val("");
        $("#serverPwd2").val("");
    }else{
        $("#id").val(data.id);
        $("#powerRate2").val(data.powerRate);
        $("#powerNum2").val(data.powerNum);
        $("#systemType2").val(data.systemType);
        $("#systemVersion2").val(data.systemVersion);
        $("#devType2").val(data.devType);
        $("#roomId2").val(data.roomId);
        $("#cpuNum2").val(data.cpuNum);
        $("#cpuXh2").val(data.cpuXh);
        $("#cpuZp2").val(data.cpuZp);
        $("#cpuHs2").val(data.cpuHs);
        $("#romNum2").val(data.romNum);
        $("#romSize2").val(data.romSize);
        $("#diskType2").val(data.diskType);
        $("#diskNum2").val(data.diskNum);
        $("#diskSize2").val(data.diskSize);
        $("#hbaNum2").val(data.hbaNum);
        $("#netcardNum2").val(data.netcardNum);
        $("#raidDetail2").val(data.raidDetail);
        $("#externalRom2").val(data.externalRom);
        $("#doubleVersion2").val(data.doubleVersion);
        $("#gks2").val(data.gks);
        $("#serverPwd2").val(data.serverPwd);
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
        content:$('#setServer'),
        end:function(){
            cleanServer();
        }
    });
}

function delServer(obj,id,name) {
    layer.confirm('您确定要删除编号是 '+name+' 的服务器吗？', {
        btn: ['确认','返回'] //按钮
    }, function(){
        $.post("/server/delServer",{"id":id},function(data){
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
        var url = "/server/two/dimension/code/create?id="+id;
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
        var url = "/server/two/dimension/code/query?id="+id;
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
function cleanServer(){
    $("#powerRate2").val("");
    $("#powerNum2").val("");
    $("#systemType2").val("");
    $("#systemVersion2").val("");
    $("#devType2").val("");
    $("#roomId2").val("");
    $("#cpuNum2").val("");
    $("#cpuXh2").val("");
    $("#cpuZp2").val("");
    $("#cpuHs2").val("");
    $("#romNum2").val("");
    $("#romSize2").val("");
    $("#diskType2").val("");
    $("#diskNum2").val("");
    $("#diskSize2").val("");
    $("#hbaNum2").val("");
    $("#netcardNum2").val("");
    $("#raidDetail2").val("");
    $("#externalRom2").val("");
    $("#doubleVersion2").val("");
    $("#gks2").val("");
    $("#serverPwd2").val("");
}


function deleteServerBatch(){
    var data = layui.table.checkStatus('checkboxTable').data;
    var long = data.length;
    var ids = '';
    if(long <= 0){
        layer.alert('请选择要删除的服务器', {
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

    layer.confirm('您确定要批量删除服务器吗？', {
        btn: ['确认','返回'] //按钮
    }, function(){

        $.ajax({
            type: "POST",
            data: { ids:ids },
            url: "/server/deleteServerBatch",
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
    obj.href = "/server/getImmsServerList/export?data="+$("#userSearch").serialize();
}