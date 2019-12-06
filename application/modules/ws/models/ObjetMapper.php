<?php
class ObjetMapper extends Model{


    public function __construct()
    {
        $this->setTable('public.object');
    }

    public function findByIdObjet($id_objet){
        $req=$this->getBdd()->prepare('SELECT * FROM '.$this->getTable().' WHERE id_object='.$id_objet);
        $req->execute();
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            return new Objet($row);
        }
        return null;
    }

    public function createObjet(Objet $objet){
        $req=$this->getBdd()->prepare('INSERT INTO '.$this->getTable().'(id_user, type, description, geom) VALUES (\''.$objet->getId_User().'\',\''.$objet->getType().'\', \''.$objet->getDescription().'\', \''.$objet->getGeom().'\')');
        $req->execute();
        return true;
    }

    public function updateObjet(Objet $newObjet){
        $req=$this->getBdd()->prepare(' UPDATE '.$this->getTable().' SET type=\''.$newObjet->getType().'\', description=\''.$newObjet->getDescription().'\', geom=\''.$newObjet->getGeom().'\' WHERE id_objet=\''.$newObjet->getId_Objet().'\' AND id_user=\''.$user->getId_User().'\'');
        $req->execute();
        return true;
    }

    public function deleteObjet($id_objet){
        $req=$this->getBdd()->prepare('DELETE FROM '.$this->getTable().' WHERE id_objet='.$id_objet);
        $req->execute();
        return true;
    }

    public function coordToGeomWhithTest($latitude,$longitude){

        $reqGeom=$this->getBdd()->prepare('SELECT ST_SetSRID(ST_MakePoint('.$latitude.','.$longitude.'),'.$this->getSrid().')');
        $reqGeom->execute();
        $geom = $reqGeom->fetch(PDO::FETCH_ASSOC);
        echo 'Coord to Geom ='.$geom;

        $req=$this->getBdd()->prepare('SELECT ST_Contains('.$geom.',(SELECT ST_SetSRID(ST_MakePoint('.$latitude,$longitude.'),'.$this->getSrid().')) FROM public.contours_agglo;');
        $req->execute();

        return $geom;
    }
}