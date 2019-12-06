<?php

class Router
{
    private $_ctrl;

    public function routeReq()
    {
        $module = 'ws';
        $controller = "indexController";
        $action = "indexAction";
        $return = [];

        try {
            //CHARGEMENT AUTO DES CLASS
            require_once('application/plugins/session/MapAcess.php');
            require_once('application/plugins/session/JwtToken.php');
            spl_autoload_register(function ($class) {
                require_once('application/modules/ws/models/' . $class . '.php');
            });
            $url = '';
            if (isset($_GET['url'])) {
                $url = explode('/', filter_var($_GET['url']), FILTER_SANITIZE_URL);
                if (count($url) == 3 && $url[0]=="detentApi") {
                    //detentApi/controller/action
                    $controller = ucfirst($url[1]) . "Controller";
                    $action = $url[2] . "Action";
                    //
                    $controllerFile = 'application/modules/'.$module.'/controllers/' . $controller . '.php';
                    if (file_exists($controllerFile)) {
                        require_once($controllerFile);
                        $this->_ctrl = new $controller();
                        if (method_exists($this->_ctrl, $action)) {
                            if ($this->_checkAcces($module, $controller, $action)) {
                                $this->_ctrl->$action();
                            }else{
                                http_response_code(403);
                                throw new Exception('Acces refuse');
                            }
                        }else{
                            http_response_code(404);
                            throw new Exception('Url invalide 1');
                        }
                    }else{
                        http_response_code(404);
                        throw new Exception('Url invalide 2');
                    }
                }else{
                    http_response_code(404);
                    throw new Exception('Url invalide 3');
                }
            } else {
                http_response_code(404);
                throw new Exception('Url vide');
            }
        } catch (Exception $e) {
            $return["msg"]=$e->getMessage();            
            echo(json_encode($return));
        }
    }

    private function _checkAcces($module, $controller, $action)
    {
        $mapAcess = new MapAcess();
        if($mapAcess->exist($module, $controller, $action)){
            if ($mapAcess->getAcces($module, $controller, $action) != 0) {
                $jwtToken = new JwtToken();
                $payload = $jwtToken->giveMePayload();
                if ($payload != null){
                    return true;
                }
                return false;
            } else {
                return true;
            }
        }else{
            return false;
        }
    }
}