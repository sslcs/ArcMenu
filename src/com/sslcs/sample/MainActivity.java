package com.sslcs.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import com.sslcs.arcmenu.ArcMenu;

public class MainActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArcMenu arcMenu = (ArcMenu) findViewById(R.id.arc_menu);
        arcMenu.setDuration(300);
        arcMenu.setRadius(10);
        arcMenu.init(getChildViews(), getMenuView(), ArcMenu.Position.RIGHT_BOTTOM);
        arcMenu.setOnItemClickListener(new ArcMenu.OnItemClickListener()
        {
            @Override
            public void onItemClick(int pos)
            {
                System.out.println("pos = " + pos);
            }
        });
    }

    private ImageView getMenuView()
    {
        ImageView menu = new ImageView(this);
        menu.setImageResource(R.drawable.ic_camera);
        return menu;
    }

    private ImageView[] getChildViews()
    {
        ImageView[] views = new ImageView[3];
        views[0] = new ImageView(this);
        views[0].setImageResource(R.drawable.ic_camera);
        views[1] = new ImageView(this);
        views[1].setImageResource(R.drawable.ic_music);
        views[2] = new ImageView(this);
        views[2].setImageResource(R.drawable.ic_location);
        return views;
    }
}