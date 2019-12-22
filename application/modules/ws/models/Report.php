<?php

class Report {
    private $_id_report;
    private $_id_user;
    private $_id_object;
    private $_description;

    //CONSTRUCTEURS
    public function __construct()
    {
        $ctp = func_num_args();
        $args = func_get_args();
        switch($ctp)
        {
            case 3:
                $this->_id_report = 0;
                $this->_id_user = $args[0];
                $this->_id_object = $args[1];
                $this->_description = $args[2];
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
    public function getId_Report(){ return $this->_id_report; }

    /**
     * @param mixed $id_report
     */
    public function setId_Report($id_report){ $this->_id_report = $id_report; }

    /**
     * @return mixed
     */
    public function getId_User(){ return $this->_id_user;}

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
    public function getDescription(){ return $this->_description; }

    /**
     * @param mixed $description
     */
    public function setDescription($description){ $this->_description = $description; }
}