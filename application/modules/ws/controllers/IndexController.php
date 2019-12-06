<?php

class IndexController{
    private $_return;
    private $_userMapper;

    public function __construct()
    {
        $this->_return=[];
        $this->_userMapper = new UserMapper();
        http_response_code(500);
    }


    public function indexAction(){
    	$this->_return["msg"]="Hello";
    	http_response_code(200);
    	echo(json_encode($this->_return));
    }

    public function testDBAction(){
        $this->_return["msg"]="TestDB";
		$user = $this->_userMapper->findByIdUser(1);

		if($user!=null){
			http_response_code(200);
			$this->_return["contenu"]=$user->getLogin();
		}else{
			http_response_code(500);
			$this->_return["contenu"]="Probleme";
		}
    	echo(json_encode($this->_return));
    }

    public function testComposerAction(){
        $this->_return["msg"]="TestComposer";
        $configBdd = Retrinko\Ini\IniFile::load(PATH_CONFIG."database.ini");
        $this->_return["contenu"]=$configBdd->get('database', 'dbname');
        http_response_code(200);
        echo(json_encode($this->_return));
    }

    public function testJWTAction(){
        $this->_return["msg"]="TestJwt good";
        http_response_code(200);
        echo(json_encode($this->_return));
    }

    public function giveMeMyIdAction(){
        $jwtToken = new JwtToken();
        $payload = $jwtToken->giveMePayload();
        $this->_return["msg"]="My ID:".$payload->id_user;
        http_response_code(200);
        echo(json_encode($this->_return));
    }

}
