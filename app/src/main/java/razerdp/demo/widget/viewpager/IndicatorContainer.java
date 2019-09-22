package razerdp.demo.widget.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import razerdp.demo.utils.ToolUtil;
import razerdp.demo.utils.UIHelper;

/**
 * Created by 大灯泡 on 2019/4/24
 * <p>
 * Description：
 */
public class IndicatorContainer extends LinearLayout implements ViewpagerIndicator {
    private static final String TAG = "IndicatorContainer";
    private static final int DEFAULT_INDICATOR_SIZE = 6;

    List<IndicatorView> mIndicators;
    private int currentSelection = -1;

    private int mNormalColor = IndicatorView.NORMAL_COLOR;
    private int mSelectedColor = IndicatorView.SELECTED_COLOR;
    private int indicatorSize = UIHelper.dip2px(DEFAULT_INDICATOR_SIZE);


    public IndicatorContainer(Context context) {
        this(context, null);
    }

    public IndicatorContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER);
    }

    @Override
    public void onDataSizeChange(int dataSize) {
        if (!ToolUtil.isEmpty(mIndicators) && mIndicators.size() == dataSize) return;
        if (dataSize <= 1) {
            removeAllViewsInLayout();
            setVisibility(GONE);
            return;
        }

        setVisibility(VISIBLE);
        if (mIndicators == null) {
            mIndicators = new ArrayList<>();
        }
        mIndicators.clear();

        for (int i = 0; i < dataSize; i++) {
            mIndicators.add(buildIndicator(getContext(), i));
        }
        for (IndicatorView mIndicator : mIndicators) {
            addViewInLayout(mIndicator, -1, mIndicator.getLayoutParams(), true);
        }

        requestLayout();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setSelection(position);
    }

    /**
     * 当前选中的
     */
    public void setSelection(int selection) {
        if (this.currentSelection == selection || !ToolUtil.indexInList(mIndicators, selection))
            return;
        if (!ToolUtil.isEmpty(mIndicators)) {
            this.currentSelection = selection;
            for (IndicatorView mIndicator : mIndicators) {
                mIndicator.setSelected(false);
            }
        }
        mIndicators.get(selection).setSelected(true);
    }

    /**
     * 初始化dotview
     */
    private IndicatorView buildIndicator(Context context, int position) {
        if (getChildCount() > 0) removeAllViewsInLayout();
        IndicatorView mIndicator = new IndicatorView(context);
        mIndicator.setSelected(false);
        mIndicator.setNormalColor(mNormalColor);
        mIndicator.setSelectedColor(mSelectedColor);
        LayoutParams params = new LayoutParams(indicatorSize, indicatorSize);
        mIndicator.setLayoutParams(params);
        if (position == 0) {
            params.leftMargin = 0;
        } else {
            params.leftMargin = UIHelper.dip2px(6f);
        }
        return mIndicator;
    }

    public IndicatorContainer setNormalColor(int mNormalColor) {
        this.mNormalColor = mNormalColor;
        if (!ToolUtil.isEmpty(mIndicators)) {
            for (IndicatorView mIndicator : mIndicators) {
                mIndicator.setNormalColor(mNormalColor);
            }
        }
        return this;
    }

    public IndicatorContainer setSelectedColor(int mSelectedColor) {
        this.mSelectedColor = mSelectedColor;
        if (!ToolUtil.isEmpty(mIndicators)) {
            for (IndicatorView mIndicator : mIndicators) {
                mIndicator.setSelectedColor(mNormalColor);
            }
        }
        return this;
    }

    public void attachViewPager(final ViewPager mViewPager) {
        if (mViewPager == null) return;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                IndicatorContainer.this.onPageScrolled(i, v, i1);
            }

            @Override
            public void onPageSelected(int i) {
                IndicatorContainer.this.onPageSelected(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        PagerAdapter adapter = mViewPager.getAdapter();
        if (adapter != null) {
            onDataSizeChange(adapter.getCount());
            setSelection(mViewPager.getCurrentItem());
        } else {
            mViewPager.addOnAdapterChangeListener(new ViewPager.OnAdapterChangeListener() {
                @Override
                public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter pagerAdapter, @Nullable PagerAdapter pagerAdapter1) {
                    if (pagerAdapter1 != null) {
                        onDataSizeChange(pagerAdapter1.getCount());
                        setSelection(mViewPager.getCurrentItem());
                    }
                }
            });
        }

    }
}
