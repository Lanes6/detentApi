<?php

class MapAcess
{
    private $_mapAcess;

    public function __construct()
    {
        $this->_mapAcess = array(
            "ws" => array()
        );
        $this->_mapAcess["ws"] = array(
            "IndexController" => array(),
            "UserController" => array(),
            "LoginController" => array()
        );
        $this->_mapAcess["ws"]["IndexController"] = array(
            "indexAction"=>0,
            "testComposerAction"=>0,
            "testJWTAction"=>1,
            "giveMeMyIdAction"=>1,
            "testDBAction"=>0
        );         
        $this->_mapAcess["ws"]["LoginController"] = array(
            "loginAction"=>0,
            "logoutAction"=>1,
            "refreshTokenAction"=>0
        );
        $this->_mapAcess["ws"]["UserController"] = array(
            "createAction"=>0,
            "updateAction"=>1,
            "deleteAction"=>1,
            "selectByIdUserAction"=>1
        );
        $this->_mapAcess["ws"]["ObjetController"] = array(
            "createAction"=>1,
            "updateAction"=>1,
            "deleteAction"=>1,
            "selectByIdAction"=>1
        );
        $this->_mapAcess["ws"]["NoteController"] = array(
            "createAction"=>1,
            "selectAction"=>1
        );
        $this->_mapAcess["ws"]["ReportController"] = array(
            "createAction"=>1,
            "deleteAction"=>1,
            "selectByIdAction"=>1,
            "selectByObjetAction"=>1
        );
        $this->_mapAcess["ws"]["PictureController"] = array(
            "createAction"=>1,
            "selectByIdAction"=>1,
            "selectByObjectAction"=>1
        );
    }

    //return l'accès du triplet module-controller-action
    function getAcces($module, $controller, $action){
        return $this->_mapAcess[$module][$controller][$action];
    }

    function exist($module, $controller, $action){
        if(array_key_exists($module,$this->_mapAcess) && array_key_exists($controller,$this->_mapAcess[$module]) && array_key_exists($action,$this->_mapAcess[$module][$controller])){
            return true;
        }
        return false;
    }
}