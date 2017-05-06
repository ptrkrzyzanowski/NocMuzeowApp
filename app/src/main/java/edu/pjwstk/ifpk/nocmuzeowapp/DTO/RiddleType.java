package edu.pjwstk.ifpk.nocmuzeowapp.DTO;

/**
 * Created by ptrkr on 05.05.2017.
 */

public enum RiddleType {
    RT_TEXT,
    RT_IMAGE,
    RT_MOVIE,
    RT_PUZZLE;
    public static RiddleType getType(String s){
        s = s.trim();
        if(s.equals("puzzle"))
            return RT_PUZZLE;
        if(s.equals("image"))
            return  RT_IMAGE;
        if(s.equals("movie"))
            return RT_MOVIE;
        return RT_TEXT;
    }
}
