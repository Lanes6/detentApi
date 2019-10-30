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
            "IndexController" => array()
        );
        $this->_mapAcess["ws"]["IndexController"] = array(
            "indexAction"=>0,
            "testComposerAction"=>0,
            "testDBAction"=>0
        );                
    }

    //return l'accès du triplet module-controller-action
    function getAcces($module, $controller, $action){
        return $this->_mapAcess[$module][$controller][$action];
    }
}