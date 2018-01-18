package com.xiaweizi.bankpickerview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : xiaweizi
 *     class  : com.xiaweizi.bankpickerview.BankPickerView
 *     e-mail : 1012126908@qq.com
 *     time   : 2018/01/14
 *     desc   : 银行选择 view
 * </pre>
 */

public class BankPickerView extends ScrollView {
    public static final String TAG = "BankPickerView:";

    public interface OnWheelViewListener {
        void onSelected(int selectedIndex, BankModel item);
    }

    private Context context;
    private LinearLayout views;
    private int displayItemCount; // 每页显示的数量
    private int selectedIndex = 1;
    private List<BankModel> items;
    public static final int OFF_SET_DEFAULT = 1;
    private int offset = OFF_SET_DEFAULT; // 偏移量（需要在最前面和最后面补全）
    private int initialY;
    private Runnable scrollerTask;
    private int newCheck = 50;
    private int itemHeight = 0;

    private int scrollDirection = -1;
    private static final int SCROLL_DIRECTION_UP = 0;
    private static final int SCROLL_DIRECTION_DOWN = 1;

    private Paint paint;
    private int viewWidth;
    /**
     * 获取选中区域的边界
     */
    private int[] selectedAreaBorder;

    public BankPickerView(Context context) {
        super(context);
        init(context);
    }

    public BankPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BankPickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private List<BankModel> getItems() {
        return items;
    }

