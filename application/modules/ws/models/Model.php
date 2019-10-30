<?php

abstract class Model
{
    private static $_bdd;
    private static $_table;

    public function __construct()
    {
        $this->setBdd();
    }

    //INSTANCIE LA CONNEXION A LA BDD
    private static function setBdd()
    {
        $configBdd = Retrinko\Ini\IniFile::load(PATH_CONFIG."database.ini");
        self::$_bdd = new PDO('pgsql:host='.$configBdd->get('database', 'host').';dbname='.$configBdd->get('database', 'dbname'), $configBdd->get('database', 'username'), $configBdd->get('database', 'password'));
        self::$_bdd->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_WARNING);
    }

    //RECUPERE LA CONNEXION A LA BDD

    /**
     * @return mixed
     */
    public static function getTable()
    {
        return self::$_table;
    }

    /**
     * @param mixed $table
     */
    public static function setTable($table)
    {
        self::$_table = $table;
    }

    protected function getBdd()
    {
        if(self::$_bdd==null){
            self::setBdd();
        }
        return self::$_bdd;
    }
}