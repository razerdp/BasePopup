package razerdp.demo.popup.issue;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.utils.ButterKnifeUtil;
import razerdp.demo.widget.DPTextView;

/**
 * Created by 大灯泡 on 2020/02/11.
 */
public class PopupIssue236 extends BasePopupWindow {
    @BindView(R.id.tv_go)
    DPTextView mTvGo;

    public PopupIssue236(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
    }

    @Override
    public void onViewCreated(View contentView) {
        ButterKnifeUtil.bind(this, contentView);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_issue_236);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(-1f, 0f, 500);
    }


    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0f, -1f, 500);
    }

    @OnClick(R.id.tv_go)
    @Override
    public void dismiss() {
        super.dismiss();
    }
}
