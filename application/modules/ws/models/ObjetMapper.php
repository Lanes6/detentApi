<?php
class ObjetMapper extends ModelMapper{

    private $SRID = 2154;

    public function findByIdObjet($id_object){
        try {
            $req = $this->getBdd()->prepare('SELECT * FROM '.$this->getObjectTable().' WHERE id_object=?');
            $req->execute(array( $id_object));
            while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
                return new Objet($row);
            }
            return null;
        }catch (Exception $e){
            return null;
        }
    }

    public function createObjet(Objet $objet){
        try {
            $req=$this->getBdd()->prepare('INSERT INTO '.$this->getObjectTable().' (id_user, type, description, geom) VALUES (?,?,?,?)');
            $req->execute(array($objet->getId_User(),$objet->getType(),$objet->getDescription(),$objet->getGeom()));
            return $this->getBdd() -> lastInsertId();
        }catch (Exception $e){
            return null;
        }
    }

    public function updateObjet(Objet $newObjet){
        try {
            $req = $this->getBdd()->prepare('UPDATE '.$this->getObjectTable().' SET type= ?, description= ? WHERE id_object= ?');
            $req->execute(array($newObjet->getType(), $newObjet->getDescription(), $newObjet->getId_Object()));
            return true;
        }catch (Exception $e){
            return false;
        }
    }

    public function deleteObjet($id_object){
        try {
            $req=$this->getBdd()->prepare('DELETE FROM '.$this->getObjectTable().' WHERE id_object=?');
            $req->execute(array($id_object));
            return true;
        }catch (Exception $e){
            return false;
        }
    }

    public function getContourAgloGeom(){
        try {
            $req = $this->getBdd()->prepare('SELECT geom FROM '.$this->getOutlineTable().' WHERE type=\'contour_agglo\'');
            $req->execute();
            while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
                return array_pop($row);
            }
            return null;
        }catch (Exception $e){
            return null;
        }
    }

    public function coordToGeomWhithTest($latitude,$longitude){
        try {
            $geomNewObjet = null;
            $req = $this->getBdd()->prepare('SELECT ST_SetSRID(ST_MakePoint(?,?),?)');
            $req->execute(array($longitude,$latitude,$this->SRID));
            while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
                $geomNewObjet = array_pop($row);
            }
            $geomContourAgglo = $this->getContourAgloGeom();
            if($geomNewObjet == null || $geomContourAgglo == null){
                return null;
            }
            $req=$this->getBdd()->prepare('SELECT ST_Contains(?::geometry,?::geometry)');
            $req->execute(array($geomContourAgglo,$geomNewObjet));
            while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
                if (array_pop($row)){
                    return $geomNewObjet;
                }
            }
            return null;
        }catch (Exception $e){
            return null;
        }
    }
}