# BasePopup
抽象出一个方便自定义的Basepopup类


# 依赖
点击 [here](https://github.com/razerdp/BasePopup/tree/master/lib/src/main/java/razerdp/basepopup) 然后复制里面的三个文件到您的工程中，因为并非什么大项目，所以就没有发布上去了。
# 使用方法

----------

**Step 1:**
新建一个类继承Basepopup

**Step 2:**
实现必要的几个方法

例如

```java
public class DialogPopup extends BasePopupWindow {

    public DialogPopup(Activity context) {
        super(context);
    }

    @Override
    protected Animation getShowAnimation() {
        AnimationSet set=new AnimationSet(false);
        Animation shakeAnima=new RotateAnimation(0,15,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        shakeAnima.setInterpolator(new CycleInterpolator(5));
        shakeAnima.setDuration(400);
        set.addAnimation(getDefaultAlphaAnimation());
        set.addAnimation(shakeAnima);
        return set;
    }

    @Override
    protected View getClickToDismissView() {
        return mPopupView;
    }

    @Override
    public View getPopupView() {
        return getPopupViewById(R.layout.popup_dialog);
    }

    @Override
    public View getAnimaView() {
        return mPopupView.findViewById(R.id.popup_anima);
    }
}
```

**Step 3:**把您刚才实现的popup给new出来并调用show方法

例如

```java
 new DialogPopup(context).showPopupWindow();
```

# 一些例子
![image](https://github.com/razerdp/BasePopup/blob/master/img/comment_popup_with_exitAnima.gif)
![image](https://github.com/razerdp/BasePopup/blob/master/img/scale_popup.gif)
![image](https://github.com/razerdp/BasePopup/blob/master/img/slide_from_bottom_popup.gif)
![image](https://github.com/razerdp/BasePopup/blob/master/img/input_popup.gif)
![image](https://github.com/razerdp/BasePopup/blob/master/img/list_popup.gif)
![image](https://github.com/razerdp/BasePopup/blob/master/img/menu_popup.gif)

例子更新日志:

https://github.com/razerdp/BasePopup/blob/master/UpdateLog.md

# 方法介绍：
本项目拥有的方法如下：

 - 必须实现的抽象方法：
	+ getPopupView()：得到popupwindow的主体，一般是在xml文件写好然后inflate出来并返回
	+ getAnimaView()：得到用于展示动画的view，一般为了做到蒙层效果，我们的xml都会给一个灰色半透明的底层然后才是给定展示的popup（详情见demo）
	+ getShowAnimation()：展示popup的动画
	+ getClickToDismissView()：点击触发dismiss的view
 - 非必须实现的公有方法：
	+ getShowAnimator()：同getShowAnimation，不过有些时候用animator更加的丰富
	+ getInputView()：得到给定需要输入的view，一般用于包含edittext的popup
	+ getExitAnimation()：popup执行dismiss时的退出动画
	+ getExitAnimator()：同上
	+ setAutoShowInputMethod()：是否自动弹出输入法
	+ setAdjustInputMethod()：popup是否随着输入法弹出而自适应
	+ getPopupViewById()：工具方法，不用写那么多LayoutInflate.from(context)
 - show方法：
	+ showPopupWindow():默认将popup显示到当前窗口
	+ showPopupWindow(int res)：将popup显示到对应的id控件上
	+ showPopupWindow(View v)：将popup显示到view上

# 代码解析：
http://www.jianshu.com/p/069f57e14a9c

#License
MIT
