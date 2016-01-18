# BasePopup

##通过继承顶级类BasePopupWindow来简便的实现各种类型的Popup</br>
##代码解析：</br>
  http://blog.csdn.net/mkfrank/article/details/50522666</br>
</br>
##Demo版本更新日志：</br>
  https://github.com/razerdp/BasePopup/blob/master/UpdateLog.md</br>
##Some Preview Img:</br>
![image](https://github.com/razerdp/BasePopup/blob/master/img/comment_popup_with_exitAnima.gif)
![image](https://github.com/razerdp/BasePopup/blob/master/img/scale_popup.gif)
![image](https://github.com/razerdp/BasePopup/blob/master/img/slide_from_bottom_popup.gif)
![image](https://github.com/razerdp/BasePopup/blob/master/img/input_popup.gif)
</br>
click link to show more:</br>
https://github.com/razerdp/BasePopup/blob/master/UpdateLog.md
 </br>
##用法（Sample）：</br>
step 1:继承BasePopupWindow</br>
step 2:对应实现抽象方法</br>
```java
/**
 * Created by 大灯泡 on 2016/1/15.
 * 普通的popup
 */
public class ScalePopup extends BasePopupWindow implements View.OnClickListener{
    private View popupView;

    public ScalePopup(Activity context) {
        super(context);
        bindEvent();
    }

    @Override
    public Animation getAnimation() {
        return getDefaultScaleAnimation();
    }

    @Override
    public Animator getAnimator() {
        return null;
    }

    @Override
    public View getInputView() {
        return null;
    }

    @Override
    public View getDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View getPopupView() {
        popupView= LayoutInflater.from(mContext).inflate(R.layout.popup_normal,null);
        return popupView;
    }

    @Override
    public View getAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView!=null){
            popupView.findViewById(R.id.tx_1).setOnClickListener(this);
            popupView.findViewById(R.id.tx_2).setOnClickListener(this);
            popupView.findViewById(R.id.tx_3).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tx_1:
                ToastUtils.ToastMessage(mContext,"click tx_1");
                break;
            case R.id.tx_2:
                ToastUtils.ToastMessage(mContext,"click tx_2");
                break;
            case R.id.tx_3:
                ToastUtils.ToastMessage(mContext,"click tx_3");
                break;
            default:
                break;
        }
    }
}
```
</br>
step 3:在您需要用的地方 new出对象并调用 showPopup()或者其重载方法</br>
```java
new ScalePopup(context).showPopupWindow();
```


