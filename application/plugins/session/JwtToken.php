<?php

class JwtToken
{
    private $_userMapper;
    private $_configApp;

    public function __construct()
    {
        $this->_userMapper = new UserMapper();
        $this->_configApp = Retrinko\Ini\IniFile::load(PATH_CONFIG . "application.ini");
    }

    public function createRefreshToken($user)
    {
        $secret = $this->_userMapper->resetSecret($user);
        $time = time();
        $payload = array(
            'iat' => $time,
            'iss' => 'idkApp',
            'exp' => $time + $this->_configApp->get('jwt', 'timeLifeRefreshToken'),
            'id_user' => $user->getId_User(),
            'secret' => $secret
        );
        $jwt = new JWT();
        return $jwt->encode($payload, $this->_configApp->get('jwt', 'key'), $this->_configApp->get('jwt', 'algo'));
    }

    public function createToken($user)
    {
        $time = time();
        $payload = array(
            'iat' => $time,
            'iss' => 'idkApp',
            'exp' => $time + $this->_configApp->get('jwt', 'timeLifeToken'),
            'id_user' => $user->getId_User()
        );
        $jwt = new JWT();
        return $jwt->encode($payload, $this->_configApp->get('jwt', 'key'), $this->_configApp->get('jwt', 'algo'));
    }

    public function verifyToken($token)
    {
        try {
            $jwt = new JWT();
            $payload = $jwt->decode($token, $this->_configApp->get('jwt', 'key'), array($this->_configApp->get('jwt', 'algo')));
        } catch (Exception $e) {
            return null;
        }
        return $payload;
    }

    public function refreshJwtToken($refreshPayload)
    {
        $user = $this->_userMapper->findByIdUser($refreshPayload->id_user);
        if (password_verify($refreshPayload->secret,$user->getSecret())){
            return $this->createToken($user);
        }else{
            return null;
        }
    }
}