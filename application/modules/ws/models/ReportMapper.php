<?php
class ReportMapper extends ModelMapper{

    public function findReport($id_report){
        try {
            $req=$this->getBdd()->prepare('SELECT * FROM '.$this->getReportTable().' WHERE id_report= ?');
            $req->execute(array($id_report));
            while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
                return new Report($row);
            }
            return null;
        }catch (Exception $e){
            return null;
        }
    }

    public function findReObject($id_object){
        try {
            $req=$this->getBdd()->prepare('SELECT * FROM '.$this->getReportTable().' WHERE id_object= ?');
            $req->execute(array($id_object));
            while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
                return new Report($row);
            }
            return null;
        }catch (Exception $e){
            return null;
        }
    }

    public function createReport(Report $report)
    {
        try {
            $this->getBdd()->beginTransaction();
            $req1 = $this->getBdd()->prepare('INSERT INTO '.$this->getReportTable().' (id_user,id_object,description) VALUES (?,?,?)');
            $req2 = $this->getBdd()->prepare('UPDATE '.$this->getObjectTable().'  SET type = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(type,\'tree\',\'tree_report\'),\'banc\',\'banc_report\'),\'trash\',\'trash_report\'),\'pav_verre\',\'pav_verre_report\'),\'toilet\',\'toilet_report\') WHERE id_object = ?');
            $req2->execute(array($report->getId_Object()));
            $req1->execute(array($report->getId_User(), $report->getId_Object(), $report->getDescription()));
            $this->getBdd()->commit();
            return $this->getBdd() -> lastInsertId();
        } catch (Exception $e) {
            $this->getBdd()->rollBack();
            return null;
        }
    }

    public function deleteReport($id_report){
        try {
            $id_object = $this->findReport($id_report)->getId_Object();
            $this->getBdd()->beginTransaction();
            $req1 = $this->getBdd()->prepare('DELETE FROM '.$this->getReportTable().' WHERE id_report= ?');
            $req2 = $this->getBdd()->prepare('UPDATE '.$this->getObjectTable().' SET id_user = NULL , type = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(type,\'tree_report\',\'tree\'),\'banc_report\',\'banc\'),\'trash_report\',\'trash\'),\'pav_verre_report\',\'pav_verre\'),\'toilet_report\',\'toilet\')  Where id_object = ?');
            $req2->execute(array($id_object));
            $req1->execute(array($id_report));
            $this->getBdd()->commit();
            return true;
        } catch (Exception $e) {
            $this->getBdd()->rollBack();
            return false;
        }
    }
}
