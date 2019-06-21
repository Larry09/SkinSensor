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

public class HomePage extends AppCompatActivity implements OnClickableAreaClickedListener {

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
        image.setImageResource(R.drawable.center);

        // Create your image
        ClickableAreasImage clickableAreasImage = new ClickableAreasImage(new PhotoViewAttacher(image), this);

        // Define your clickable area (pixel values: x coordinate, y coordinate, width, height) and assign an object to it
        List<ClickableArea> clickableAreas = getClickableAreas();
        clickableAreasImage.setClickableAreas(clickableAreas);

        questionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, HomePageInfo.class);
                startActivity(intent);
            }
        });

    }


    // Listen for touches on your images:
    @Override
    public void onClickableAreaTouched(Object item) {
        if (item instanceof BodyParts) {
            String text = ((BodyParts) item).getName();
            Intent intent = new Intent(HomePage.this,PartsActivity.class);
            intent.putExtra("bodypart",text);
            startActivity(intent);

            //Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private List<ClickableArea> getClickableAreas() {

        List<ClickableArea> clickableAreas = new ArrayList<>();
        clickableAreas.add(new ClickableArea(205,188,277,259,new BodyParts("Abdomen")));
        clickableAreas.add(new ClickableArea(175,193,198,249,new BodyParts("Oblique")));//R
        clickableAreas.add(new ClickableArea(290,196,313,252,new BodyParts("Oblique")));//L

        clickableAreas.add(new ClickableArea(221,274,266,307,new BodyParts("Groin")));

        clickableAreas.add(new ClickableArea(172,310,214,383,new BodyParts("Thigh")));//l
        clickableAreas.add(new ClickableArea(269,308,311,381,new BodyParts("Thigh")));//R

        clickableAreas.add(new ClickableArea(168,393,204,430,new BodyParts("Knee")));//l
        clickableAreas.add(new ClickableArea(282,393,318,430,new BodyParts("Knee")));//r

        clickableAreas.add(new ClickableArea(160,442,185,521,new BodyParts("Calf")));//l
        clickableAreas.add(new ClickableArea(298,438,323,517,new BodyParts("Calf")));//r

        clickableAreas.add(new ClickableArea(305,529,331,555,new BodyParts("Ankle")));//r
        clickableAreas.add(new ClickableArea(160,528,181,554,new BodyParts("Ankle")));//l

        clickableAreas.add(new ClickableArea(149,562,186,590,new BodyParts("Toe")));//l
        clickableAreas.add(new ClickableArea(304,562,337,586,new BodyParts("Toe")));//r

        clickableAreas.add(new ClickableArea(332,142,436,237,new BodyParts("Arm")));//r
        clickableAreas.add(new ClickableArea(52,141,156,236,new BodyParts("Arm")));//l

        clickableAreas.add(new ClickableArea(424,238,467,257,new BodyParts("Hand")));//r
        clickableAreas.add(new ClickableArea(20,236,52,260,new BodyParts("Hand")));//l


        clickableAreas.add(new ClickableArea(155,111,186,139,new BodyParts("Shoulder")));//l
        clickableAreas.add(new ClickableArea(302,106,333,134,new BodyParts("Shoulder")));//r

        clickableAreas.add(new ClickableArea(5,262,30,294,new BodyParts("Fingers")));//l
        clickableAreas.add(new ClickableArea(456,263,481,295,new BodyParts("Fingers")));//r

        clickableAreas.add(new ClickableArea(202,35,218,62,new BodyParts("Ear")));//r
        clickableAreas.add(new ClickableArea(267,35,288,62,new BodyParts("Ear")));//l

        clickableAreas.add(new ClickableArea(224,57,261,71,new BodyParts("Mouth")));
        clickableAreas.add(new ClickableArea(233,33,251,55,new BodyParts("Nose")));
        clickableAreas.add(new ClickableArea(246,28,262,43, new BodyParts("Eye")));//r
        clickableAreas.add(new ClickableArea(224,28,241,44,new BodyParts("Eye")));//l
        clickableAreas.add(new ClickableArea(214,5,267,28,new BodyParts("Forehead")));
        clickableAreas.add(new ClickableArea(197,131,293,181,new BodyParts("Chest")));
        clickableAreas.add(new ClickableArea(188,93,293,116,new BodyParts("Neck")));
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
        Intent intent = new Intent(HomePage.this,HomePageLeft.class);
        startActivity(intent);
    }

    public void NextImg(View view) {
        Intent intent = new Intent(HomePage.this, HomePageRight.class);
        startActivity(intent);
    }
}
