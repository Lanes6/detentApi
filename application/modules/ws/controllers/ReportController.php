<?php

class ReportController
{
    private $_reportMapper;
    private $_return;

    public function __construct()
    {
        $this->_reportMapper=new ReportMapper();
        http_response_code(500);
        $this->_return=[];
    }
    
    public function createAction(){
       if(isset($_POST["id_object"]) && isset($_POST["description"])){
            $id_object=intval($_POST["id_object"]);
            $description=$_POST["description"];
            $jwtToken = new JwtToken();
            $id_user=$jwtToken->giveMePayload()->id_user;
            $object=$this->_reportMapper->findObject($id_object);
            if(isset($object)){
                $report = new Report($id_user,$id_object,$description);
                $res = $this->_reportMapper->createReport($report);
                $this->_return["id_report"]=$this->_reportMapper->findReportByUserObject($id_user,$id_object)->getId_Report();
                http_response_code(200);
            }else{
                $this->_return["msg"]="Aucun objet correspondant";
                http_response_code(404);  
            }
       }else{
            $this->_return["msg"]="1 ou plusieurs parametres absents";
            http_response_code(400);
       }
       echo(json_encode($this->_return));
    }

    public function deleteAction(){
        if(isset($_POST["id_report"])){
            $id_report=$_POST["id_report"];
            $report=$this->_reportMapper->findReport($id_report);
            if($report != NULL){
                $this->_reportMapper->deleteReport($id_report);
                $this->_return["id_report"]=$id_report;
                http_response_code(200);
            }else{
                $this->_return["msg"]="Aucun report correspondant";
                http_response_code(404);
            }
        }else{
            $this->_return["msg"]="1 ou plusieurs parametres absents";
            http_response_code(400);
        }
        echo(json_encode($this->_return));
    }

    public function selectByIdAction(){
        if(isset($_POST["id_report"])){
            $id_report=$_POST["id_report"];
            $report=$this->_reportMapper->findReport($id_report);
            if($report != NULL){
                $this->_select($report);
            }else{
                $this->_return["msg"]="Aucun report correspondant";
                http_response_code(404);
            }
       }else{
            $this->_return["msg"]="1 ou plusieurs parametres absents";
            http_response_code(400);
       }
       echo(json_encode($this->_return));
    }

    public function selectByObjectAction(){
       if(isset($_POST["id_object"])){
            $id_object=$_POST["id_object"];
            $jwtToken = new JwtToken();
            $id_user=$jwtToken->giveMePayload()->id_user;
            $object=$this->_reportMapper->findReObject($id_object);
            if($object != NULL){
                $report=$this->_reportMapper->findReportByUserObject($id_user,$id_object);
                $this->_select($report);
            }else{
                $this->_return["msg"]="Aucun object correspondant";
                http_response_code(404);
            }
       }else{
            $this->_return["msg"]="1 ou plusieurs parametres absents";
            http_response_code(400);
       }
       echo(json_encode($this->_return));
    }

    private function _select(Report $report){
        $this->_return["id_report"]=$report->getId_Report();
        $this->_return["id_object"]=$report->getId_Object();
        $this->_return["id_user"]=$report->getId_User();
        $this->_return["description"]=$report->getDescription();
        http_response_code(200);
    }
}