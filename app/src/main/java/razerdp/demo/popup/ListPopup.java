package razerdp.demo.popup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;

/**
 * Created by 大灯泡 on 2016/1/20.
 * 包含着listview的popup，使用builder模式，事件与tag进行绑定
 */
public class ListPopup extends BasePopupWindow {

    private ListView mListView;
    private OnListPopupItemClickListener mOnListPopupItemClickListener;

    private ListPopup(Activity context) {
        super(context);
    }
    private ListPopup(Activity context,Builder builder){
        this(context);
        mListView= (ListView) findViewById(R.id.popup_list);
        setAdapter(context,builder);
    }

    public static class Builder{
        private List<Object> mItemEventList=new ArrayList<>();
        private Activity mContext;

        public Builder(Activity context) {
            mContext = context;
        }
        public Builder addItem(String itemTx){
            mItemEventList.add(itemTx);
            return this;
        }
        public Builder addItem(int clickTag,String itemTx){
            mItemEventList.add(new clickItemEvent(clickTag,itemTx));
            return this;
        }

        public List<Object> getItemEventList() {
            return mItemEventList;
        }

        public ListPopup build(){
            return new ListPopup(mContext,this);
        }

    }

    public static class clickItemEvent{
        private int clickTag;
        private String itemTx;

        public clickItemEvent(int clickTag, String itemTx) {
            this.clickTag = clickTag;
            this.itemTx = itemTx;
        }

        public int getClickTag() {
            return clickTag;
        }

        public void setClickTag(int clickTag) {
            this.clickTag = clickTag;
        }

        public String getItemTx() {
            return itemTx;
        }

        public void setItemTx(String itemTx) {
            this.itemTx = itemTx;
        }
    }

    //=============================================================adapter
    class ListPopupAdapter extends BaseAdapter{
        private LayoutInflater mInflater;
        private Context mContext;
        private List<Object> mItemList;

        public ListPopupAdapter(Context context, @NonNull List<Object> itemList) {
            mContext = context;
            mItemList = itemList;
            mInflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mItemList.size();
        }

        @Override
        public String getItem(int position) {
            if (mItemList.get(position) instanceof String){
                return (String) mItemList.get(position);
            }
            if (mItemList.get(position) instanceof clickItemEvent){
                return ((clickItemEvent) mItemList.get(position)).getItemTx();
            }
            return "";
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh=null;
            if (convertView==null){
                vh=new ViewHolder();
                convertView=mInflater.inflate(R.layout.item_popup_list,parent,false);
                vh.mTextView= (TextView) convertView.findViewById(R.id.item_tx);
                convertView.setTag(vh);
            }else {
                vh= (ViewHolder) convertView.getTag();
            }
            vh.mTextView.setText(getItem(position));
            return convertView;
        }

        public List<Object> getItemList(){
            return this.mItemList;
        }


        class ViewHolder{
            public TextView mTextView;
        }
    }

    //=============================================================init
    private void setAdapter(Activity context, Builder builder) {
        if (builder.getItemEventList()==null||builder.getItemEventList().size()==0)return;
        final ListPopupAdapter adapter=new ListPopupAdapter(context,builder.getItemEventList());
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnListPopupItemClickListener!=null){
                    Object clickObj=adapter.getItemList().get(position);
                    if (clickObj instanceof String){
                        mOnListPopupItemClickListener.onItemClick(position);
                    }
                    if (clickObj instanceof clickItemEvent) {
                        int what=((clickItemEvent) clickObj).clickTag;
                        mOnListPopupItemClickListener.onItemClick(what);
                    }
                }
            }
        });

    }
    //=============================================================super methods
    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public Animator initShowAnimator() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(mAnimaView, "rotationX", 90f, 0f).setDuration(400),
                ObjectAnimator.ofFloat(mAnimaView, "translationY", 250f, 0f).setDuration(400),
                ObjectAnimator.ofFloat(mAnimaView, "alpha", 0f, 1f).setDuration(400 * 3 / 2));
        return set;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.popup_list);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_anima);
    }

    @Override
    public Animator initExitAnimator() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(mAnimaView, "rotationX", 0f, 90f).setDuration(400),
                ObjectAnimator.ofFloat(mAnimaView, "translationY", 0f, 250f).setDuration(400),
                ObjectAnimator.ofFloat(mAnimaView, "alpha",1f, 0f).setDuration(400 * 3 / 2));
        return set;
    }

    //=============================================================interface

    public OnListPopupItemClickListener getOnListPopupItemClickListener() {
        return mOnListPopupItemClickListener;
    }

    public void setOnListPopupItemClickListener(OnListPopupItemClickListener onListPopupItemClickListener) {
        mOnListPopupItemClickListener = onListPopupItemClickListener;
    }

    public interface OnListPopupItemClickListener{
        void onItemClick(int what);
    }
}
