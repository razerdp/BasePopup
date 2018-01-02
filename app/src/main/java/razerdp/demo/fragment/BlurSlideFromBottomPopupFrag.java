package razerdp.demo.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.blur.BlurHelper;
import razerdp.blur.PopupBlurOption;
import razerdp.demo.popup.BlurSlideFromBottomPopup;

/**
 * Created by 大灯泡 on 2017/12/27.
 */
public class BlurSlideFromBottomPopupFrag extends SimpleBaseFrag {

    List<ImageView> mImageViews;

    View mSelectedView;
    ImageView mPreviewImageView;
    TextView mTipsTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void bindEvent() {
        mPreviewImageView = (ImageView) findViewById(R.id.iv_preview);
        mTipsTextView = (TextView) findViewById(R.id.tv_tips);
        mImageViews = new ArrayList<>();
        mImageViews.add((ImageView) findViewById(R.id.iv_color_1));
        mImageViews.add((ImageView) findViewById(R.id.iv_color_2));
        mImageViews.add((ImageView) findViewById(R.id.iv_color_3));
        mImageViews.add((ImageView) findViewById(R.id.iv_color_4));
        mImageViews.add((ImageView) findViewById(R.id.iv_color_5));
        mImageViews.add((ImageView) findViewById(R.id.iv_color_6));
        mImageViews.add((ImageView) findViewById(R.id.iv_color_7));
        mImageViews.add((ImageView) findViewById(R.id.iv_color_8));
        mImageViews.add((ImageView) findViewById(R.id.iv_color_9));
        mImageViews.add((ImageView) findViewById(R.id.iv_color_10));
        mImageViews.add((ImageView) findViewById(R.id.iv_color_11));
        mImageViews.add((ImageView) findViewById(R.id.iv_color_12));

        for (ImageView imageView : mImageViews) {
            imageView.setOnClickListener(onSelectedClickListener);
        }
        mTipsTextView.setOnClickListener(onSelectedClickListener);

    }

    @Override
    public BasePopupWindow getPopup() {
        final BlurSlideFromBottomPopup popup = new BlurSlideFromBottomPopup(mContext);
        popup.setOnBeforeShowCallback(new BasePopupWindow.OnBeforeShowCallback() {
            @Override
            public boolean onBeforeShow(View popupRootView, View anchorView, boolean hasShowAnima) {
                if (mSelectedView == null) {
                    popup.setBlurBackgroundEnable(true);
                } else {
                    PopupBlurOption option = new PopupBlurOption();
                    option.setBlurView(mSelectedView)
                            .setFullScreen(false);
                    popup.setBlurOption(option);
                }
                return true;
            }
        });
        return popup;
    }

    @Override
    public Button getButton() {
        return (Button) mFragment.findViewById(R.id.popup_show);
    }

    @Override
    public View getFragment() {
        return mInflater.inflate(R.layout.frag_blur_slide_from_bottom_popup, container, false);
    }

    private View.OnClickListener onSelectedClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mSelectedView) {
                unSelectView(mSelectedView);
                mPreviewImageView.setImageBitmap(null);
                mSelectedView = null;
                return;
            }
            for (ImageView imageView : mImageViews) {
                unSelectView(imageView);
            }
            selectView(v);
            mSelectedView = v;

            mPreviewImageView.setImageBitmap(BlurHelper.getViewBitmap(v, false));

        }
    };

    private void selectView(View v) {
        if (v instanceof ImageView) {
            ImageView iv = ((ImageView) v);
            iv.setAlpha(1f);
            Drawable drawable = iv.getDrawable();
            if (drawable == null) {
                drawable = iv.getBackground();
            }
            if (drawable != null) {
                drawable.setColorFilter(0x31000000, PorterDuff.Mode.SRC_ATOP);
            }
        } else if (v instanceof TextView) {
            ((TextView) v).setTextColor(Color.RED);

        }
    }

    private void unSelectView(View v) {
        if (v instanceof ImageView) {
            ImageView iv = ((ImageView) v);
            iv.setAlpha(1f);
            Drawable drawable = iv.getDrawable();
            if (drawable == null) {
                drawable = iv.getBackground();
            }
            if (drawable != null) {
                drawable.clearColorFilter();
            }
        } else if (v instanceof TextView) {
            ((TextView) v).setTextColor(Color.GRAY);

        }
    }
}
