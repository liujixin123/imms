/**
 * 菜单
 * */
//获取路径uri
var pathUri=window.location.href;
$(function(){
    layui.use('element', function(){
        var element = layui.element;
        // 左侧导航区域（可配合layui已有的垂直导航）
        $.get("/permission/getUserPerms",function(data){
            if(data!=null){
                console.log(data.perm)
                getMenus(data.perm);
                element.render('nav');
            }else{
                layer.alert("权限不足，请联系管理员",function () {
                    //退出
                    window.location.href="/logout";
                });
            }
        });
    });

    $.ajax({
        type: "POST",
        dataType: "json",
        data: {'pageIndex': 1,'pageSize':1},//请求的页码和每页显示条数
        async: true,
        url: '/msg/news',
        success: function (result) {
            if ( result.rows.length > 0) { //数据插入
                var newsHtml = '';
                layui.each(result.rows, function (index, item) {
                 /*   newsHtml += '<li><a href="/device/devManage" target="_blank" style="color: rgba(255,255,255,.7);">'+ item.title + '</a></li>';*/
                    newsHtml += '<li><a href="javascript:void(0)" onclick="goUrlBatch(\'' + item.url + '\',\'' + item.sid + '\',\'' + item.version+ '\',\''+ item.title+ '\',\''+ item.rid+ '\')"  style="color: rgba(255,255,255,.7);">'+ item.title + '</a></li>';

                });
                $("#news1").html(newsHtml);
                $("#msgNum").html(result.rows.length);
            }else{
                $("#msgNum").html(0);
            }
        }
    });
    var speed=40;
    var news=document.getElementById("news");
    var news2=document.getElementById("news2");
    var news1=document.getElementById("news1");
    news2.innerHTML=news1.innerHTML
    function Marquee(){
        if(news.scrollTop>=news1.offsetHeight){
            news.scrollTop=0;
        }
        else{
            news.scrollTop=news.scrollTop+1;
        }
    }
    var MyMar=setInterval(Marquee,speed)
    news.onmouseover=function(){clearInterval(MyMar)}
    news.onmouseout=function(){MyMar=setInterval(Marquee,speed)}

    $("#newpic").click(function(){
        location.href='/msg/msgManage';
    });
});
var getMenus=function(data){
    //回显选中
    var ul=$("<ul class='layui-nav layui-nav-tree' lay-filter='test'></ul>");
    for(var i=0;i < data.length;i++){
        var node=data[i];
        console.log(node)
        var li=$("<li class='layui-nav-item' flag='"+node.id+"'></li>");
        var a=$("<a style='font-size:16px' class='layui-icon layui-icon-align-left' href='javascript:;' >&nbsp;"+node.name+"</a>");
        li.append(a);
        //获取子节点
        var childArry = node.childrens;
        console.log(childArry);
        if(childArry.length>0){
            a.append("<span class='layui-nav-more'></span>");
            var dl=$("<dl class='layui-nav-child' ></dl>");
            for (var y in childArry) {
                var dd=$("<dd><a style='font-size:12px' href='"+childArry[y].url+"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+childArry[y].name+"</a></dd>");
                //判断选中状态
                if(pathUri.indexOf(childArry[y].url)>0){
                    li.addClass("layui-nav-itemed");
                    dd.addClass("layui-this")
                }
                dl.append(dd);
            }
            li.append(dl);
        }
        ul.append(li);
    }
    $(".layui-side-scroll").append(ul);
}
//根据菜单主键id获取下级菜单
//id：菜单主键id
//arry：菜单数组信息
function getParentArry(id, arry) {
    var newArry = new Array();
    for (var x in arry) {
        if (arry[x].pId == id)
            newArry.push(arry[x]);
    }
    return newArry;
}



function updateUsePwd(){
    layer.open({
        type:1,
        title: "修改密码",
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['450px'],
        content:$('#pwdDiv')
    });

}


function devOpen() {
    var url = "/device/devManage";
/*    layer.open({
        type: 2,
        title: "消息",
        area:  ['500px', '500px'],
        maxmin: true, //开启最大化最小化按钮
        shadeClose: true,
        content: url
    });*/
    window.location.href=url;
}



function goUrlBatch(url, id,version,title,rid) {
    // window.location=url+"?version="+version;
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
        content: url+"?id="+id+"&version="+version
        ,maxmin: true
        ,btn: [ '关闭']
        ,btn1: function(index, layero){
            layer.close(index);
            window.location.reload();
        }

    });
}