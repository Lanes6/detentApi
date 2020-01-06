<?php

class ReportController
{
    private $_reportMapper;
    private $_objetManager;
    private $_return;

    public function __construct()
    {
        $this->_objetManager = new ObjetMapper();
        $this->_reportMapper=new ReportMapper();
        http_response_code(500);
        $this->_return=[];
    }

    public function createAction(){
        if(isset($_POST["id_objet"]) && isset($_POST["description"])){
            $id_objet=intval($_POST["id_objet"]);
            $description=$_POST["description"];
            $jwtToken = new JwtToken();
            $id_user=$jwtToken->giveMePayload()->id_user;
            $objet=$this->_objetManager->findByIdObjet($id_objet);
            if(isset($objet) && !strpos($objet->getType(), 'suggestion')){
                $oldReport = $this->_reportMapper->findReObject($id_objet);
                if(!isset($oldReport)){
                    $report = new Report($id_user,$id_objet,$description);
                    $res = $this->_reportMapper->createReport($report);
                    if(isset($res)){
                        $this->_return["id_report"]=$res;
                        http_response_code(200);
                    }else{
                        $this->_return["msg"]="Erreur lors de la creation du report";
                        http_response_code(500);
                    }
                }else{
                    $this->_return["msg"]="objet deja report";
                    http_response_code(400);
                }
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
                $res = $this->_reportMapper->deleteReport($id_report);
                if($res){
                    $this->_return["id_report"]=$id_report;
                    http_response_code(200);
                }else{
                    $this->_return["msg"]="Erreur lors de la supression du report";
                    http_response_code(500);
                }
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

    public function selectByIdReportAction(){
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

    public function selectByIdObjetAction(){
        if(isset($_POST["id_objet"])){
            $id_objet=$_POST["id_objet"];
            $report=$this->_reportMapper->findReobject($id_objet);
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

    private function _select(Report $report){
        $this->_return["id_report"]=$report->getId_Report();
        $this->_return["id_objet"]=$report->getId_object();
        $this->_return["id_user"]=$report->getId_User();
        $this->_return["description"]=$report->getDescription();
        http_response_code(200);
    }
}