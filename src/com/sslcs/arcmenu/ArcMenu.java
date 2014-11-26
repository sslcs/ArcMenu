package com.sslcs.arcmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;

public class ArcMenu extends RelativeLayout
{
    private boolean isShowing = false;
    private Context mContext;
    private byte mCount;
    private View[] mChilds;
    private View mMenu;
    private int mDuration = 300;
    private int mRadius;
    private OnItemClickListener mListener;
    private ArcMenu.Position mPosition;
    private double mAngleRange = 180.0;
    private byte mOriX = 1, mOriY = 1;
    private ArrayList<ViewPropertyAnimator> mAnimators = new ArrayList<ViewPropertyAnimator>();

    public ArcMenu(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        setRadius(90);
    }

    /**
     * Rotate animation fo menu view
     *
     * @param from           from degrees
     * @param to             to degrees
     * @param durationMillis milliseconds of duration
     */
    public static Animation getRotateAnimation(float from, float to, int durationMillis)
    {
        RotateAnimation ra = new RotateAnimation(from, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(durationMillis);
        ra.setFillAfter(true);
        return ra;
    }

    /**
     * initialization
     *
     * @param childs child views
     * @param menu   menu view
     * @param pos    {@link com.sslcs.arcmenu.ArcMenu.Position}
     */
    public void init(View[] childs, View menu, Position pos)
    {
        mCount = (byte) childs.length;
        mChilds = childs;

        int[] align = initPosition(pos);

        int wrap = LayoutParams.WRAP_CONTENT;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(wrap, wrap);
        params.alignWithParent = true;
        params.addRule(align[0], RelativeLayout.TRUE);
        params.addRule(align[1], RelativeLayout.TRUE);
        addChildViews(childs,params);
        addMenuView(menu,params);
    }

    private void addChildViews(View[] childs,RelativeLayout.LayoutParams params)
    {
        int match = LayoutParams.MATCH_PARENT;
        RelativeLayout rlChilds = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams paramsRL = new RelativeLayout.LayoutParams(match, match);
        paramsRL.alignWithParent = true;
        rlChilds.setLayoutParams(paramsRL);
        if (null != mListener)
        {
            setClickListener();
        }
        for (View child : childs)
        {
            child.setLayoutParams(params);
            rlChilds.addView(child);
            ViewPropertyAnimator anim = ViewPropertyAnimator.animate(child);
            mAnimators.add(anim);
        }
        addView(rlChilds);
    }

    private void addMenuView(View menu, RelativeLayout.LayoutParams params)
    {
        mMenu = menu;
        menu.setLayoutParams(params);
        menu.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isShowing)
                {
                    fold();
                }
                else
                {
                    unfold();
                }
            }
        });
        addView(menu);
    }

    private void setClickListener()
    {
        for (byte i = 0; i < mChilds.length; i++)
        {
            final byte finalI = i;
            mChilds[i].setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    fold();
                    mListener.onItemClick(finalI);
                }
            });
        }
    }

    /**
     * @param pos Location of the screen {@link com.sslcs.arcmenu.ArcMenu.Position}
     * @return aligns of the ArcMenu
     */
    private int[] initPosition(Position pos)
    {
        mPosition = pos;
        if (pos == Position.RIGHT_BOTTOM)
        {
            mAngleRange = 90;
            mOriX = -1;
            mOriY = -1;
            return new int[]{ALIGN_PARENT_RIGHT, ALIGN_PARENT_BOTTOM};
        }
        else if (pos == Position.BOTTOM_CENTER)
        {
            mAngleRange = 180;
            mOriX = -1;
            mOriY = -1;
            return new int[]{CENTER_HORIZONTAL, ALIGN_PARENT_BOTTOM};
        }
        else if (pos == Position.LEFT_BOTTOM)
        {
            mAngleRange = 90;
            mOriX = 1;
            mOriY = -1;
            return new int[]{ALIGN_PARENT_LEFT, ALIGN_PARENT_BOTTOM};
        }
        else if (pos == Position.LEFT_CENTER)
        {
            mAngleRange = 180;
            mOriX = 1;
            mOriY = -1;
            return new int[]{ALIGN_PARENT_LEFT, CENTER_VERTICAL};
        }
        else if (pos == Position.LEFT_TOP)
        {
            mAngleRange = 90;
            mOriX = 1;
            mOriY = 1;
            return new int[]{ALIGN_PARENT_LEFT, ALIGN_PARENT_TOP};
        }
        else if (pos == Position.TOP_CENTER)
        {
            mAngleRange = 180;
            mOriX = -1;
            mOriY = 1;
            return new int[]{CENTER_HORIZONTAL, ALIGN_PARENT_TOP};
        }
        else if (pos == Position.RIGHT_TOP)
        {
            mAngleRange = 90;
            mOriX = -1;
            mOriY = 1;
            return new int[]{ALIGN_PARENT_RIGHT, ALIGN_PARENT_TOP};
        }
        else
        {
            mAngleRange = 180;
            mOriX = -1;
            mOriY = -1;
            return new int[]{ALIGN_PARENT_RIGHT, CENTER_VERTICAL};
        }
    }

    /**
     * @param durationMillis milliseconds of duration for animation
     */
    public void setDuration(int durationMillis)
    {
        mDuration = durationMillis;
    }

    /**
     * Set radius of the ArcMenu
     * @param radius radius ignore density
     */
    public void setRadius(int radius)
    {
        float density = getResources().getDisplayMetrics().density;
        mRadius = (int) (radius * density);
    }

    /**
     * Register a callback to be invoked when an item in this ArcMenu has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
        if (null != mChilds)
        {
            setClickListener();
        }
    }

    /**
     * fold childs
     */
    public void fold()
    {
        startAnimationsOut(mDuration);
        mMenu.startAnimation(getRotateAnimation(-270, 0, mDuration));
        isShowing = false;
    }

    /**
     * show childs
     */
    public void unfold()
    {
        startAnimationsIn(mDuration);
        mMenu.startAnimation(getRotateAnimation(0, -270, mDuration));
        isShowing = true;
    }

    /**
     * Animation fo unfold child
     *
     * @param durationMillis milliseconds of duration
     */
    public void startAnimationsIn(int durationMillis)
    {
        for (byte i = 0; i < mCount; i++)
        {
            double offsetAngle = mAngleRange / (mCount - 1);

            final double deltaY, deltaX;
            if (mPosition == ArcMenu.Position.LEFT_CENTER || mPosition == ArcMenu.Position.RIGHT_CENTER)
            {
                deltaX = Math.sin(offsetAngle * i * Math.PI / 180) * mRadius;
                deltaY = Math.cos(offsetAngle * i * Math.PI / 180) * mRadius;
            }
            else
            {
                deltaY = Math.sin(offsetAngle * i * Math.PI / 180) * mRadius;
                deltaX = Math.cos(offsetAngle * i * Math.PI / 180) * mRadius;
            }

            ViewPropertyAnimator animator = mAnimators.get(i);
            animator.setDuration(durationMillis);
            float x = (float) (mChilds[i].getLeft() + mOriX * deltaX);
            float y = (float) (mChilds[i].getTop() + mOriY * deltaY);
            animator.x(x).y(y);
        }
    }

    /**
     * Animation fo fold child
     *
     * @param durationMillis milliseconds of duration
     */
    public void startAnimationsOut(int durationMillis)
    {
        for (byte i = 0; i < mCount; i++)
        {
            ViewPropertyAnimator animator = mAnimators.get(i);
            animator.setDuration(durationMillis);
            float x = (float) (mChilds[i].getLeft());
            float y = (float) (mChilds[i].getTop());
            animator.x(x).y(y);
        }
    }

    /**
     * Location of the screen :
     * <p>
     * LEFT_TOP, TOP_CENTER, RIGHT_TOP, LEFT_CENTER, RIGHT_CENTER, LEFT_BOTTOM, BOTTOM_CENTER, RIGHT_BOTTOM
     */
    public enum Position
    {
        LEFT_TOP, TOP_CENTER, RIGHT_TOP, LEFT_CENTER, RIGHT_CENTER, LEFT_BOTTOM, BOTTOM_CENTER, RIGHT_BOTTOM
    }

    /**
     * Interface definition for a callback to be invoked when an item in ArcMenu
     * view has been clicked.
     */
    public interface OnItemClickListener
    {
        /**
         * Callback method to be invoked when an item in this ArcMenu has
         * been clicked.
         *
         * @param pos The position of the view in the ArcMenu.
         */
        public void onItemClick(int pos);
    }
}