package edu.pjwstk.ifpk.nocmuzeowapp.DTO;

/**
 * Created by ptrkr on 05.05.2017.
 */

public class Riddle {

    private String heroName;
    private Hero hero;
    private String name;
    private RiddleType type;
    private String description;
    private int resource;

    public String getHeroName() {
        return heroName;
    }

    public Hero getHero() {
        return hero;
    }

    public String getName() {
        return name;
    }

    public RiddleType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public int getResource() {
        return resource;
    }

    public Riddle(Hero hero, String name, RiddleType rt, String description, int resource){
        this.hero = hero;

        heroName = hero.getName();
        this.name =  name;
        this.type=rt;
        this.description = description;
        this.resource = resource;
    }


}
