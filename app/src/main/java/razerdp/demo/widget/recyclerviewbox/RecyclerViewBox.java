package razerdp.demo.widget.recyclerviewbox;

import android.content.Context;
import android.database.Observable;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import razerdp.demo.utils.SimplePool;
import razerdp.util.log.PopupLog;


/**
 * Created by 大灯泡 on 2019/7/1
 * <p>
 * Description：
 */
public class RecyclerViewBox extends ViewGroup {
    private static final String TAG = "RecyclerViewBox";

    RecyclerViewBox.LayoutManager mLayout;
    SimplePool<ViewHolder> mPools;
    State mState;
    Adapter mAdapter;
    AdapterDataObserver mObserver;


    public RecyclerViewBox(Context context) {
        this(context, null);
    }

    public RecyclerViewBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mObserver = new RecyclerViewBoxObserver();
        mPools = new SimplePool<>(9);
        mState = new State();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mLayout == null) {
            defaultOnMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            preventRequestLayout();
            mState.itemCount = mAdapter.getItemCount();

            if (mState.itemCount == 0 || mAdapter == null) {
                doRecycler();
                defaultOnMeasure(widthMeasureSpec, heightMeasureSpec);
                resumeRequestLayout();
                return;
            }
            mLayout.onMeasure(mPools, mState, widthMeasureSpec, heightMeasureSpec);
            resumeRequestLayout();
        }
    }

    void defaultOnMeasure(int widthSpec, int heightSpec) {
        int width = LayoutManager.chooseSize(widthSpec, this.getPaddingLeft() + this.getPaddingRight(), ViewCompat.getMinimumWidth(this));
        int height = LayoutManager.chooseSize(heightSpec, this.getPaddingTop() + this.getPaddingBottom(), ViewCompat.getMinimumHeight(this));
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        preventRequestLayout();
        if (mAdapter == null) {
            doRecycler();
            resumeRequestLayout();
            return;
        }
        if (mLayout == null) {
            resumeRequestLayout();
            return;
        }
        mState.setBounds(getPaddingLeft(), getPaddingTop(), r - getPaddingRight(), b - getPaddingBottom());
        mLayout.onLayoutChildren(mPools, mState);
        mState.dataChanged = false;
        resumeRequestLayout();
    }

    private void doRecycler() {
        final int childCount = getChildCount();
        if (childCount <= 0) return;
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            ViewGroup.LayoutParams p = v.getLayoutParams();
            if (!checkLayoutParams(p)) continue;
            ViewHolder holder = ((LayoutParams) p).mViewHolder;
            if (holder == null) continue;
            boolean intercept = mLayout != null && mLayout.onInterceptRecycle(childCount, i, holder);
            if (!intercept) {
                mPools.release(holder);
            }
            holder.onRecycled();
        }
        detachAllViewsFromParent();
    }


    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public Adapter getAdapter() {
        return mAdapter;
    }

    public RecyclerViewBox setAdapter(Adapter adapter) {
        if (this.mAdapter == adapter) return this;
        if (this.mAdapter != null) {
            this.mAdapter.unregisterAdapterDataObserver(mObserver);
            this.mAdapter.onDetachedFromRecyclerViewBox(this);
        }
        doRecycler();
        mPools.clearPool(new SimplePool.OnClearListener<ViewHolder>() {
            @Override
            public void onClear(ViewHolder cached) {
                cached.onDead();
            }
        });

        this.mAdapter = adapter;
        if (adapter != null) {
            adapter.registerAdapterDataObserver(mObserver);
            adapter.onAttachedToRecyclerViewBox(this);
        }
        mState.dataChanged = true;
        return this;
    }

    public RecyclerViewBox setLayoutManager(LayoutManager manager) {
        if (this.mLayout != null) {
            doRecycler();
            this.mLayout = null;
        }
        this.mLayout = manager;
        mLayout.bindRecyclerViewBox(this);
        requestLayout();

        return this;
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public SimplePool<ViewHolder> getRecyclerPools() {
        return mPools;
    }

    private void preventRequestLayout() {
        mState.preventRequestLayout++;
    }

    private void resumeRequestLayout() {
        mState.preventRequestLayout--;
        if (mState.preventRequestLayout < 0) {
            mState.preventRequestLayout = 0;
        }
    }

    @Override
    public void requestLayout() {
//        if (mState == null || mState.preventRequestLayout != 0) {
//            NELog.i(TAG,"preventRequest");
//            return;
//        }
        super.requestLayout();
    }

    public State getState() {
        return mState;
    }

    @Nullable
    public ViewHolder findViewHolderForPosition(int position) {
        View v = getChildAt(position);
        if (v == null) return null;
        if (!checkLayoutParams(v.getLayoutParams())) return null;
        return ((LayoutParams) v.getLayoutParams()).mViewHolder;

    }

    public abstract static class LayoutManager {

        RecyclerViewBox mRecyclerViewBox;

        public abstract void onMeasure(@NonNull SimplePool<ViewHolder> pool, @NonNull State state, int widthSpec, int heightSpec);

        public abstract void onLayoutChildren(@NonNull SimplePool<ViewHolder> pool, @NonNull State state);

        public static int chooseSize(int spec, int desired, int min) {
            int mode = MeasureSpec.getMode(spec);
            int size = MeasureSpec.getSize(spec);
            switch (mode) {
                case MeasureSpec.AT_MOST:
                    return Math.min(size, Math.max(desired, min));
                case MeasureSpec.EXACTLY:
                    return size;
                case MeasureSpec.UNSPECIFIED:
                default:
                    return Math.max(desired, min);
            }
        }

        protected void setMeasuredDimension(int widthSize, int heightSize) {
            this.mRecyclerViewBox.setMeasuredDimension(widthSize, heightSize);
        }

        private void bindRecyclerViewBox(RecyclerViewBox mRecyclerViewBox) {
            boolean call = this.mRecyclerViewBox != mRecyclerViewBox;
            this.mRecyclerViewBox = mRecyclerViewBox;
            if (call) {
                onAttachedRecyclerViewBox(mRecyclerViewBox);
            }
        }

        public void requestLayout() {
            this.mRecyclerViewBox.requestLayout();
        }

        public RecyclerViewBox getParent() {
            return mRecyclerViewBox;
        }

        public int getChildCount() {
            return mRecyclerViewBox.getChildCount();
        }

        public void addView(View child) {
            this.addView(child, -1);
        }

        public void addView(View child, int index) {
            this.addView(child, index, null);
        }


        public void addView(View child, int index, LayoutParams p) {
            this.addView(child, index, p, false);
        }

        public void addView(View child, int index, LayoutParams p, boolean preventRequestLayout) {
            if (preventRequestLayout) {
                mRecyclerViewBox.addViewInLayout(child, index, p, true);
            } else {
                mRecyclerViewBox.addView(child, index, p);
            }
        }


        public void removeViewAt(int index) {
            View child = this.getChildAt(index);
            if (child != null) {
                mRecyclerViewBox.removeViewAt(index);
            }

        }

        public void removeAllViews() {
            int childCount = mRecyclerViewBox.getChildCount();
            for (int i = childCount - 1; i >= 0; --i) {
                mRecyclerViewBox.removeViewAt(i);
            }
        }

        public void detachView(@NonNull View child) {
            mRecyclerViewBox.detachViewFromParent(child);
        }

        public void detachViewAt(int index) {
            mRecyclerViewBox.detachViewsFromParent(index, 1);
        }

        public LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
            if (lp instanceof LayoutParams) {
                return new LayoutParams((LayoutParams) lp);
            } else {
                return lp instanceof MarginLayoutParams ? new LayoutParams((MarginLayoutParams) lp) : new LayoutParams(lp);
            }
        }

        public View getChildAt(int index) {
            return mRecyclerViewBox.getChildAt(index);
        }

        public void onAttachedRecyclerViewBox(RecyclerViewBox recyclerViewBox) {

        }

        public boolean onInterceptRecycle(int childCount, int position, @NonNull ViewHolder holder) {
            return false;
        }

        protected ViewHolder obtainViewHolder(int position) {
            ViewHolder result = mRecyclerViewBox.mPools.acquire();
            if (result == null) {
                result = createFromAdapter(position);
            }
            onPrepareViewHolder(result, position);
            return result;
        }

        protected ViewHolder createFromAdapter(int position) {
            PopupLog.d(TAG, mRecyclerViewBox, "createViewHolder  " + mRecyclerViewBox.mState.getItemCount());
            return mRecyclerViewBox.getAdapter().createViewHolder(mRecyclerViewBox, position);
        }

        protected void onPrepareViewHolder(ViewHolder holder, int position) {
            ViewGroup.LayoutParams lp = holder.rootView.getLayoutParams();
            LayoutParams p;

            if (lp == null) {
                p = generateDefaultLayoutParams();
                holder.rootView.setLayoutParams(p);
            } else if (!checkLayoutParams(lp)) {
                p = generateLayoutParams(lp);
                holder.rootView.setLayoutParams(p);
            } else {
                p = (LayoutParams) lp;
            }

            holder.position = position;
            p.mViewHolder = holder;
        }

        public LayoutParams generateDefaultLayoutParams() {
            return mRecyclerViewBox.generateDefaultLayoutParams();
        }

        protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
            return mRecyclerViewBox.checkLayoutParams(p);
        }

        protected void doRecycler() {
            mRecyclerViewBox.doRecycler();
        }

        protected ViewHolder findViewHolderForView(View v) {
            if (!checkLayoutParams(v.getLayoutParams())) {
                throw new IllegalArgumentException("View的layoutparams不正确");
            }

            return ((LayoutParams) v.getLayoutParams()).mViewHolder;

        }
    }


    public abstract static class AdapterDataObserver {
        public void onChanged() {

        }
    }

    private class RecyclerViewBoxObserver extends AdapterDataObserver {
        @Override
        public void onChanged() {
            mState.reset();
            doRecycler();
            requestLayout();
        }
    }

    static class AdapterDataObservable extends Observable<AdapterDataObserver> {
        AdapterDataObservable() {
        }

        public boolean hasObservers() {
            return !this.mObservers.isEmpty();
        }

        public void notifyChanged() {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                this.mObservers.get(i).onChanged();
            }
        }
    }

    public abstract static class Adapter<VH extends ViewHolder> {
        private final AdapterDataObservable mObservable = new AdapterDataObservable();

        public void registerAdapterDataObserver(@NonNull AdapterDataObserver observer) {
            this.mObservable.registerObserver(observer);
        }

        public void unregisterAdapterDataObserver(@NonNull AdapterDataObserver observer) {
            this.mObservable.unregisterObserver(observer);
        }

        public void onViewRecycled(@NonNull VH holder) {
        }


        public void onAttachedToRecyclerViewBox(@NonNull RecyclerViewBox recyclerViewBox) {
        }

        public void onDetachedFromRecyclerViewBox(@NonNull RecyclerViewBox recyclerViewBox) {
        }

        public abstract int getItemCount();

        public abstract VH onCreateViewHolder(ViewGroup parent, int position);

        public abstract void onBindViewHolder(VH viewHolder, int position);

        public final VH createViewHolder(ViewGroup parent, int position) {
            VH holder = onCreateViewHolder(parent, position);
            if (holder.rootView == null) {
                throw new NullPointerException("ViewHolder的rootView不能为空");
            }
            if (holder.rootView.getParent() != null) {
                throw new IllegalArgumentException("ViewHolder的rootView不能有Parent");
            }
            return holder;
        }

        public final void bindViewHolder(@NonNull VH viewHolder, int position) {
            viewHolder.position = position;
            onBindViewHolder(viewHolder, position);
        }


        public final void notifyDataSetChanged() {
            this.mObservable.notifyChanged();
        }
    }

    public abstract static class ViewHolder {
        public final View rootView;
        int position;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
        }

        public int getPosition() {
            return position;
        }

        public void onRecycled() {

        }

        public void onDead() {

        }

        public final <V extends View> V findViewById(int resid) {
            if (resid > 0 && rootView != null) {
                return rootView.findViewById(resid);
            }
            return null;
        }
    }

    public static class LayoutParams extends MarginLayoutParams {
        ViewHolder mViewHolder;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    public static class State {

        int preventRequestLayout;
        boolean dataChanged;
        int itemCount;
        Rect rect = new Rect();
        Rect tempRect = new Rect();

        void setBounds(int l, int t, int r, int b) {
            rect.set(l, t, r, b);
        }

        public boolean isDataChanged() {
            return dataChanged;
        }

        public int getItemCount() {
            return itemCount;
        }

        public Rect getBounds() {
            tempRect.set(rect);
            return tempRect;
        }

        void reset() {
            dataChanged = true;
            itemCount = 0;
            rect.setEmpty();
            tempRect.setEmpty();
        }

        @Override
        public String toString() {
            return "State{" +
                    "preventRequestLayout=" + preventRequestLayout +
                    ", dataChanged=" + dataChanged +
                    ", itemCount=" + itemCount +
                    ", rect=" + rect +
                    ", tempRect=" + tempRect +
                    '}';
        }
    }
}
