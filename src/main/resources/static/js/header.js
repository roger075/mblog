$().ready(function () {
    $(".ui.dropdown").dropdown();
    $.ajax({
        url: "/index/getUser",
        type: "post",
        dataType: "json",
        success: function (result) {
            if(result.message == "获取成功"){
                document.getElementById("UserInfo").style.display="block";
                document.getElementById("notLogin").style.display="none";
                $("#UserImg").attr("src",result.headPortrait);
                if(result.informs>0){
                  $("#informNotRead").text(result.informs);
                  document.getElementById('informNotRead').style.display="block";
                }else {
                  document.getElementById('informNotRead').style.display="none";
                }
            } else {
                document.getElementById("notLogin").style.display="block";
                document.getElementById("UserInfo").style.display="none";
            }
        }
    });
});