    public void setItems(List<BankModel> list) {
        if (null == items) {
            items = new ArrayList<>();
        }
        items.clear();
        items.addAll(list);
        // 前面和后面补全
        for (int i = 0; i < offset; i++) {
            // 添加一条空数据
            items.add(0, null);
            items.add(null);
        }
        Log.i(TAG, "size:" + items.size());
        initData();
    }


    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    private void init(Context context) {
        this.context = context;

        Log.d(TAG, "parent: " + this.getParent());
        this.setVerticalScrollBarEnabled(false);
        views = new LinearLayout(context);
        views.setOrientation(LinearLayout.VERTICAL);
        this.addView(views);

        scrollerTask = new Runnable() {

            public void run() {
                int newY = getScrollY();
                if (initialY - newY == 0) { // stopped
                    final int remainder = initialY % itemHeight;
                    final int divided = initialY / itemHeight;
                    if (remainder == 0) {
                        selectedIndex = divided + offset;

                        onSelectedCallBack();
                    } else {
                        if (remainder > itemHeight / 2) {
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    smoothScrollTo(0, initialY - remainder + itemHeight);
                                    selectedIndex = divided + offset + 1;
                                    onSelectedCallBack();
                                }
                            });
                        } else {
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    smoothScrollTo(0, initialY - remainder);
                                    selectedIndex = divided + offset;
                                    onSelectedCallBack();
                                }
                            });
                        }
                    }
                } else {
                    initialY = getScrollY();
                    postDelayed(scrollerTask, newCheck);
                }
            }
        };


    }

    public void startScrollerTask() {
        initialY = getScrollY();
        this.postDelayed(scrollerTask, newCheck);
    }

    private void initData() {
        displayItemCount = offset * 2 + 1;
        views.removeAllViews();
        for (BankModel item : items) {
            views.addView(createView(item));
        }
        refreshItemView(0);
    }

    private View createView(BankModel item) {
        View view = View.inflate(getContext(), R.layout.item_bank_info, null);
        ImageView logo = view.findViewById(R.id.iv_bank_logo);
        TextView name = view.findViewById(R.id.tv_bank_name);
        TextView desc = view.findViewById(R.id.tv_bank_description);
        if (item != null) {
            Glide.with(getContext()).load(item.bankLogo).into(logo);
            name.setText(item.bankName);
            desc.setText(item.bankDesc);
        }
        if (0 == itemHeight) {
            itemHeight = getViewMeasuredHeight(view);
            Log.d(TAG, "itemHeight: " + itemHeight);
            views.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * displayItemCount));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.getLayoutParams();
            this.setLayoutParams(new LinearLayout.LayoutParams(lp.width, itemHeight * displayItemCount));
        }

        return view;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        refreshItemView(t);
        if (t > oldt) {
//            Log.d(TAG, "向下滚动");
            scrollDirection = SCROLL_DIRECTION_DOWN;
        } else {
//            Log.d(TAG, "向上滚动");
            scrollDirection = SCROLL_DIRECTION_UP;

        }
    }

    private void refreshItemView(int y) {
        int position = y / itemHeight + offset;
        int remainder = y % itemHeight;
        int divided = y / itemHeight;

        if (remainder == 0) {
            position = divided + offset;
        } else {
            if (remainder > itemHeight / 2) {
                position = divided + offset + 1;
            }
        }

        int childSize = views.getChildCount();
        for (int i = 0; i < childSize; i++) {
            if (i == position) {
                View itemView = views.getChildAt(i);
                if (itemView != null) {
                    itemView.setAlpha(1f);
                }
            } else if (i == position - 1 || i == position + 1){
                View view1 = views.getChildAt(position - 1);
                View view2 = views.getChildAt(position + 1);
                if (view1 != null) {
                    view1.setAlpha(0.65f);
                }
                if (view2 != null) {
                    view2.setAlpha(0.65f);
                }
            } else {
                View itemView = views.getChildAt(i);
                if (itemView != null) {
                    itemView.setAlpha(0.4f);
                }
            }
        }
    }


    private int[] obtainSelectedAreaBorder() {
        if (null == selectedAreaBorder) {
            selectedAreaBorder = new int[2];
            selectedAreaBorder[0] = itemHeight * offset;
            selectedAreaBorder[1] = itemHeight * (offset + 1);
        }
        return selectedAreaBorder;
    }


    @Override
    public void setBackgroundDrawable(Drawable background) {

        if (viewWidth == 0 && context != null) {
            viewWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
            Log.d(TAG, "viewWidth: " + viewWidth);
        }

        if (null == paint) {
            paint = new Paint();
            paint.setColor(0xffff);
            paint.setStrokeWidth(dip2px(1f));
        }

        background = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                canvas.drawLine(0, obtainSelectedAreaBorder()[0], viewWidth, obtainSelectedAreaBorder()[0], paint);
                canvas.drawLine(0, obtainSelectedAreaBorder()[1], viewWidth, obtainSelectedAreaBorder()[1], paint);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter cf) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.UNKNOWN;
            }
        };


        super.setBackgroundDrawable(background);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "w: " + w + ", h: " + h + ", oldw: " + oldw + ", oldh: " + oldh);
        viewWidth = w;
        setBackgroundDrawable(null);
    }

    /**
     * 选中回调
     */
    private void onSelectedCallBack() {
        if (null != onWheelViewListener && selectedIndex < items.size()) {
            onWheelViewListener.onSelected(selectedIndex, items.get(selectedIndex));
        }

    }

    public void setSeletion(int position) {
        final int p = position;
        selectedIndex = p + offset;
        this.post(new Runnable() {
            @Override
            public void run() {
                smoothScrollTo(0, p * itemHeight);
            }
        });

    }

    public BankModel getSeletedItem() {
        return items.get(selectedIndex);
    }

    public int getSeletedIndex() {
        return selectedIndex - offset;
    }


    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            startScrollerTask();
        }
        return super.onTouchEvent(ev);
    }

    private OnWheelViewListener onWheelViewListener;

    public OnWheelViewListener getOnWheelViewListener() {
        return onWheelViewListener;
    }

    public void setOnWheelViewListener(OnWheelViewListener onWheelViewListener) {
        this.onWheelViewListener = onWheelViewListener;
    }

    private int dip2px(float dpValue) {
        if (context == null) {
            return 1;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getViewMeasuredHeight(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
        return view.getMeasuredHeight();
    }

}
