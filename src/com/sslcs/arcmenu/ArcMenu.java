package com.sslcs.arcmenu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ArcMenu extends RelativeLayout
{
    private boolean areButtonsShowing = false;// 有冇展開
    private Context mycontext;
    private ImageView cross; // 主按鈕中間嗰個十字
    private Animations myani; // 動畫類
    private LinearLayout[] llayouts; // 子按鈕集
    private int durTime = 300;

    public ArcMenu(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mycontext = context;
    }

    /**
     * 初始化
     *
     * @param imgResId         子按鈕嘅圖片drawalbe嘅id[]
     * @param showhideButtonId 主按鈕嘅圖片drawable嘅id
     * @param crossId          主按鈕上面嗰個轉動十字嘅圖片drawable嘅id
     * @param pos              位置代碼，例如“右上角”係ALIGN_PARENT_BOTTOM|ALIGN_PARENT_RIGHT
     * @param radius           半徑
     * @param durationMillis   動畫耗時
     */
    public void init(int[] imgResId, int showhideButtonId, int crossId, Position pos, int radius, final int durationMillis)
    {
        durTime = durationMillis;
        int[] align = initAlign(pos);
        // 如果細過半徑就整大佢
        RelativeLayout.LayoutParams thislps = (LayoutParams) this.getLayoutParams();
        Bitmap mBottom = BitmapFactory.decodeResource(mycontext.getResources(), imgResId[0]);
        if (pos == Position.CENTERBOTTOM || pos == Position.CENTERTOP)
        {
            if (thislps.width != -1 && thislps.width != -2 && thislps.width < (radius + mBottom.getWidth() + radius * 0.1) * 2)
            {
                thislps.width = (int) ((radius * 1.1 + mBottom.getWidth()) * 2);
            }
        }
        else
        {
            if (thislps.width != -1 && thislps.width != -2 && thislps.width < radius + mBottom.getWidth() + radius * 0.1)
            {
                thislps.width = (int) (radius * 1.1 + mBottom.getWidth());
            }
        }
        if (pos == Position.LEFTCENTER || pos == Position.RIGHTCENTER)
        {
            if (thislps.height != -1 && thislps.height != -2 && thislps.height < (radius + mBottom.getHeight() + radius * 0.1) * 2)
            {
                thislps.width = (int) ((radius * 1.1 + mBottom.getHeight()) * 2);
            }
        }
        else
        {
            if (thislps.height != -1 && thislps.height != -2 && thislps.height < radius + mBottom.getHeight() + radius * 0.1)
            {
                thislps.height = (int) (radius * 1.1 + mBottom.getHeight());
            }
        }
        this.setLayoutParams(thislps);
        // 兩個主要層
        RelativeLayout rl1 = new RelativeLayout(mycontext);// 包含若干子按鈕嘅層

        RelativeLayout rlButton = new RelativeLayout(mycontext);
        llayouts = new LinearLayout[imgResId.length];

        int wrap = LayoutParams.WRAP_CONTENT;
        int match = LayoutParams.MATCH_PARENT;
        // N個子按鈕
        for (int i = 0; i < imgResId.length; i++)
        {
            ImageView img = new ImageView(mycontext);// 子按扭圖片
            img.setImageResource(imgResId[i]);
            LinearLayout.LayoutParams llps = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            img.setLayoutParams(llps);
            llayouts[i] = new LinearLayout(mycontext);// 子按鈕層
            llayouts[i].setId(imgResId[i]);
            llayouts[i].addView(img);

            RelativeLayout.LayoutParams rlps = new RelativeLayout.LayoutParams(wrap, wrap);
            rlps.alignWithParent = true;
            rlps.addRule(align[0], RelativeLayout.TRUE);
            rlps.addRule(align[1], RelativeLayout.TRUE);
            llayouts[i].setLayoutParams(rlps);
            llayouts[i].setVisibility(View.INVISIBLE);// 此处不能为GONE
            rl1.addView(llayouts[i]);
        }
        RelativeLayout.LayoutParams rlps1 = new RelativeLayout.LayoutParams(match, match);
        rlps1.alignWithParent = true;
        rlps1.addRule(align[0], RelativeLayout.TRUE);
        rlps1.addRule(align[1], RelativeLayout.TRUE);
        rl1.setLayoutParams(rlps1);

        RelativeLayout.LayoutParams buttonlps = new RelativeLayout.LayoutParams(wrap, wrap);
        buttonlps.alignWithParent = true;
        buttonlps.addRule(align[0], RelativeLayout.TRUE);
        buttonlps.addRule(align[1], RelativeLayout.TRUE);
        rlButton.setLayoutParams(buttonlps);
        rlButton.setBackgroundResource(showhideButtonId);
        cross = new ImageView(mycontext);
        cross.setImageResource(crossId);
        RelativeLayout.LayoutParams crosslps = new RelativeLayout.LayoutParams(wrap, wrap);
        crosslps.alignWithParent = true;
        crosslps.addRule(CENTER_IN_PARENT, RelativeLayout.TRUE);
        cross.setLayoutParams(crosslps);
        rlButton.addView(cross);
        myani = new Animations(rl1, pos, radius);
        rlButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (areButtonsShowing)
                {
                    collapse();
                }
                else
                {
                    expand();
                }
            }
        });

        cross.startAnimation(Animations.getRotateAnimation(0, 360, 200));
        this.addView(rl1);
        this.addView(rlButton);
    }

    private int[] initAlign(Position pos)
    {
        int align1, align2;
        if (pos == Position.RIGHTBOTTOM)
        { // 右下角
            align1 = ALIGN_PARENT_RIGHT;
            align2 = ALIGN_PARENT_BOTTOM;
        }
        else if (pos == Position.CENTERBOTTOM)
        {// 中下
            align1 = CENTER_HORIZONTAL;
            align2 = ALIGN_PARENT_BOTTOM;
        }
        else if (pos == Position.LEFTBOTTOM)
        { // 左下角
            align1 = ALIGN_PARENT_LEFT;
            align2 = ALIGN_PARENT_BOTTOM;
        }
        else if (pos == Position.LEFTCENTER)
        { // 左中
            align1 = ALIGN_PARENT_LEFT;
            align2 = CENTER_VERTICAL;
        }
        else if (pos == Position.LEFTTOP)
        { // 左上角
            align1 = ALIGN_PARENT_LEFT;
            align2 = ALIGN_PARENT_TOP;
        }
        else if (pos == Position.CENTERTOP)
        { // 中上
            align1 = CENTER_HORIZONTAL;
            align2 = ALIGN_PARENT_TOP;
        }
        else if (pos == Position.RIGHTTOP)
        { // 右上角
            align1 = ALIGN_PARENT_RIGHT;
            align2 = ALIGN_PARENT_TOP;
        }
        else
        { // 右中
            align1 = ALIGN_PARENT_RIGHT;
            align2 = CENTER_VERTICAL;
        }
        return new int[]{align1,align2};
    }

    /**
     * 收埋
     */
    public void collapse()
    {
        myani.startAnimationsOut(durTime);
        cross.startAnimation(Animations.getRotateAnimation(-270, 0, durTime));
        areButtonsShowing = false;
    }

    /**
     * 打開
     */
    public void expand()
    {
        myani.startAnimationsIn(durTime);
        cross.startAnimation(Animations.getRotateAnimation(0, -270, durTime));
        areButtonsShowing = true;
    }

    /**
     * 設置各子按鈕嘅onclick事件
     */
    public void setButtonsOnClickListener(final OnClickListener l)
    {
        for (LinearLayout llayout : llayouts)
        {
            llayout.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(final View view)
                {
                    collapse();
                    l.onClick(view);
                }
            });
        }
    }

    public enum Position
    {
        RIGHTBOTTOM, CENTERBOTTOM, LEFTBOTTOM, LEFTCENTER, LEFTTOP, CENTERTOP, RIGHTTOP, RIGHTCENTER
    }
}