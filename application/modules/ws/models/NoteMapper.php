<?php
class NoteMapper extends Model{

    public function __construct()
    {
        $this->setTable('data.note');
    }


    public function findObject($id_object){
        $req=$this->getBdd()->prepare('SELECT * FROM public.object WHERE id_object=\''.$id_object.'\' AND type LIKE \'%_suggestion\'');
        $req->execute();
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            return $row['id_object'];
        }
        return null;
    }

    public function findNoteByUserObject($id_user,$id_object){
        $req=$this->getBdd()->prepare('SELECT * FROM '.$this->getTable().' WHERE id_user=\''.$id_user.'\' AND id_object=\''.$id_object.'\'');
        $req->execute();
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            return new Note($row);
        }
        return null;
    }

    public function findNotes($id_object){
        $req=$this->getBdd()->prepare('SELECT AVG(note) as moyenne FROM '.$this->getTable().' WHERE id_object=\''.$id_object.'\'GROUP BY id_object');
        $req->execute();
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            return $row['moyenne'];
        }
        return null;
    }

    public function createNote(Note $note){
        $req=$this->getBdd()->prepare('INSERT INTO '.$this->getTable().'(id_user,id_object,note) VALUES ('.$note->getId_User().', '.$note->getId_Object().', '.$note->getNote().')');
        $req->execute();
        return true;
    }
}