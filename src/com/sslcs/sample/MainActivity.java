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

        ArcMenu[] arcMenus = new ArcMenu[8];
        ArcMenu.Position[] positions = new ArcMenu.Position[]{ArcMenu.Position.LEFT_TOP, ArcMenu.Position.TOP_CENTER,
            ArcMenu.Position.RIGHT_TOP, ArcMenu.Position.LEFT_CENTER, ArcMenu.Position.RIGHT_CENTER, ArcMenu.Position.LEFT_BOTTOM, ArcMenu.Position.BOTTOM_CENTER, ArcMenu.Position.RIGHT_BOTTOM};
        for (byte i = 0; i < 8; i++)
        {
            arcMenus[i] = (ArcMenu) findViewById(R.id.arc_menu_0 + i);
            arcMenus[i].setRadius(60);
            arcMenus[i].init(getChildViews(), getMenuView(), positions[i]);
        }
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