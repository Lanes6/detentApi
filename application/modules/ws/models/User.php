<?php

class User implements \JsonSerializable {
    private $_id_user;
    private $_login;
    private $_mail;
    private $_hash_mdp;
    private $_secret_token;
    private $_configApp;

    //CONSTRUCTEURS
    public function __construct()
    {
        $this->_configApp = Retrinko\Ini\IniFile::load(PATH_CONFIG . "application.ini");

        $ctp = func_num_args();
        $args = func_get_args();
        switch($ctp)
        {
            case 3:
                $this->_id_user = 0;
                $this->_login = $args[0];
                $this->_mail = $args[1];
                $this->hashMdp($args[2]);
                $this->generateSecretToken();
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

    public function hashMdp($password){
        $this->_hash_mdp= password_hash($password, $this->_configApp->get('password', 'algoInInt'));
    }

    public function generateSecretToken($longueur = 10){
        $caracteres = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        $longueurMax = strlen($caracteres);
        $chaineAleatoire = '';
        for ($i = 0; $i < $longueur; $i ++) {
            $chaineAleatoire .= $caracteres[rand(0, $longueurMax - 1)];
        }
        $this->_secret_token = $chaineAleatoire;
    }

    public function checkPassword($password){
        return password_verify($password,$this->getHash_mdp());
    }
















    /**
     * @return mixed
     */
    public function getMail()
    {
        return $this->_mail;
    }

    /**
     * @param mixed $mail
     */
    public function setMail($mail)
    {
        $this->_mail = $mail;
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