package Src.auth;

public abstract class Utilisateur  {
    private int id;
    private String identifiant;
    private String password;
    private String name;
    private String role;   


    public  Utilisateur(int id,String identifiant,String password,String name,String role){
        this.id=id;
        this.identifiant=identifiant;
        this.password=password;
        this.name=name;
        this.role=role;
    }
    //getters
    public int getId(){
        return id;
    }
    public String getIdentifiant(){
        return identifiant;
    }
    public String getPassword(){
        return password;

    }
    public String getName(){
        return name;
    }
    public String getRole(){
        return role;
    }   
    //setters
    public void setName(String name){
        this.name=name;
    }
    public void setPassword(String password){
        this.password=password;
    }
    //aide pour la sauvegarde dans le fichier
    @Override
    public String toString(){
        return id + "|" + identifiant + "|" + password + "|" + name + "|" + password;
    }
}
