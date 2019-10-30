<?php

class User implements \JsonSerializable {
    private $_id_user;
    private $_login;
    private $_hash_mdp;
    private $_secret_token;

    //CONSTRUCTEUR
    public function __construct(array $data)
    {
        $this->hydrate($data);
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
    public function getLogin()
    {
        return $this->_login;
    }

    /**
     * @param mixed $login
     */
    public function setLogin($login)
    {
        if(is_string($login)){
            $this->_login = $login;
        }
    }

    /**
     * @return mixed
     */
    public function getHash_mdp()
    {
        return $this->_hash_mdp;
    }

    /**
     * @param mixed $hash_mdp
     */
    public function setHash_mdp($hash_mdp)
    {
        if(is_string($hash_mdp)) {
            $this->_hash_mdp = $hash_mdp;
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
    public function getSecret_token()
    {
        return $this->_secret_token;
    }

    /**
     * @param mixed $secret_token
     */
    public function setSecret_token($secret_token)
    {
        $this->_secret_token = $secret_token;
    }


}