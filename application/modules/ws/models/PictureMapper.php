<?php
class PictureMapper extends Model{

    public function __construct()
    {
        $this->setTable('data.picture');
    }

    public function findObject($id_object){
        $req=$this->getBdd()->prepare('SELECT * FROM public.object WHERE id_object = \''.$id_object.'\'');
        $req->execute();
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
             return $row['id_object'];
        }
        return null;
    }

    public function findArbre($id_object){
        $req=$this->getBdd()->prepare('SELECT * FROM public.object WHERE id_object = \''.$id_object.'\' AND ( type = \'tree\' OR type = \'tree_report\')');
        $req->execute();
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
             return $row['id_object'];
        }
        return null;
    }

    public function findPictureByUserObject($id_user,$id_object,$data){
        $req=$this->getBdd()->prepare('SELECT * FROM '.$this->getTable().' WHERE id_user = \''.$id_user.'\' AND id_object = \''.$id_object.'\' AND file = \''.$data.'\'');
        $req->execute();
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            return new Picture($row);
        }
        return null;
    }

    public function findPicture($id_picture){
        $req=$this->getBdd()->prepare('SELECT * FROM '.$this->getTable().' WHERE id_picture=\''.$id_picture.'\'');
        $req->execute();
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            return new Picture($row);
        }
        return null;
    }

    public function findPictures($id_object){
        $req=$this->getBdd()->prepare('SELECT id_picture as id FROM '.$this->getTable().' WHERE id_object=\''.$id_object.'\'');
        $req->execute();
        $res = array();
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            array_push($res,$row['id']);
        }
        if(count($res)!=0){
            return $res;
        }else{
            return null;
        }
    }

    public function createPicture(Picture $picture){
        $req=$this->getBdd()->prepare('INSERT INTO '.$this->getTable().'(id_user,id_object,saison,file,name) VALUES (\''.$picture->getId_User().'\', \''.$picture->getId_Object().'\', \''.$picture->getSaison().'\', \''.$picture->getFile().'\',\''.$picture->getName().'\')');
        $req->execute();
        return true;
    }
}