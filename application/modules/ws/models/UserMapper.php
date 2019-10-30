<?php
class UserMapper extends Model{

    public function __construct()
    {
        $this->setTable('data.user');
    }

    /*public function fetchAll(){
        $req=$this->getBdd()->prepare('SELECT * FROM '.$this->getTable());
        $req->execute();
        $data= array();
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            array_push ($data,(new User($row)));
        }
        return $data;
    }*/

    public function findByIdUser($id_user){
        $req=$this->getBdd()->prepare('SELECT * FROM '.$this->getTable().' WHERE id_user='.$id_user);
        $req->execute();
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            return new User($row);
        }
        return null;
    }

   /* public function findByLogin($login){
        $req=$this->getBdd()->prepare('SELECT * FROM '.$this->getTable().' WHERE login=\''.$login.'\'');
        $req->execute();
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            return new User($row);
        }
        return null;
    }

    public function resetSecret($user){
        $configApp = Retrinko\Ini\IniFile::load(PATH_CONFIG."application.ini");
        $newSecret=$user->getSecret();
        while ($newSecret==$user->getSecret()){
            $newSecret=$this->_generateRandomSecret();
        }
        $newSecretHash= password_hash($newSecret, $configApp->get('password', 'algoInInt'));
        $req=$this->getBdd()->prepare('UPDATE '.$this->getTable().' SET secret = \''.$newSecretHash.'\' WHERE id_user='.$user->getId_user());
        $req->execute();
        return $newSecret;
    }


    private function _generateRandomSecret($longueur = 10)
    {
        $caracteres = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        $longueurMax = strlen($caracteres);
        $chaineAleatoire = '';
        for ($i = 0; $i < $longueur; $i ++) {
            $chaineAleatoire .= $caracteres[rand(0, $longueurMax - 1)];
        }
        return $chaineAleatoire;
    }*/
}