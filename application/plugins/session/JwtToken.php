<?php

class JwtToken
{
    private $_configApp;

    public function __construct()
    {
        $this->_configApp = Retrinko\Ini\IniFile::load(PATH_CONFIG . "application.ini");
    }

    public function createRefreshToken(User $user)
    {
        $time = time();
        $payload = array(
            'iat' => $time,
            'iss' => 'detentApi',
            'exp' => $time + $this->_configApp->get('jwt', 'timeLifeRefreshToken'),
            'id_user' => $user->getId_User(),
            'secret' => $user->getSecret_token()
        );
        return \Firebase\JWT\JWT::encode($payload, $this->_configApp->get('jwt', 'key'), $this->_configApp->get('jwt', 'algo'));
    }

    public function createToken($user)
    {
        $time = time();
        $payload = array(
            'iat' => $time,
            'iss' => 'detentApi',
            'exp' => $time + $this->_configApp->get('jwt', 'timeLifeToken'),
            'id_user' => $user->getId_User()
        );
        return \Firebase\JWT\JWT::encode($payload, $this->_configApp->get('jwt', 'key'), $this->_configApp->get('jwt', 'algo'));
    }

    public function verifyToken($token)
    {
        try {
            $payload = \Firebase\JWT\JWT::decode($token, $this->_configApp->get('jwt', 'key'), array($this->_configApp->get('jwt', 'algo')));
        } catch (Exception $e) {
            return null;
        }
        return $payload;
    }
}