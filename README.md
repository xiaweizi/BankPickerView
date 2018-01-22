## 前言

年前的最后一个开发需求，将之前`H5`开卡界面转变成`native`。意思就是开卡这个需求做成`Android`原生的界面，就这单单一个界面需要请求 **8** 个接口！可想逻辑是有多复杂，其中中间有个小需求-「选择银行卡」。来看一下`UI`出图。

![设计图](http://upload-images.jianshu.io/upload_images/4043475-5f50c0e7819ed245.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

大致就是点击「银行」弹出`popupWindow`，在里面嵌套一个银行选择器。这个「银行选择器」可以滚动类似`IOS`那边的`UIPickerView`，或者安卓的日期选择器，但是为了完成特殊的定制效果，所以咱们得自己写一个。[BankPickerView](https://github.com/xiaweizi/BankPickerView)

看一下效果图：

![BankPickerView.gif](http://upload-images.jianshu.io/upload_images/4043475-42f7f21a686f03da.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 实现

思路其实也很简单。

**滚动**

`ScrollView`、`ListView`和`RecyclerView`都可以实现滚动，这自然不用说，首尾都有`offset`个位置是留占坑，可以添加空数据作为占坑的显示。

**回弹**

那就在手指松开的时候，拿到滑动的距离，通过一系列的逻辑处理，回弹到最近的`Item`处即可。

**回调**

在滑动停止的时候，通过计算获取当前所处的`item`的位置，通过`listener`回调给调用者。

因为时间有限，所以没对其进行封装，如果想使用请自行下载，通过修改源码满足特定的需求。

至于弹出的效果，用的是`popupWindow`,其中遇到了一下坑，在这里记录并分享一下。

**背景透明**

`popupWindow`不像`dialog`本身是没有背景透明的效果的，需要自己实现。主要就是两种实现方式。

    <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:skin="http://schemas.android.com/android/skin"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#7D000000"
        skin:enable="true">
    
            <!-- 内容 -->
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:background="#fff"
                android:layout_alignParentBottom="true"
                android:orientation="vertical">
            </LinerarLayout>
     </RelativeLayout>

一种就是直接在`pooupWidnow`展示的布局强行假如半透明的背景。

第二种获取`WindowManager`修改当前窗口的透明值。

**点击返回关闭 popupWindow**

首先如果想实现点击返回让`popupWindow`消失，就得实现`View.OnKeyListener`接口，并且在回调中，让其消失。

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
            return true;
        }
        return false;
    }

但是你会发现并没有生效，那是因为当前没有获取到焦点，需要设置`setFocusable(true)`即可。