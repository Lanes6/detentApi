<?php
class ReportMapper extends Model{

    public function __construct()
    {
        $this->setTable('data.report');
    }

    public function findObject($id_object){
        $req=$this->getBdd()->prepare('SELECT * FROM public.object WHERE id_object=\''.$id_object . '\' AND type NOT LIKE \'%_report\' AND type NOT LIKE \'%_suggestion\'');
        $req->execute();
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            return $row['id_object'];
        }
        return null;
    }

    public function findReObject($id_object){
        $req=$this->getBdd()->prepare('SELECT * FROM public.object WHERE id_object=\''.$id_object . '\' AND type LIKE \'%_report\'');
        $req->execute();
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            return $row['id_object'];
        }
        return null;
    }

    public function findReport($id_report){
        $req=$this->getBdd()->prepare('SELECT * FROM '.$this->getTable().' WHERE id_report=\''.$id_report.'\'');
        $req->execute();
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            return new Report($row);
        }
        return null;
    }

    public function findReportByUserObject($id_user,$id_object){
        $req=$this->getBdd()->prepare('SELECT * FROM '.$this->getTable().' WHERE id_user=\''.$id_user.'\' AND id_object=\''.$id_object.'\'');
        $req->execute();
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            return new Report($row);
        }
        return null;
    }

    public function createReport(Report $report){
        $req=$this->getBdd()->prepare('INSERT INTO '.$this->getTable().'(id_user,id_object,description) VALUES (\''.$report->getId_User().'\', \''.$report->getId_Object().'\', \''.$report->getDescription().'\')');
        $req->execute();

        $req2=$this->getBdd()->prepare('UPDATE public.object  SET id_user = '.$report->getId_User().', type = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(type,\'tree\',\'tree_report\'),\'banc\',\'banc_report\'),\'trash\',\'trash_report\'),\'pav_glass\',\'pav_glass_report\'),\'toilet\',\'toilet_report\')  Where id_object = \''.$report->getId_Object().'\'');
        $req2->execute();

        return true;
    }

    public function deleteReport($id_report){
        $req=$this->getBdd()->prepare('SELECT * FROM '.$this->getTable().' WHERE id_report=\''.$id_report . '\'');
        $req->execute();
        $id_object = $req->fetch(PDO::FETCH_ASSOC)['id_object'];

        $req2=$this->getBdd()->prepare('DELETE FROM '.$this->getTable().' WHERE id_report=\''.$id_report.'\'');
        $req2->execute();

        $req3=$this->getBdd()->prepare('UPDATE public.object  SET id_user = NULL , type = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(type,\'tree_report\',\'tree\'),\'banc_report\',\'banc\'),\'trash_report\',\'trash\'),\'pav_glass_report\',\'pav_glass\'),\'toilet_report\',\'toilet\')  Where id_object = \''.$id_object.'\'');
        $req3->execute();
        
        return true;
    }
}
