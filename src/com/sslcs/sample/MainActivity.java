package com.sslcs.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.sslcs.arcmenu.ArcMenu;

public class MainActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArcMenu arcMenu = (ArcMenu) findViewById(R.id.arc_menu);
        arcMenu.init(new int[]{R.drawable.ic_camera, R.drawable.ic_music, R.drawable.ic_location}, R.drawable.bg_btn, R.drawable.ic_plus, ArcMenu.Position.RIGHTBOTTOM, 180, 300);
        OnClickListener clickit = new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (v.getId() == R.drawable.ic_camera)
                {
                    System.out.println("ic_camera");
                }
                else if (v.getId() == R.drawable.ic_music)
                {
                    System.out.println("ic_music");
                }
                else if (v.getId() == R.drawable.ic_location)
                {
                    System.out.println("ic_location");
                }
            }
        };
        arcMenu.setButtonsOnClickListener(clickit);
    }

}