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
                    $this->_return["msg"]=$user->getLogin();
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
        if(isset($_POST["login"])){
            $login=$_POST["login"];
            $user=$this->_userMapper->findByLogin($login);
            if($user != NULL){
                $this->_userMapper->deleteUser($user->getId_User());
                $this->_return["msg"]=$user->getLogin()." supprime";
                http_response_code(200);
            }else{
                $this->_return["msg"]="Aucun utilisateur ne possede ce login";
                http_response_code(200);
            }
        }else{
            $this->_return["msg"]="1 ou plusieurs parametres absents";
            http_response_code(400);
        }
        echo(json_encode($this->_return));
    }

    public function selectByLoginAction(){
        if(isset($_POST["login"])){
            $login=$_POST["login"];
            $user=$this->_userMapper->findByLogin($login);
            if($user != NULL){
                $this->_select($user);
            }else{
                $this->_return["msg"]="Aucun utilisateur ne possede ce login";
                $this->_return["user"]=null;
                http_response_code(200);
            }
        }else{
            $this->_return["msg"]="1 ou plusieurs parametres absents";
            http_response_code(400);
        }
        echo(json_encode($this->_return));
    }

    public function selectByMailAction(){
        if(isset($_POST["mail"])){
            $mail=$_POST["mail"];
            $user=$this->_userMapper->findByMail($mail);
            if($user != NULL){
                $this->_select($user);
            }else{
                $this->_return["msg"]="Aucun utilisateur ne possede ce mail";
                $this->_return["user"]=null;
                http_response_code(200);
            }
        }else{
            $this->_return["msg"]="1 ou plusieurs parametres absents";
            http_response_code(400);
        }
        echo(json_encode($this->_return));
    }

    public function updateAction(){
        if(isset($_POST["oldLogin"]) && isset($_POST["newLogin"]) && isset($_POST["oldPassword"]) && isset($_POST["newPassword"]) && isset($_POST["newMail"])){
            $user=$this->_userMapper->findByLogin($_POST["oldLogin"]);
            if($user != NULL && $user->checkPassword($_POST["oldPassword"])){
                $newUser= new User($_POST["newLogin"],$_POST["newMail"],$_POST["newPassword"]);
                $newUser->setId_User($user->getId_User());

                $this->_userMapper->updateUser($newUser);
                $this->_return["msg"]=$newUser->getLogin();
            }else{
                $this->_return["msg"]="Identifiants invalides";
                http_response_code(200);
            }
        }else{
            $this->_return["msg"]="1 ou plusieurs parametres absents";
            http_response_code(400);
        }
        echo(json_encode($this->_return));
    }

    private function _select(User $user){
        $this->_return["msg"]="Utilisateur trouve";
        $this->_return["user"]=array(
            "login"=>$user->getLogin(),
            "mail"=>$user->getMail()
        );
        http_response_code(200);
    }
}