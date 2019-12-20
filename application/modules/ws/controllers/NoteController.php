<?php

class NoteController
{
    private $_noteMapper;
    private $_return;

    public function __construct()
    {
        $this->_noteMapper=new NoteMapper();
        http_response_code(500);
        $this->_return=[];
    }

    public function createAction(){
       if(isset($_POST["id_object"]) && isset($_POST["note"])){
            $id_object=intval($_POST["id_object"]);
            $note=intval($_POST["note"]);
            $jwtToken = new JwtToken();
            $id_user=$jwtToken->giveMePayload()->id_user;
            $object=$this->_noteMapper->findObject($id_object);
            if(isset($object)){
                $note_bd=$this->_noteMapper->findNoteByUserObject($id_user,$id_object);
                if(!isset($note_bd)){
                    $note_ob = new Note($id_user,$id_object,$note);
                    $res = $this->_noteMapper->createNote($note_ob);
                    $this->_return["id_note"]=$this->_noteMapper->findNoteByUserObject($id_user,$id_object)->getId_Note();
                    http_response_code(200);
                }else{
                    $this->_return["msg"]="Vous avez deja vote";
                    http_response_code(403);  
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

    public function selectAction(){
        if(isset($_POST["id_object"])){
            $id_object=$_POST["id_object"];
            $object=$this->_noteMapper->findObject($id_object);
            if(isset($object)){
                $notes=$this->_noteMapper->findNotes($id_object);
                $this->_return["note"]=number_format($notes,2);
                http_response_code(200);
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
}