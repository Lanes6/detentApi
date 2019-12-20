<?php
class ObjetMapper extends Model{


    private $SRID = 2154;
 
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
        $req=$this->getBdd()->prepare(' UPDATE '.$this->getTable().' SET type=\''.$newObjet->getType().'\', description=\''.$newObjet->getDescription().'\' WHERE id_object=\''.$newObjet->getId_Objet().'\'');
        $req->execute();
        return true;
    }

    public function deleteObjet($id_objet){
        $req=$this->getBdd()->prepare('DELETE FROM '.$this->getTable().' WHERE id_object='.$id_objet);
        $req->execute();
        return true;
    }

    public function getGeomBD(){
        $req=$this->getBdd()->prepare('SELECT geom FROM public.outline WHERE type=?');
        $req->execute(array('contour_agglo'));
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            return array_pop($row);
        }
    }

    public function coordToGeomWhithTest($latitude,$longitude){
        $newGeom;
        $req=$this->getBdd()->prepare('SELECT ST_SetSRID(ST_MakePoint('.$longitude.','.$latitude.'),'.$this->getSrid().')');
        $req->execute();
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            $newGeom = array_pop($row);
        }

        $geom = $this->getGeomBD();

        $req2=$this->getBdd()->prepare('SELECT ST_Contains(?::Geometry,?::Geometry)');
        $req2->execute(array($geom,$newGeom));
       
        while ($row = $req2->fetch(PDO::FETCH_ASSOC)) {
            if(array_pop($row)){
                return $newGeom;
            }
            return null;
        }
    }


    /**
     * @return mixed
     */
    public function getSrid()
    {
        return $this->SRID;
    }
}