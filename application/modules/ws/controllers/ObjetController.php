<?php

class ObjetController
{
    private $_objetMapper;
    private $_return;
    private $jwt;

    public function __construct()
    {
        $this->_objetMapper=new ObjetMapper();
        http_response_code(500);
        $this->_return=[];
        $this->jwt = new JwtToken();
    }

    public function createAction(){
        if(isset($_POST["type"]) && isset($_POST["description"]) && isset($_POST["latitude"])&& isset($_POST["longitude"])){
            $type=$_POST["type"];
            $description=$_POST["description"];
            $latitude=$_POST["latitude"];
            $longitude=$_POST["longitude"];
            $id_user=$this->jwt->giveMePayload()->id_user;
            $geom = $this->_objetMapper->coordToGeomWhithTest($latitude,$longitude);
            if($geom != NULL){
                $objet= new Objet($id_user,$type,$description, $geom);
                $res = $this->_objetMapper->createObjet($objet);
                if(isset($res)){
                    $this->_return["id_objet"]=$res;
                    http_response_code(200);
                }else{
                    $this->_return["msg"]="Erreur lors de la creation de l'objet";
                    http_response_code(500);
                }
            }else{
                $this->_return["msg"]="Erreur lors de la creation du point verifier que l objet est dans l agglo";
                http_response_code(400);
            }
        }else{
            $this->_return["msg"]="1 ou plusieurs parametres absents";
            http_response_code(400);
        }
        echo(json_encode($this->_return));
    }

    public function deleteAction(){

        if(isset($_POST["id_objet"])){
            $id_objet=$_POST["id_objet"];
            $objet=$this->_objetMapper->findByIdObjet($id_objet);
            if($objet != NULL && $objet->getId_User() == $this->jwt->giveMePayload()->id_user){
                $res=$this->_objetMapper->deleteObjet($id_objet);
                if($res){
                    $this->_return["id_objet"]=$id_objet;
                    http_response_code(200);
                }else{
                    $this->_return["msg"]="Probleme lors de la suppression de l objet";
                    http_response_code(404);
                }
            }else{
                $this->_return["msg"]="Objet inexistant";
                http_response_code(404);
            }
        }else{
            $this->_return["msg"]="Parametre id_objet absent";
            http_response_code(400);
        }
        echo(json_encode($this->_return));
    }

    public function selectByIdAction(){
        if(isset($_POST["id_objet"])){
            $id_objet=$_POST["id_objet"];
            $objet=$this->_objetMapper->findByIdObjet($id_objet);
            if($objet != NULL){
                $this->_select($objet);
            }else{
                $this->_return["msg"]="Aucun objet ne possede cet id";
                http_response_code(404);
            }
        }else{
            $this->_return["msg"]="Parametre id_objet absent";
            http_response_code(400);
        }
        echo(json_encode($this->_return));
    }

    public function updateAction(){
        if(isset($_POST["id_objet"]) && isset($_POST["type"]) && isset($_POST["description"])){
            $id_objet=$_POST["id_objet"];
            $newType=$_POST["type"];
            $newDescription=$_POST["description"];
            $id_user = $this->jwt->giveMePayload()->id_user;
            $objet=$this->_objetMapper->findByIdObjet($id_objet);
            if($objet != NULL){
                if($objet->getId_User() == $id_user) {
                    $newObjet= new Objet($id_user,$newType, $newDescription, $objet->getGeom());
                    $newObjet->setId_Object($objet->getId_Object());
                    $res=$this->_objetMapper->updateObjet($newObjet);
                    if($res){
                        $this->_return["msg"]=$id_objet;
                        http_response_code(200);
                    }else{
                        $this->_return["msg"]="Erreur lors de la modification de l objet";
                        http_response_code(404);
                    }
                }else {
                    $this->_return["msg"]="Vous ne pouvez pas modifier un objet que vous n'avez pas cree";
                    http_response_code(403);
                }
            }else{
                $this->_return["msg"]="Objet non trouve !";
                http_response_code(404);
            }
        }else{
            $this->_return["msg"]="1 ou plusieurs parametres absents";
            http_response_code(400);
        }
        echo(json_encode($this->_return));
    }

    private function _select(Objet $objet){
        $this->_return["msg"]="Objet trouve";
        $this->_return["objet"]=array(
            "id_objet"=>$objet->getId_Object(),
            "id_user"=>$objet->getId_User(),
            "type"=>$objet->getType(),
            "description"=>$objet->getDescription(),
            "geom"=>$objet->getGeom()
        );
        http_response_code(200);
    }
}