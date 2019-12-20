<?php

class UserController
{
    private $_userMapper;
    private $_return;

    public function __construct()
    {
        $this->_userMapper=new UserMapper();
        http_response_code(500);
        $this->_return=[];
    }

    public function createAction(){
        if(isset($_POST["login"]) && isset($_POST["password"]) && isset($_POST["mail"]) ){
            $login=$_POST["login"];
            $mail=$_POST["mail"];
            $password=$_POST["password"];
            $userLogin=$this->_userMapper->findByLogin($login);
            $userMail=$this->_userMapper->findByMail($mail);
            if($userLogin == NULL && $userMail == NULL){
                $user= new User($login,$mail,$password);
                $res = $this->_userMapper->createUser($user);
                if($res){
                    $this->_return["id_user"]=$this->_userMapper->findByLogin($login)->getId_User();
                    http_response_code(200);
                }else{
                    $this->_return["msg"]="Erreur lors de la creation de l utilisateur";
                    http_response_code(500);
                }
            }else{
                $this->_return["msg"]="Identifiants deja presents dans la base";
                http_response_code(403);
            }
        }else{
            $this->_return["msg"]="1 ou plusieurs parametres absents";
            http_response_code(400);
        }
        echo(json_encode($this->_return));
    }

    public function deleteAction(){
        $jwt = new JwtToken();
        $id_user=$jwt->giveMePayload()->id_user;
        $user=$this->_userMapper->findByIdUser($id_user);
        if($user != NULL){
            $res = $this->_userMapper->deleteUser($user->getId_User());
            if($res){
                $this->_return["id_user"]=$user->getId_User();
                http_response_code(200);
            }else{
                $this->_return["msg"]="Erreur lors de la supression de l utilisateur";
                http_response_code(500);
            }
        }else{
            $this->_return["msg"]="Aucun utilisateur ne possede cet id_user";
            http_response_code(404);
        }
        echo(json_encode($this->_return));
    }

    public function selectByIdUserAction(){
        $jwt = new JwtToken();
        $id_user=$jwt->giveMePayload()->id_user;
        $user=$this->_userMapper->findByIdUser($id_user);
        if($user != NULL){
            $this->_return["id_user"]=$user->getId_User();
            $this->_return["login"]=$user->getLogin();
            $this->_return["mail"]=$user->getMail();
            http_response_code(200);
        }else{
            $this->_return["msg"]="Aucun utilisateur ne possede cet id_user";
            http_response_code(404);
        }
        echo(json_encode($this->_return));
    }

    public function updateAction(){
        if(isset($_POST["newLogin"]) && isset($_POST["oldPassword"]) && isset($_POST["newPassword"]) && isset($_POST["newMail"])){
            $jwt = new JwtToken();
            $id_user=$jwt->giveMePayload()->id_user;
            $user=$this->_userMapper->findByIdUser($id_user);
            if($user != NULL && $user->checkPassword($_POST["oldPassword"])){
                $newUser= new User($_POST["newLogin"],$_POST["newMail"],$_POST["newPassword"]);
                $newUser->setId_User($user->getId_User());
                $res = $this->_userMapper->updateUser($newUser);
                if(res){
                    $this->_return["id_user"]=$newUser->getId_User();
                    http_response_code(200);
                }else{
                    $this->_return["msg"]="Erreur lors de la modification de l utilisateur";
                    http_response_code(500);
                }
            }else{
                $this->_return["msg"]="Identifiants invalides";
                http_response_code(403);
            }
        }else{
            $this->_return["msg"]="1 ou plusieurs parametres absents";
            http_response_code(400);
        }
        echo(json_encode($this->_return));
    }
}