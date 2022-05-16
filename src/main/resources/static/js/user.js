let index = {
    init: function(){
        $("#btn-save").on("click", ()=>{ // function(){} : this를 바인딩 하지 못함, ()=>{}: this를 바인딩 가능
            this.save();
        });

         $("#btn-login").on("click", ()=>{ // function(){} : this를 바인딩 하지 못함, ()=>{}: this를 바인딩 가능
             this.login();
         });
    },

    save: function(){
        let data = {
            username: $("#username").val(),
            password: $("#password").val(),
            email : $("#email").val()
        }

        console.log(data);
        $.ajax({
            type: "POST",
            url: "/blog/api/user",
            data: JSON.stringify(data), //  http body 데이터 ( javascript 오브젝트 데이터를 이해하지 못하므로 문자열로 변경해서 보낸다)
            contentType: "application/json; charset=utf-8", // body 데이터가 어떤 타입인지(MIME)
           // dataType: "json"  // default 값이 json  // 요청에 대한 응답이 서버로부터 왔을때 기본적으로 모든것이 문자열(생긴게 json이라면) => javascript 오브젝트로 변경
        }).done(function(resp){
            if(resp.data == 1){
                alert("회원가입이 완료 되었습니다." + resp);
                console.log(resp);
            }else{
                alert(JSON.stringify(resp));
            }
         //   location.href = "/blog";
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },

    login: function(){
            let data = {
                username: $("#username").val(),
                password: $("#password").val()
            }

            $.ajax({
                type: "POST",
                url: "/blog/api/user/login",
                data: JSON.stringify(data), //  http body 데이터 ( javascript 오브젝트 데이터를 이해하지 못하므로 문자열로 변경해서 보낸다)
                contentType: "application/json; charset=utf-8", // body 데이터가 어떤 타입인지(MIME)
               // dataType: "json"  // default 값이 json  // 요청에 대한 응답이 서버로부터 왔을때 기본적으로 모든것이 문자열(생긴게 json이라면) => javascript 오브젝트로 변경
            }).done(function(resp){
                if(resp.data == 1){
                    alert("로그인이 완료 되었습니다." + resp);
                    console.log(resp);
                }else{
                    console.log(resp);
                 //   alert(JSON.stringify(resp));
                }
               location.href = "/blog";
            }).fail(function(error){
            console.log(error);
                alert(JSON.stringify(error));
            });
        }
}

index.init();