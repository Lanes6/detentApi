<?php

class LoginController{
    private $_userMapper;
    private $_return;

    public function __construct()
    {
        $this->_userMapper=new UserMapper();
        http_response_code(500);
        $this->_return=[];
    }

    public function loginAction(){
        if(isset($_POST["login"]) && isset($_POST["password"]) ){
            $login=$_POST["login"];
            $password=$_POST["password"];
            $user=$this->_userMapper->findByLogin($login);
            if($user != NULL && $user->checkPassword($password)){
                $user->generateSecretToken();
                $res = $this->_userMapper->updateUser($user);
                if($res) {
                    $jwtToken = new JwtToken();
                    $this->_return["jwtRefresh"] = $jwtToken->createRefreshToken($user);
                    $this->_return["jwt"] = $jwtToken->createToken($user);
                    http_response_code(200);
                }else{
                    $this->_return["msg"]="Erreur pendant le login";
                    http_response_code(500);
                }
            }else{
                $this->_return["msg"]="identifiants invalides";
                http_response_code(403);
            }
        }else{
            $this->_return["msg"]="1 ou plusieurs parametres absents";
            http_response_code(400);
        }
        echo(json_encode($this->_return));
    }

    public function refreshTokenAction(){
        $headers=apache_request_headers();
        if(isset($headers["Authorization"])) {
            $authorizationHeader = explode(" ", $headers["Authorization"]);
            if (isset($authorizationHeader[1])) {
                $jwtToken = new JwtToken();
                $payload = $jwtToken->verifyToken($authorizationHeader[1]);
                if ($payload != null){
                    $user=$this->_userMapper->findByIdUser($payload->id_user);
                    if($user->getSecret_token()==$payload->secret){
                        $this->_return["jwt"]=$jwtToken->createToken($user);
                        http_response_code(200);
                    }else{
                        $this->_return["msg"]="Acces refuse !!";
                        http_response_code(403);
                    }
                }else{
                    $this->_return["msg"]="Acces refuse !";
                    http_response_code(403);
                }
            }else{
                $this->_return["msg"]="Header Bearer absent";
                http_response_code(400);
            }
        }else{
            $this->_return["msg"]="Header Authorization absent";
            http_response_code(400);
        }
        echo(json_encode($this->_return));
    }

    public function logoutAction(){
        $headers=apache_request_headers();
        if(isset($headers["Authorization"])) {
            $authorizationHeader = explode(" ", $headers["Authorization"]);
            if (isset($authorizationHeader[1])) {
                $jwtToken = new JwtToken();
                $payload = $jwtToken->verifyToken($authorizationHeader[1]);
                if ($payload != null){
                    $user=$this->_userMapper->findByIdUser($payload->id_user);
                    $user->generateSecretToken();
                    $res = $this->_userMapper->updateUser($user);
                    if($res) {
                        $this->_return["id_user"]=$user->getId_User();
                        http_response_code(200);
                    }else{
                        $this->_return["msg"]="Erreur pendant la deconnexion";
                        http_response_code(500);
                    }
                }else{
                    $this->_return["msg"]="Acces refuse !";
                    http_response_code(403);
                }
            }else{
                $this->_return["msg"]="Header Bearer absent";
                http_response_code(400);
            }
        }else{
            $this->_return["msg"]="Header Authorization absent";
            http_response_code(400);
        }
        echo(json_encode($this->_return));
    }
}