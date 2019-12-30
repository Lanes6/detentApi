<?php
class PictureMapper extends ModelMapper{
    
    public function findPicture($id_picture){
        try {
        $req=$this->getBdd()->prepare('SELECT * FROM '.$this->getPictureTable().' WHERE id_picture= ?');
        $req->execute(array($id_picture));
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            return new Picture($row);
        }
            return null;
        }catch (Exception $e){
            return null;
        }
    }

    public function findPictures($id_object){
        $res = array();
        $req=$this->getBdd()->prepare('SELECT id_picture FROM '.$this->getPictureTable().' WHERE id_object= ?');
        $req->execute(array($id_object));
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            array_push($res,$row);
        }
        return $res;
    }

   public function createPicture(Picture $picture){
       try {
           $req=$this->getBdd()->prepare('INSERT INTO '.$this->getPictureTable().'(id_user,id_object,saison,file,name) VALUES (\''.$picture->getId_User().'\', \''.$picture->getId_Object().'\', \''.$picture->getSaison().'\', \''.$picture->getFile().'\',\''.$picture->getName().'\')');
           $req->execute();
           return $this->getBdd() -> lastInsertId();
       } catch (Exception $e) {
           return $e->getMessage();
       }
    }
}