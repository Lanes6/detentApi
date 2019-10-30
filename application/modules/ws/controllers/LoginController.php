<?php

class LoginController{
    private $_userMapper;

    public function __construct()
    {
        $this->_userMapper=new UserMapper();
        http_response_code(500);
    }

    public function loginAction(){
        $return=[];
        if(isset($_POST["login"]) && isset($_POST["password"]) ){
            $login=$_POST["login"];
            $password=$_POST["password"];
            $user=$this->_userMapper->findByLogin($login);
            if($user != NULL && password_verify($password,$user->getPassword())){
                $jwtToken= new JwtToken();
                $return["jwtRefresh"]=$jwtToken->createRefreshToken($user);
                $return["jwt"]=$jwtToken->createToken($user);
                $_SESSION["id_user"]=$user->getId_User();
                http_response_code(200);
            }else{
                http_response_code(403);
            }
        }else{
            http_response_code(400);
        }
        echo(json_encode($return));
    }

    /*public function refreshjwtAction(){
        $return=[];
        http_response_code(400);
        $headers=apache_request_headers();
        if(isset($headers["Authorization"])) {
            $authorizationHeader = explode(" ", $headers["Authorization"]);
            if (isset($authorizationHeader[1])) {
                $jwtToken = new JwtToken();
                $payload = $jwtToken->verifyToken($authorizationHeader[1]);
                if ($payload != null){
                    $return["jwt"]=$payload->secret;
                    $token=$jwtToken->refreshJwtToken($payload);
                    if($token!=null){
                        $return["jwt"]=$token;
                        http_response_code(200);
                    }
                }
            }
        }
        echo(json_encode($return));
    }*/
}