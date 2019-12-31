<?php

class PictureController
{
    private $_pictureMapper;
    private $_objetMapper;
    private $_return;

    public function __construct()
    {
        $this->_objetMapper = new ObjetMapper();
        $this->_pictureMapper=new PictureMapper();
        http_response_code(500);
        $this->_return=[];
    }

    public function createAction(){
        if(isset($_POST["id_objet"]) && isset($_POST["saison"]) && isset($_FILES["file"])){
            $id_objet=$_POST["id_objet"];
            $saison=$_POST["saison"];
            $file=$_FILES["file"];
            $jwtToken = new JwtToken();
            $id_user = $jwtToken->giveMePayload()->id_user;
            $type = end(explode(".", $file["name"]));
            if($type == "jpeg" or $type == "jpg" or $type == "png"){
                $objet=$this->_objetMapper->findByIdObjet($id_objet);
                if(isset($objet)){
                    if ($objet->getType() == 'tree' && !strpos($objet->getType(), 'suggestion')) {
                        $now = date('Y-m-d H:i:s');
                        $name = str_replace(array(" ", ".", "-", ":"), "_", $now);
                        $name .= ".".$type;
                        $data = pg_escape_bytea(file_get_contents($file['tmp_name']));
                        $picture = new Picture($id_user, $id_objet, $saison, $data, $name);
                        $res = $this->_pictureMapper->createPicture($picture);
                        if (isset($res)) {
                            $this->_return["id_picture"] = $res;
                            http_response_code(200);
                        } else {
                            $this->_return["msg"] = "Erreur lors de la creation de l'image";
                            http_response_code(500);
                        }
                    } else {
                        $this->_return["msg"] = "L'objet n'est pas un arbre";
                        http_response_code(404);
                    }
                }else{
                    $this->_return["msg"]="Aucun object correspondant";
                    http_response_code(404);
                }
            }else{
                //$this->_return["msg"]="Fichier incompatible";
                $this->_return["msg"]=$file['name'];
                http_response_code(400);
            }
        }else{
            $this->_return["msg"]="1 ou plusieurs parametres absents";
            http_response_code(400);
        }
        echo(json_encode($this->_return));
    }

    public function selectByIdAction(){
        if(isset($_POST["id_picture"])){
            $id_picture=$_POST["id_picture"];
            $picture=$this->_pictureMapper->findPicture($id_picture);
            if($picture != NULL){
                http_response_code(200);
                $this->_return["id_picture"]=$picture->getId_Picture();
                $this->_return["id_user"]=$picture->getId_User();
                $this->_return["id_objet"]=$picture->getId_Object();
                $this->_return["saison"]=$picture->getSaison();
                $temp = stream_get_contents($picture->getFile());
                file_put_contents("tempP/".$picture->getName(),$temp);
                $this->_return["file"]="tempP/".$picture->getName();
                http_response_code(200);
            }else{
                $this->_return["msg"]="Aucune photo correspondante";
                http_response_code(404);
            }
        }else{
            $this->_return["msg"]="1 ou plusieurs parametres absents";
            http_response_code(400);
        }
        echo(json_encode($this->_return));
    }

    public function selectByObjetAction(){
        if(isset($_POST["id_objet"])){
            $id_objet=$_POST["id_objet"];
            $pictures=$this->_pictureMapper->findPictures($id_objet);
            if($pictures != NULL){
                $index = 1;
                foreach ($pictures as $key => $value) {
                    $this->_return["id_picture_".$index]=$value;
                    $index++;
                }
                http_response_code(200);
            }else{
                $this->_return["msg"]="L'arbre ne possède pas de photo";
                http_response_code(404);
            }
        }else{
            $this->_return["msg"]="1 ou plusieurs parametres absents";
            http_response_code(400);
        }
        echo(json_encode($this->_return));
    }
}