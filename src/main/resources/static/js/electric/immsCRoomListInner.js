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
            elem: '#immsCRoomList',
            url:'/room/getImmsCRoomList',
            method: 'post', //默认：get请求
            cellMinWidth: 120,
            page: true,
            where:{ sid:$("#sid").val(),version:$("#version").val()},
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
                ,{field:'cabinetName', title:'机柜名称',align:'center'}
                ,{field:'devHeight', title:'设备高度',align:'center'}
                ,{field:'devName', title:'设备名称',align:'center'}
                ,{field:'devManu', title: '制造商',align:'center'}
                ,{field:'devModel', title: '设备型号',align:'center'}
                ,{field:'devNumber', title:'序列号',align:'center'}
                ,{field:'system', title:'所属系统',align:'center'}
                ,{field:'safeArea', title:'安全区',align:'center'}
                ,{field:'maintainLevel', title: '运维等级',align:'center'}
                ,{field:'useTime', title: '投运日期',align:'center'}
                ,{field:'responsibilityDpt', title:'责任处室',align:'center'}
                ,{field:'responsibilityMan', title:'责任人',align:'center'}
                ,{field:'maintainManu', title:'运维厂商',align:'center'}
                ,{field:'maintainMan', title: '运维人员',align:'center'}
                ,{field:'maintainManPhone', title: '联系电话',align:'center'}
                // ,{field:'ip', title: 'IP',align:'center'}
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
                ,{title:'操作',align:'center', toolbar:'#twoBar'}
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
        table.on('tool(devTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'queryTwo'){
                queryTwo(data,data.id);
            }
        });

        //监听提交
        form.on('submit(userSubmit)', function(data){
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

function load(obj){
    //重新加载table
    tableIns.reload({
        where: obj.field
        , page: {
            curr: pageCurr //从当前页码开始
        }
    });
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