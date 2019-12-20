<?php
class UserMapper extends Model{

    public function __construct()
    {
        $this->setTable('data.user');
    }

    public function findByIdUser($id_user){
        try {
            $req = $this->getBdd()->prepare('SELECT * FROM ' . $this->getTable() . ' WHERE id_user= ?');
            $req->execute(array($id_user));
            while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
                return new User($row);
            }
            return null;
        }catch (Exception $e){
            return null;
        }
    }

    public function findByLogin($login){
        try{
            $req=$this->getBdd()->prepare('SELECT * FROM '.$this->getTable().' WHERE login= ?');
            $req->execute(array($login));
            while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
                return new User($row);
            }
            return null;
        }catch (Exception $e){
            return null;
        }
    }

    public function findByMail($mail){
        try{
            $req=$this->getBdd()->prepare('SELECT * FROM '.$this->getTable().' WHERE mail= ?');
            $req->execute(array($mail));
            while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
                return new User($row);
            }
            return null;
        }catch (Exception $e){
            return null;
        }
    }

    public function createUser(User $user){
        try{
            $req=$this->getBdd()->prepare('INSERT INTO '.$this->getTable().' (login, hash_mdp, secret_token, mail) VALUES (?,?,?,?)');
            $req->execute(array($user->getLogin(),$user->getHash_mdp(),$user->getSecret_token(),$user->getMail()));
            return true;
        }catch (Exception $e){
            return false;
        }
    }

    public function updateUser(User $user){
        try{
            $req=$this->getBdd()->prepare(' UPDATE '.$this->getTable().' SET login= ?, hash_mdp= ?, secret_token= ?, mail= ? WHERE id_user= ?');
            $req->execute(array($user->getLogin(),$user->getHash_mdp(),$user->getSecret_token(),$user->getMail(),$user->getId_User()));
            return true;
        }catch (Exception $e){
            return false;
        }
    }

    public function deleteUser($id_user){
        try{
            $req=$this->getBdd()->prepare('DELETE FROM '.$this->getTable().' WHERE id_user= ?');
            $req->execute(array($id_user));
            return true;
        }catch (Exception $e){
            return false;
        }
    }
}