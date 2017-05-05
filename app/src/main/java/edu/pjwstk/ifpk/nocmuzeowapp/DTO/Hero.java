package edu.pjwstk.ifpk.nocmuzeowapp.DTO;

public class Hero {
    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id=id;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }
    public boolean istKnown(){
        return known;
    }
    public void meet(){
        known = true;
    }
    public int getDrawable(){
        return image;
    }
    public int getDetailsDrawable(){return image_details;}

    private int id;
    private String name;
    private String description;
    private String imagename;
    private int image;
    private int image_details;
    private boolean known;
    public Hero(){

    }
    public Hero(int id,String name, String description, String imagename){
        this.id=id;
        this.name=name;
        this.description=description;
        this.imagename=imagename;
        this.known = false;

    }
    public Hero(int id,String name, String description, String imagename,int img,int detaimg,boolean known){
        this.id=id;
        this.name=name;
        this.description=description;
        this.imagename=imagename;
        this.image=img;
        this.image_details = detaimg;
        this.known = known;

    }

}
