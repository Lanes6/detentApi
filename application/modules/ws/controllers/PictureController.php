<?php

class PictureController
{
    private $_pictureMapper;
    private $_return;

    public function __construct()
    {
        $this->_pictureMapper=new PictureMapper();
        http_response_code(500);
        $this->_return=[];
    }

    public function createAction(){
       if(isset($_POST["id_object"]) && isset($_POST["saison"]) && isset($_FILES["file"])){
            $id_object=$_POST["id_object"];
            $saison=$_POST["saison"];
            $file=$_FILES["file"];
            $jwtToken = new JwtToken();
            $id_user=$jwtToken->giveMePayload()->id_user;
            if( $file['type'] == "image/jpeg" or $file['type'] == "image/png" ){
                $object=$this->_pictureMapper->findObject($id_object);
                if(isset($object)){
                    $arbre=$this->_pictureMapper->findArbre($id_object);
                    if(isset($arbre)){
                        $data = pg_escape_bytea(file_get_contents($file['tmp_name']));
                        $picture= new Picture($id_user,$id_object,$saison,$data);
                        $res = $this->_pictureMapper->createPicture($picture);
                        $this->_return["id_picture"]=$this->_pictureMapper->findPictureByUserObject($id_user,$id_object,$data)->getId_Picture();
                        file_put_contents("image1.jpg", file_get_contents($file['tmp_name']));
                        http_response_code(200);
                    }else{
                        $this->_return["msg"]="L'object n'est pas un arbre";
                        http_response_code(404);  
                    }
                }else{
                    $this->_return["msg"]="Aucun object correspondant";
                    http_response_code(404);  
                }
            }else{
                $this->_return["msg"]="Fichier incompatible";
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
                $this->_return["id_picture"]=$picture->getId_Picture();
                $this->_return["id_user"]=$picture->getId_User();
                $this->_return["id_object"]=$picture->getId_Object();
                $this->_return["saison"]=$picture->getSaison();
                header('Content-type: image/jpeg');
                echo pg_unescape_bytea($picture->getFile());
                file_put_contents("image2.jpg",pg_unescape_bytea($picture->getFile()));
                $this->_return["file"]='a';
                http_response_code(200);
            }else{
                $this->_return["msg"]="Aucun photo correspondant";
                http_response_code(404);
            }
       }else{
            $this->_return["msg"]="1 ou plusieurs parametres absents";
            http_response_code(400);
       }
       echo(json_encode($this->_return));
    }

    public function selectByObjectAction(){
       /*if(isset($_POST["id_object"])){
            $id_object=$_POST["id_object"];
            $pictures=$this->_pictureMapper->findPictures($id_object);
            if($pictures != NULL){
                $this->_return["id_object"]=$picture->getId_Object();
                http_response_code(200);
            }else{
                $this->_return["msg"]="Aucun photo correspondant";
                http_response_code(404);
            }
       }else{
            $this->_return["msg"]="1 ou plusieurs parametres absents";
            http_response_code(400);
       }
       echo(json_encode($this->_return));*/
    }
}