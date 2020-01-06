<?php

class NoteController
{
    private $_objetMapper;
    private $_noteMapper;
    private $_return;

    public function __construct()
    {
        $this->_objetMapper = new ObjetMapper();
        $this->_noteMapper = new NoteMapper();
        http_response_code(500);
        $this->_return=[];
    }

    public function createAction(){
        if(isset($_POST["id_objet"]) && isset($_POST["note"])){
            $id_objet=intval($_POST["id_objet"]);
            $note=intval($_POST["note"]);
            $jwtToken = new JwtToken();
            $id_user=$jwtToken->giveMePayload()->id_user;
            $objet=$this->_objetMapper->findByIdObjet($id_objet);
            if((Double)$note >=0.0 && (Double)$note <=5.0) {
                if (isset($objet) && strpos($objet->getType(), 'suggestion')) {
                    $note_bd = $this->_noteMapper->findNoteByUserObject($id_user, $id_objet);
                    if (!isset($note_bd)) {
                        $note_ob = new Note($id_user, $id_objet, $note);
                        $res = $this->_noteMapper->createNote($note_ob);
                        if(isset($res)){
                            $this->_return["id_note"] = $res;
                            http_response_code(200);
                        }else{
                            $this->_return["msg"]="Erreur lors de la creation de la note";
                            http_response_code(500);
                        }
                    } else {
                        $this->_return["msg"] = "Vous avez deja vote";
                        http_response_code(403);
                    }
                } else {
                    $this->_return["msg"] = "Aucun objet correspondant";
                    http_response_code(404);
                }
            }else{
                $this->_return["msg"]="Une note doit etre entre 0 et 5";
                http_response_code(400);
            }
        }else{
            $this->_return["msg"]="1 ou plusieurs parametres absents";
            http_response_code(400);
        }
        echo(json_encode($this->_return));
    }

    public function selectAction(){
        if(isset($_POST["id_objet"])){
            $id_objet=$_POST["id_objet"];
            $objet=$this->_objetMapper->findByIdObjet($id_objet);
            if(isset($objet) && strpos($objet->getType(), 'suggestion')){
                $notes=$this->_noteMapper->findNotes($id_objet);
                if(isset($notes)){
                    $this->_return["note"]=number_format($notes,2);
                    http_response_code(200);
                }else{
                    $this->_return["msg"]="Pas de note";
                    $this->_return["note"]=-1;
                    http_response_code(200);
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
}