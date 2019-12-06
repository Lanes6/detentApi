<?php

class ObjectController
{
    private $_objectMapper;
    private $_return;
    private $jwt;

    public function __construct()
    {
        $this->_objectMapper=new ObjectMapper();
        http_response_code(500);
        $this->_return=[];
        $this->jwt = new JwtToken();
    }

    public function ctlToken(){
        //$this->jwt = new JwtToken();
        return ($jwt->giveMePayload() != NULL);
    }
    
    

    public function createAction(){
        $type=$_POST["type"];
        $description=$_POST["description"];
        $latitude=$_POST["latitude"];
        $longitude=$_POST["longitude"];

        if(isset($id_user) && isset($type) && isset($description) && isset($latitude)&& isset($latitude)){
            if(ctlToken()){
                $id_user=$jwt->giveMePayload()->id_user;
            }else{
                $this->_return["msg"]="Vous n'êtes pas connecté !";
                http_response_code(400);
            }
            $geom = $this->$this->_objectMapper->coordToGeomWhithTest($latitude,$longitude);
            //$testoord=$this->ObjectMapper->testCoordGeom($geom);
            if($geom != NULL){
                $objet= new Objet($id_user,$type,$description, $geom);
                $res = $this->_objetMapper->createObjet($objet);
                if($res){
                    $this->_return["msg"]="Le nouveau objet ".$objet->getType()." à été bien créé !";
                    http_response_code(200);
                }else{
                    $this->_return["msg"]="Erreur lors de la creation de l'objet";
                    http_response_code(500);
                }
            }else{
                $this->_return["msg"]="Coordonnées de l'objet en dehors de l'agglomération";
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
            if(ctlToken()){
                $id_user=$jwt->giveMePayload()->id_user;
            }else{
                $this->_return["msg"]="Vous n'êtes pas connecté !";
                http_response_code(400);
            }
            $id_objet=$_POST["id_objet"];
            $objet=$this->_objetMapper->findByIdgin($id_objet);
            if($objet != NULL){
                $res=$this->_objetMapper->deleteObjet($id_objet);
                if($res){
                    $this->_return["msg"]=$objet->getType()." à été bien supprime";
                    http_response_code(200);
                }else{
                    $this->_return["msg"]="Problème lors de la suppression de l'objet";
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
                if(ctlToken()){
                    $id_user=$jwt->giveMePayload()->id_user;
                }else{
                    $this->_return["msg"]="Vous n'êtes pas connecté !";
                    http_response_code(400);
                }
            $id_objet=$_POST["id_objet"];
            $objet=$this->_objetMapper->findById($id_objet);;
            if($objet != NULL){
                $this->_select($objet);
            }else{
                $this->_return["msg"]="Aucun objet ne possede cet id";
                $this->_return["objet"]=null;
                http_response_code(404);
            }
        }else{
            $this->_return["msg"]="Parametre id_objet absent";
            http_response_code(400);
        }
        echo(json_encode($this->_return));
    }


    public function updateAction(){

        $id_objet=$_POST["id_objet"];
        $id_user=$_POST["id_user"];
        $newType=$_POST["newType"];
        $newDescription=$_POST["newDescription"];
        $newGeom=$_POST["newGeom"];

        if(isset($id_objet) && isset($id_user) && isset($newType) && isset($newDescription) && isset($newGeom)){
            if(ctlToken()){
                $id_user=$jwt->giveMePayload()->id_user;
            }else{
                $this->_return["msg"]="Vous n'êtes pas connecté !";
                http_response_code(400);
            }

            $objet=$this->_objetMapper->findByIdgin($id_objet);
            if($objet != NULL){
                if($objet->getId_User() == $id_user){
                    $newObjet= new Objet($id_user,$newType, $newDescription, $newGeom);
                    //$newObjet->setId_Objet($objet->getId_Objet());
                    $res=$this->_objetMapper->updateObjet($newObjet);
                    if($res){
                        $this->_return["msg"]="L'objet ".$newObjet->getType()." à été bien modifié !";
                        http_response_code(200);
                    }else{
                        $this->_return["msg"]="Erreur lors de la modification de l'objet";
                        http_response_code(404);
                    }
                }else{
                    $this->_return["msg"]="Vous ne pouvez pas modifier un objet que vous n'avez pas créé";
                    http_response_code(403);
                }
            }else{
                $this->_return["msg"]="Oups: Objet non trouvé !";
                http_response_code(404);
            }
        }else{
            $this->_return["msg"]="1 ou plusieurs parametres absents";
            http_response_code(400);
        }
        echo(json_encode($this->_return));
    }

    private function _select(Objet $objet){
        $this->_return["msg"]="Objet trouvé";
        $this->_return["objet"]=array(
            "id_objet"=>$objet->getId_Objet(),
            "id_user"=>$objet->getId_User(),
            "type"=>$objet->getType(),
            "description"=>$objet->getDescription(),
            "geom"=>$objet->getGeom()
        );
        http_response_code(200);
    }
}