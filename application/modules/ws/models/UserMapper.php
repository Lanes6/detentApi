<?php
class UserMapper extends Model{

    public function __construct()
    {
        $this->setTable('data.user');
    }

    public function findByIdUser($id_user){
        $req=$this->getBdd()->prepare('SELECT * FROM '.$this->getTable().' WHERE id_user='.$id_user);
        $req->execute();
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            return new User($row);
        }
        return null;
    }

    public function findByLogin($login){
        $req=$this->getBdd()->prepare('SELECT * FROM '.$this->getTable().' WHERE login=\''.$login.'\'');
        $req->execute();
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            return new User($row);
        }
        return null;
    }

    public function findByMail($mail){
        $req=$this->getBdd()->prepare('SELECT * FROM '.$this->getTable().' WHERE mail=\''.$mail.'\'');
        $req->execute();
        while ($row = $req->fetch(PDO::FETCH_ASSOC)) {
            return new User($row);
        }
        return null;
    }

    public function createUser(User $user){
        $req=$this->getBdd()->prepare('INSERT INTO '.$this->getTable().'(login, hash_mdp, secret_token, mail) VALUES (\''.$user->getLogin().'\', \''.$user->getHash_mdp().'\', \''.$user->getSecret_token().'\', \''.$user->getMail().'\')');
        $req->execute();
        return true;
    }

    public function updateUser(User $user){
        $req=$this->getBdd()->prepare(' UPDATE '.$this->getTable().' SET login=\''.$user->getLogin().'\', hash_mdp=\''.$user->getHash_mdp().'\', secret_token=\''.$user->getSecret_token().'\', mail=\''.$user->getMail().'\' WHERE id_user=\''.$user->getId_User().'\'');
        $req->execute();
        return true;
    }

    public function deleteUser($id_user){
        $req=$this->getBdd()->prepare('DELETE FROM '.$this->getTable().' WHERE id_user='.$id_user);
        $req->execute();
        return true;
    }
}