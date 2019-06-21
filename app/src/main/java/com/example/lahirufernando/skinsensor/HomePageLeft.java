package com.example.lahirufernando.skinsensor;
/**
 * Created by lahirufernando on 30/11/2017.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import at.lukle.clickableareasimage.ClickableArea;
import at.lukle.clickableareasimage.ClickableAreasImage;
import at.lukle.clickableareasimage.OnClickableAreaClickedListener;
import uk.co.senab.photoview.PhotoViewAttacher;

public class HomePageLeft extends AppCompatActivity implements OnClickableAreaClickedListener {
    private final String TAG = getClass().getSimpleName();
    public int[] img={R.drawable.center,R.drawable.right,R.drawable.left};
    int i=0;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        FloatingActionButton homeBtn = (FloatingActionButton) findViewById(R.id.homeButton);
        FloatingActionButton questionButton = (FloatingActionButton) findViewById(R.id.questionBtn);

        // Add image
        image = (ImageView) findViewById(R.id.imageView);
        image.setImageResource(R.drawable.left);

        // Create your image
        ClickableAreasImage clickableAreasImage = new ClickableAreasImage(new PhotoViewAttacher(image), this);

        // Define your clickable area (pixel values: x coordinate, y coordinate, width, height) and assign an object to it
        List<ClickableArea> clickableAreas = getClickableAreas();
        clickableAreasImage.setClickableAreas(clickableAreas);

        questionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageLeft.this, HomePageInfo.class);
                startActivity(intent);
            }
        });

    }

    // Listen for touches on your images:
    @Override
    public void onClickableAreaTouched(Object item) {
        if (item instanceof BodyParts) {
            String text = ((BodyParts) item).getName();
            Intent intent = new Intent(HomePageLeft.this,PartsActivity.class);
            intent.putExtra("bodypart",text);
            startActivity(intent);

            //Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private List<ClickableArea> getClickableAreas() {

        List<ClickableArea> clickableAreas = new ArrayList<>();

        clickableAreas.add(new ClickableArea(130,287,272,339, new BodyParts("Bottom")));
        clickableAreas.add(new ClickableArea(118,343,179,419,new BodyParts("Thigh"))); //L
        clickableAreas.add(new ClickableArea(218,345,279,421,new BodyParts("Thigh"))); //R
        clickableAreas.add(new ClickableArea(112,465,154,550,new BodyParts("Calf")));
        clickableAreas.add(new ClickableArea(243,472,290,560,new BodyParts("Calf")));
        clickableAreas.add(new ClickableArea(119,418,167,465,new BodyParts("Knee")));
        clickableAreas.add(new ClickableArea(235,422,283,469,new BodyParts("Knee")));
        clickableAreas.add(new ClickableArea(114,552,143,589,new BodyParts("Ankle")));
        clickableAreas.add(new ClickableArea(262,559,287,595,new BodyParts("Ankle")));
        clickableAreas.add(new ClickableArea(137,164,269,271,new BodyParts("Back")));
        clickableAreas.add(new ClickableArea(62,141,121,202,new BodyParts("Arm")));
        clickableAreas.add(new ClickableArea(274,136,331,205,new BodyParts("Arm")));
        clickableAreas.add(new ClickableArea(12,208,53,248,new BodyParts("Arm")));
        clickableAreas.add(new ClickableArea(347,206,388,246,new BodyParts("Arm")));
        clickableAreas.add(new ClickableArea(43,194,73,222,new BodyParts("Elbow"))); //L
        clickableAreas.add(new ClickableArea(326,196,356,221,new BodyParts("Elbow"))); //R
        clickableAreas.add(new ClickableArea(107,112,151,158,new BodyParts("Shoulder")));
        clickableAreas.add(new ClickableArea(255,112,284,162,new BodyParts("Shoulder")));
        clickableAreas.add(new ClickableArea(146,89,180,124,new BodyParts("Neck")));
        clickableAreas.add(new ClickableArea(225,93,259,128,new BodyParts("Neck")));








        return clickableAreas;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void PrevImg(View view){
        Intent intent = new Intent(HomePageLeft.this,HomePageRight.class);
        startActivity(intent);
    }

    public void NextImg(View view){
        Intent intent = new Intent(HomePageLeft.this,HomePage.class);
        startActivity(intent);
    }
}
