<?php

class Note {
    private $_id_note;
    private $_id_user;
    private $_id_object;
    private $_note;

    //CONSTRUCTEURS
    public function __construct()
    {
        $ctp = func_num_args();
        $args = func_get_args();
        switch($ctp)
        {
            case 3:
                $this->_id_note = 0;
                $this->_id_user = $args[0];
                $this->_id_object = $args[1];
                $this->_note = $args[2];
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
    public function getId_Note(){ return $this->_id_note; }

    /**
     * @param mixed $id_note
     */
    public function setId_Note($id_note){ $this->_id_note = $id_note; }

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
    public function getNote(){ return $this->_note; }

    /**
     * @param mixed $note
     */
    public function setNote($note){ $this->_note = $note; }
}