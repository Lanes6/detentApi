<?php

class Objet implements \JsonSerializable {
    private $_id_object;
    private $_id_user;
    private $_type;
    private $_description;
    private $_geom;
    private $_configApp;

    //CONSTRUCTEURS
    public function __construct()
    {
        $this->_configApp = Retrinko\Ini\IniFile::load(PATH_CONFIG . "application.ini");
        $ctp = func_num_args();
        $args = func_get_args();
        switch($ctp)
        {
            case 4:
                $this->_id_object = 0;
                $this->_id_user = $args[0];
                $this->_type = $args[1];
                $this->_description = $args[2];
                $this->_geom = $args[3];
                break;

            case 1:
                $this->hydrate($args[0]);
                break;
            default:
                break;
        }
    }

    //HYDRATATION
    public function hydrate(array $data)
    {
        foreach ($data as $key=>$value){
            $method='set'.ucfirst($key);
            if(method_exists($this,$method)){
                $this->$method($value);
            }
        }
    }

    /**
     * @return mixed
     */
    public function getId_Object()
    {
        return $this->_id_object;
    }

    /**
     * @param mixed $id_object
     */
    public function setId_Object($id_object)
    {
        $this->_id_object = $id_object;
    }

    /**
     * @return mixed
     */
    public function getId_User()
    {
        return $this->_id_user;
    }

    /**
     * @param mixed $id_user
     */
    public function setId_User($id_user)
    {
        $id_user=(int)$id_user;
        if($id_user > 0) {
            $this->_id_user = $id_user;
        }
    }

    /**
     * @return mixed
     */
    public function getType()
    {
        return $this->_type;
    }

    /**
     * @param mixed $type
     */
    public function setType($type)
    {
        if(is_string($type)){
            $this->_type = $type;
        }
    }

    /**
     * @return mixed
     */
    public function getDescription()
    {
        return $this->_description;
    }

    /**
     * @param mixed $description
     */
    public function setDescription($description)
    {
        if(is_string($description)) {
            $this->_description = $description;
        }
    }

    //PERMET DE FAIRE LE CONVERSION OBJET -> JSON
    public function jsonSerialize()
    {
        $vars = get_object_vars($this);
        return $vars;
    }

    /**
     * @return mixed
     */
    public function getGeom()
    {
        return $this->_geom;
    }

    /**
     * @param mixed $geom
     */
    public function setGeom($geom)
    {
        $this->_geom = $geom;
    }
}