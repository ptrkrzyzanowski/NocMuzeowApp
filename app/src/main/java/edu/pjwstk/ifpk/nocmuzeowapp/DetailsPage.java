package edu.pjwstk.ifpk.nocmuzeowapp;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import edu.pjwstk.ifpk.nocmuzeowapp.DTO.Hero;

/**
 * Created by ptrkr on 28.04.2017.
 */

public class DetailsPage {
    Activity owner;
    Hero hero;
    private TextView nameTV;
    private TextView detailsTV;
    private ImageView imageIV;

    public DetailsPage(final Activity owner){
        nameTV = (TextView) owner.findViewById(R.id.details_name);
        detailsTV = (TextView) owner.findViewById(R.id.details_details);
        imageIV = (ImageView) owner.findViewById(R.id.details_image);
        this.owner=owner;
    }
    public void setHero(final Hero hero){
        this.hero = hero;
        owner.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nameTV.setText(hero.getName());
                imageIV.setImageResource(hero.getDetailsDrawable());
                detailsTV.setText(hero.getDescription());
            }
        });
    }

}
