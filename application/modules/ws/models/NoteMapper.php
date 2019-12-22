<?php
class NoteMapper extends ModelMapper{

    public function findNoteByUserObject($id_user,$id_object){
        try {
            $req=$this->getBdd()->prepare('SELECT * FROM '.$this->getNoteTable().' WHERE id_user= ? AND id_object= ?');
            $req->execute(array($id_user,$id_object));
            while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
                return new Note($row);
            }
        }catch (Exception $e){
            return null;
        }
        return null;
    }

    public function findNotes($id_object){
        try{
            $req=$this->getBdd()->prepare('SELECT AVG(note) as moyenne FROM '.$this->getNoteTable().' WHERE id_object= ? GROUP BY id_object');
            $req->execute(array($id_object));
            while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
                return $row['moyenne'];
            }
        }catch (Exception $e){
            return null;
        }
        return null;
    }

    public function createNote(Note $note){
        try{
            $req=$this->getBdd()->prepare('INSERT INTO '.$this->getNoteTable().' (id_user, id_object, note) VALUES (?,?,?)');
            $req->execute(array($note->getId_User(), $note->getId_Object(), $note->getNote()));
            return $this->getBdd()->lastInsertId();
        }catch (Exception $e){
            return null;
        }
    }
}