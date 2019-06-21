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

public class HomePageRight extends AppCompatActivity implements OnClickableAreaClickedListener {
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
        image.setImageResource(R.drawable.right);

        // Create your image
        ClickableAreasImage clickableAreasImage = new ClickableAreasImage(new PhotoViewAttacher(image), this);

        // Define your clickable area (pixel values: x coordinate, y coordinate, width, height) and assign an object to it
        List<ClickableArea> clickableAreas = getClickableAreas();
        clickableAreasImage.setClickableAreas(clickableAreas);

        questionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageRight.this, HomePageInfo.class);
                startActivity(intent);
            }
        });

    }

    // Listen for touches on your images:
    @Override
    public void onClickableAreaTouched(Object item) {
        if (item instanceof BodyParts) {
            String text = ((BodyParts) item).getName();
            Intent intent = new Intent(HomePageRight.this,PartsActivity.class);
            intent.putExtra("bodypart",text);
            startActivity(intent);

            //Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private List<ClickableArea> getClickableAreas() {

        List<ClickableArea> clickableAreas = new ArrayList<>();

        clickableAreas.add(new ClickableArea(177,308,224,394, new BodyParts("Thigh")));
        clickableAreas.add(new ClickableArea(179,395,221,440, new BodyParts("Knee")));
        clickableAreas.add(new ClickableArea(191,442,224,545, new BodyParts("Calf")));
        clickableAreas.add(new ClickableArea(188,148,222,213, new BodyParts("arm")));
        clickableAreas.add(new ClickableArea(140,213,207,250, new BodyParts("arm")));
        clickableAreas.add(new ClickableArea(118,239,165,278, new BodyParts("arm")));
        clickableAreas.add(new ClickableArea(56,297,108,335, new BodyParts("fingers")));
        clickableAreas.add(new ClickableArea(87,266,123,304, new BodyParts("hand")));
        clickableAreas.add(new ClickableArea(160,134,185,200, new BodyParts("chest")));
        clickableAreas.add(new ClickableArea(181,116,227,142, new BodyParts("shoulder")));
        clickableAreas.add(new ClickableArea(179,96,216,120, new BodyParts("neck")));
        clickableAreas.add(new ClickableArea(172,4,214,29,new BodyParts("forehead")));
        clickableAreas.add(new ClickableArea(209,3,234,51,new BodyParts("head")));
        clickableAreas.add(new ClickableArea(187,547,223,585,new BodyParts("Ankle")));
        clickableAreas.add(new ClickableArea(140,565,174,593,new BodyParts("toes")));
        clickableAreas.add(new ClickableArea(187,37,211,65,new BodyParts("ear")));
        clickableAreas.add(new ClickableArea(155,37,182,54,new BodyParts("eye")));
        clickableAreas.add(new ClickableArea(150,63,172,79,new BodyParts("mouth")));
        clickableAreas.add(new ClickableArea(155,49,167,66, new BodyParts("nose")));

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
        Intent intent = new Intent(HomePageRight.this,HomePage.class);
        startActivity(intent);
    }

    public void NextImg(View view){
        Intent intent = new Intent(HomePageRight.this,HomePageLeft.class);
        startActivity(intent);
    }
}
