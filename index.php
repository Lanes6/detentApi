<?php
define('URL',str_replace("index.php","",(isset($_SERVER['HTTPS']) ? "https" : "http") . "://$_SERVER[HTTP_HOST]$_SERVER[PHP_SELF]"));
define('PATH_CONFIG',"application/configs/");

require "vendor/autoload.php";
require_once('Router.php');
$router = new Router();
$router->routeReq();