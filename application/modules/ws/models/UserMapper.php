<?php
class UserMapper extends ModelMapper{

    public function findByIdUser($id_user){
        try {
            $req = $this->getBdd()->prepare('SELECT * FROM '.$this->getUserTable().' WHERE id_user= ?');
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
            $req=$this->getBdd()->prepare('SELECT * FROM '.$this->getUserTable().' WHERE login= ?');
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
            $req=$this->getBdd()->prepare('SELECT * FROM '.$this->getUserTable().' WHERE mail= ?');
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
            $req=$this->getBdd()->prepare('INSERT INTO '.$this->getUserTable().' (login, hash_mdp, secret_token, mail) VALUES (?,?,?,?)');
            $req->execute(array($user->getLogin(),$user->getHash_mdp(),$user->getSecret_token(),$user->getMail()));
            return $this->getBdd() -> lastInsertId();
        }catch (Exception $e){
            return null;
        }
    }

    public function updateUser(User $user){
        try{
            $req=$this->getBdd()->prepare(' UPDATE '.$this->getUserTable().' SET login= ?, hash_mdp= ?, secret_token= ?, mail= ? WHERE id_user= ?');
            $req->execute(array($user->getLogin(),$user->getHash_mdp(),$user->getSecret_token(),$user->getMail(),$user->getId_User()));
            return true;
        }catch (Exception $e){
            return false;
        }
    }

    public function deleteUser($id_user){
        try{
            $req=$this->getBdd()->prepare('DELETE FROM '.$this->getUserTable().' WHERE id_user= ?');
            $req->execute(array($id_user));
            return true;
        }catch (Exception $e){
            return false;
        }
    }
}