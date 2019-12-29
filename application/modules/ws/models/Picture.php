<?php

class Picture {
    private $_id_picture;
    private $_id_user;
    private $_id_object;
    private $_saison;
    private $_file;
    private $_name;

    //CONSTRUCTEURS
    public function __construct()
    {
        $ctp = func_num_args();
        $args = func_get_args();
        switch($ctp)
        {
            case 5:
                $this->_id_picture = 0;
                $this->_id_user = $args[0];
                $this->_id_object = $args[1];
                $this->_saison = $args[2];
                $this->_file = $args[3];
                $this->_name = $args[4];
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
    public function getId_Picture(){ return $this->_id_picture; }

    /**
     * @param mixed $id_picture
     */
    public function setId_Picture($id_picture){ $this->_id_picture = $id_picture; }

    /**
     * @return mixed
     */
    public function getId_User(){ return $this->_id_user; }

    /**
     * @param mixed $id_user
     */
    public function setId_User($id_user){ $this->_id_user = $id_user; }

    /**
     * @return mixed
     */
    public function getId_Object(){ return $this->_id_object; }

    /**
     * @param mixed $id_object
     */
    public function setId_Object($id_object){ $this->_id_object = $id_object; }

    /**
     * @return mixed
     */
    public function getSaison(){ return $this->_saison; }

    /**
     * @param mixed $saison
     */
    public function setSaison($saison){ $this->_saison = $saison; }

    /**
     * @return mixed
     */
    public function getFile(){ return $this->_file; }

    /**
     * @param mixed $file
     */
    public function setFile($file){ $this->_file = $file; }

    /**
     * @return mixed
     */
    public function getName(){ return $this->_name; }

    /**
     * @param mixed $name
     */
    public function setName($name){ $this->_name = $name; }
}