package razerdp.demo.ui;

import android.content.Intent;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.utils.DescBuilder;
import razerdp.demo.utils.UIHelper;
import razerdp.util.animation.AlphaConfig;
import razerdp.util.animation.AnimationHelper;

/**
 * Created by 大灯泡 on 2021/6/18
 * <p>
 * Description：
 */
public class AutoMirrorActivity extends BaseActivity {
    public static final String DESC = DescBuilder.get()
            .append("自动镜像定位")
            .build();

    int gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
    @BindView(R.id.rd_group)
    RadioGroup rdGroup;
    @BindView(R.id.tv_show)
    View show;

    DemoPopup demoPopup;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int contentViewLayoutId() {
        return R.layout.activity_mirror;
    }

    @Override
    protected void onInitView(View decorView) {
        rdGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rd_top:
                    gravity = Gravity.TOP;
                    break;
                case R.id.rd_right:
                    gravity = Gravity.RIGHT;
                    break;
                case R.id.rd_bottom:
                    gravity = Gravity.BOTTOM;
                    break;
                case R.id.rd_left:
                default:
                    gravity = Gravity.LEFT;
                    break;
            }
            gravity |= Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        });
        show.setOnTouchListener(new View.OnTouchListener() {
            float x, y;
            boolean isInLongClick;

            boolean isInLongClick(MotionEvent event) {
                if (isInLongClick) return true;
                float offsetX = Math.abs(event.getX() - x);
                float offsetY = Math.abs(event.getY() - y);
                return offsetX <= 10 && offsetY <= 10 && event.getEventTime() - event.getDownTime() >= 220;
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        y = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isInLongClick) {
                            isInLongClick = false;
                            v.setPressed(false);
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!isInLongClick) {
                            isInLongClick = isInLongClick(event);
                        }
                        if (isInLongClick) {
                            float offsetX = event.getX() - x;
                            float offsetY = event.getY() - y;
                            v.offsetLeftAndRight((int) offsetX);
                            v.offsetTopAndBottom((int) offsetY);
                        }
                        break;
                }
                return false;
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (demoPopup == null) {
                    demoPopup = new DemoPopup(v.getContext());
                    demoPopup.setAutoMirrorEnable(true);
                    demoPopup.setShowAnimation(AnimationHelper.asAnimation()
                            .withAlpha(AlphaConfig.IN)
                            .toShow());
                    demoPopup.setDismissAnimation(AnimationHelper.asAnimation()
                            .withAlpha(AlphaConfig.OUT)
                            .toDismiss());
                    demoPopup.setLayoutListener(layoutListener);
                }
                demoPopup.setPopupGravity(gravity);
                demoPopup.showPopupWindow(v);
            }
        });
    }

    DemoPopup.OnPopupLayoutListener layoutListener = new DemoPopup.OnPopupLayoutListener() {

        String gravityStr(int gravity) {
            String result = "";
            switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                case Gravity.LEFT:
                    result += "左";
                    break;
                case Gravity.RIGHT:
                    result += "右";
                    break;
            }
            switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                case Gravity.TOP:
                    result += "上";
                    break;
                case Gravity.BOTTOM:
                    result += "下";
                    break;
            }
            return result;
        }

        @Override
        public void onPopupLayout(@NonNull @NotNull Rect popupRect, @NonNull @NotNull Rect anchorRect) {
            int gravity = demoPopup.computeGravity(popupRect, anchorRect);
            int originGravity = demoPopup.getPopupGravity();
//            gravity &= ~Gravity.CENTER_HORIZONTAL;
//            gravity &= ~Gravity.CENTER_VERTICAL;
//
//            originGravity &= ~Gravity.CENTER_HORIZONTAL;
//            originGravity &= ~Gravity.CENTER_VERTICAL;

            if (gravity != originGravity) {
                UIHelper.toast(String.format("%s方位置不足，已经调整显示到%s方", gravityStr(originGravity), gravityStr(gravity)));
            }
        }
    };

}
