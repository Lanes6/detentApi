<?php

abstract class ModelMapper
{
    private $_bdd;
    private $_objectTable = 'public.object';
    private $_noteTable = 'data.note';
    private $_userTable = 'data.user';
    private $_reportTable = 'data.report';
    private $_outlineTable = 'public.outline';

    public function __construct()
    {
        $this->setBdd();
    }

    //INSTANCIE LA CONNEXION A LA BDD
    private function setBdd()
    {
        $configBdd = Retrinko\Ini\IniFile::load(PATH_CONFIG."database.ini");
        $this->_bdd = new PDO('pgsql:host='.$configBdd->get('database', 'host').';dbname='.$configBdd->get('database', 'dbname'), $configBdd->get('database', 'username'), $configBdd->get('database', 'password'));
        $this->_bdd->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    }

    //RECUPERE LA CONNEXION A LA BDD
    protected function getBdd()
    {
        if($this->_bdd==null){
            $this->setBdd();
        }
        return $this->_bdd;
    }

    protected function getObjectTable()
    {
        return $this->_objectTable;
    }

    protected function getNoteTable()
    {
        return $this->_noteTable;
    }

    protected function getUserTable()
    {
        return $this->_userTable;
    }

    protected function getReportTable()
    {
        return $this->_reportTable;
    }

    protected function getOutlineTable()
    {
        return $this->_outlineTable;
    }
}