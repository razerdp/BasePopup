package razerdp.demo.popup;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.utils.DimensUtils;

/**
 * Created by 大灯泡 on 2016/12/6.
 * <p>
 * 从顶部下滑的Poup
 */

public class SlideFromTopPopup extends BasePopupWindow {

    private List<String> testList;

    public SlideFromTopPopup(Activity context) {
        super(context);
        setBackPressEnable(false);
        setDismissWhenTouchOuside(true);
        testList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            testList.add("position - " + i);
        }
        ListView listView = (ListView) findViewById(R.id.popup_list);
        listView.setAdapter(new InnerPopupAdapter(context, testList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), testList.get(position), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected Animation initShowAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, -DimensUtils.dipToPx(getContext(), 350f), 0);
        translateAnimation.setDuration(450);
        translateAnimation.setInterpolator(new OvershootInterpolator(1));
        return translateAnimation;
    }

    @Override
    protected Animation initExitAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, 0, -DimensUtils.dipToPx(getContext(), 350f));
        translateAnimation.setDuration(450);
        translateAnimation.setInterpolator(new OvershootInterpolator(-4));
        return translateAnimation;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.popup_select_from_top);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_anima);
    }

    //=============================================================adapter
    private static class InnerPopupAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;
        private List<String> mItemList;

        public InnerPopupAdapter(Context context, @NonNull List<String> itemList) {
            mContext = context;
            mItemList = itemList;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mItemList.size();
        }

        @Override
        public String getItem(int position) {
            return mItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            InnerPopupAdapter.ViewHolder vh = null;
            if (convertView == null) {
                vh = new InnerPopupAdapter.ViewHolder();
                convertView = mInflater.inflate(R.layout.item_popup_list, parent, false);
                vh.mTextView = (TextView) convertView.findViewById(R.id.item_tx);
                convertView.setTag(vh);
            } else {
                vh = (InnerPopupAdapter.ViewHolder) convertView.getTag();
            }
            vh.mTextView.setText(getItem(position));
            return convertView;
        }

        class ViewHolder {
            public TextView mTextView;
        }
    }
}
