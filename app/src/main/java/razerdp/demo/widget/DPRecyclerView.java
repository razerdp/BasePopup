package razerdp.demo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Space;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import razerdp.demo.base.baseadapter.FixedViewInfo;
import razerdp.demo.base.baseadapter.HeaderViewWrapperAdapter;
import razerdp.demo.utils.ToolUtil;

import static razerdp.demo.base.baseadapter.FixedViewInfo.*;


/**
 * Created by 大灯泡 on 2019/8/13
 * <p>
 * Description：header/footer recyclerview
 */
public class DPRecyclerView extends RecyclerView {
    private static final String TAG = "NERecyclerView";

    Space space;

    public DPRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public DPRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DPRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        space = new Space(context);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                space.scrollBy(dx, dy);
            }
        });
    }


    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        if (!(adapter instanceof HeaderViewWrapperAdapter) && (mHeaderViewInfos.size() > 0 || mFooterViewInfos.size() > 0)) {
            adapter = wrapHeaderRecyclerViewAdapterInternal(adapter, mHeaderViewInfos, mFooterViewInfos);
        }
        super.setAdapter(adapter);
    }

    public void setAdapter(@Nullable Adapter adapter, RecyclerView.AdapterDataObserver observer) {
        if (!(adapter instanceof HeaderViewWrapperAdapter) && (mHeaderViewInfos.size() > 0 || mFooterViewInfos.size() > 0)) {
            adapter = wrapHeaderRecyclerViewAdapterInternal(adapter, mHeaderViewInfos, mFooterViewInfos);
        }
        if (observer != null && adapter != null) {
            try {
                adapter.registerAdapterDataObserver(observer);
            } catch (Exception e) {
                //捕捉注册观察者的exception，但不需要处理
            }
        }
        super.setAdapter(adapter);
    }

    public Adapter getOriginalAdapter() {
        Adapter result = getAdapter();
        if (result instanceof HeaderViewWrapperAdapter) {
            return ((HeaderViewWrapperAdapter) result).getWrappedAdapter();
        }
        return result;
    }

    //region header & footer
    private ArrayList<FixedViewInfo> mHeaderViewInfos = new ArrayList<>();
    private ArrayList<FixedViewInfo> mFooterViewInfos = new ArrayList<>();

    public void addHeaderView(View headerView) {
        addHeaderView(headerView, 0, 0);
    }

    public void addHeaderView(View headerView, int width, int height) {
        final FixedViewInfo info = new FixedViewInfo(headerView, ITEM_VIEW_TYPE_HEADER_START - mHeaderViewInfos.size())
                .setWidth(width)
                .setHeight(height);
        if (mHeaderViewInfos.size() == Math.abs(ITEM_VIEW_TYPE_FOOTER_START - ITEM_VIEW_TYPE_HEADER_START)) {
            mHeaderViewInfos.remove(mHeaderViewInfos.size() - 1);
        }
        if (checkFixedViewInfoNotAdded(info, mHeaderViewInfos)) {
            mHeaderViewInfos.add(info);
        }
        checkAndNotifyWrappedViewAdd(getAdapter(), info, true);
    }

    private void checkAndNotifyWrappedViewAdd(RecyclerView.Adapter adapter, FixedViewInfo info, boolean isHeader) {
        //header和footer只能再setAdapter前使用，如果是set了之后再用，为何不add普通的viewholder而非要Headr或者footer呢
        if (adapter != null) {
            if (!(adapter instanceof HeaderViewWrapperAdapter)) {
                adapter = wrapHeaderRecyclerViewAdapterInternal(adapter);
                if (isHeader) {
                    adapter.notifyItemInserted(((HeaderViewWrapperAdapter) adapter).findHeaderPosition(info.view));
                } else {
                    adapter.notifyItemInserted(((HeaderViewWrapperAdapter) adapter).findFooterPosition(info.view));
                }
            }
        }
    }

    public void addFooterView(View footerView) {
        addFooterView(footerView, 0, 0);
    }

    public void addFooterView(View footerView, int width, int height) {
        final FixedViewInfo info = new FixedViewInfo(footerView, ITEM_VIEW_TYPE_FOOTER_START - mFooterViewInfos.size())
                .setWidth(width)
                .setHeight(height);
        if (checkFixedViewInfoNotAdded(info, mFooterViewInfos)) {
            mFooterViewInfos.add(info);
        }
        checkAndNotifyWrappedViewAdd(getAdapter(), info, false);
    }

    private boolean checkFixedViewInfoNotAdded(FixedViewInfo info, List<FixedViewInfo> infoList) {
        boolean result = true;
        if (ToolUtil.isEmpty(infoList) || info == null) {
            result = true;
        } else {
            for (FixedViewInfo fixedViewInfo : infoList) {
                if (fixedViewInfo.view == info.view) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public int getHeaderViewsCount() {
        return mHeaderViewInfos.size();
    }

    public int getFooterViewsCount() {
        return mFooterViewInfos.size();
    }


    protected HeaderViewWrapperAdapter wrapHeaderRecyclerViewAdapterInternal(@NonNull RecyclerView.Adapter mWrappedAdapter,
                                                                             ArrayList<FixedViewInfo> mHeaderViewInfos,
                                                                             ArrayList<FixedViewInfo> mFooterViewInfos) {
        return new HeaderViewWrapperAdapter(this, mWrappedAdapter, mHeaderViewInfos, mFooterViewInfos);

    }

    protected HeaderViewWrapperAdapter wrapHeaderRecyclerViewAdapterInternal(@NonNull RecyclerView.Adapter mWrappedAdapter) {
        return wrapHeaderRecyclerViewAdapterInternal(mWrappedAdapter, mHeaderViewInfos, mFooterViewInfos);
    }

    //endregion
    public int getScrolledX() {
        return space.getScrollX();
    }

    public int getScrolledY() {
        return space.getScrollY();
    }
}
