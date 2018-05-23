package razerdp.demo.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.popup.DismissControlPopup;

/**
 * Created by 大灯泡 on 2017/12/26.
 * <p>
 * 专门用来怼dismiss的fragment
 */
public class DismissControlPopupFrag extends SimpleBaseFrag {
    private Button mButton;
    private DismissControlPopup mDismissControlPopup;

    private SwitchCompat switchDismissWhenTouchOutside;
    private SwitchCompat switchBackpressEnable;
    private SwitchCompat switchInterceptTouchEvent;
    private ListView mListView;

    @Override
    public void bindEvent() {
        mButton = (Button) mFragment.findViewById(R.id.popup_show);
        switchDismissWhenTouchOutside = (SwitchCompat) mFragment.findViewById(R.id.switch_dismiss_when_touch_outside);
        switchBackpressEnable = (SwitchCompat) mFragment.findViewById(R.id.switch_backpress_enable);
        switchInterceptTouchEvent = (SwitchCompat) mFragment.findViewById(R.id.switch_intercept_touch_event);
        mListView = (ListView) mFragment.findViewById(R.id.list_view);
        setAdapter(getActivity(), getData());
        mDismissControlPopup = new DismissControlPopup(mContext);
        mDismissControlPopup.setOnBeforeShowCallback(new BasePopupWindow.OnBeforeShowCallback() {
            @Override
            public boolean onBeforeShow(View popupRootView, View anchorView, boolean hasShowAnima) {
                mDismissControlPopup.setAllowDismissWhenTouchOutside(switchDismissWhenTouchOutside.isChecked());
                mDismissControlPopup.setBackPressEnable(switchBackpressEnable.isChecked());
                mDismissControlPopup.setAllowInterceptTouchEvent(switchInterceptTouchEvent.isChecked());
                return true;
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDismissControlPopup.showPopupWindow(v);
            }
        });
    }

    @Override
    public BasePopupWindow getPopup() {
        return null;
    }

    @Override
    public Button getButton() {
        return null;
    }

    @Override
    public View getFragment() {
        return mInflater.inflate(R.layout.frag_dismiss_control_popup, container, false);
    }

    public List<String> getData() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            result.add("this is item " + (i + 1));
        }
        return result;
    }

    //=============================================================adapter
    class ListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;
        private List<String> mItemList;

        public ListAdapter(Context context, @NonNull List<String> itemList) {
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
            ViewHolder vh = null;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_popup_list, parent, false);
                vh.mTextView = (TextView) convertView.findViewById(R.id.item_tx);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.mTextView.setText(getItem(position));
            return convertView;
        }

        class ViewHolder {
            public TextView mTextView;
        }
    }

    //=============================================================init
    private void setAdapter(Activity context, List<String> mDatas) {
        if (mDatas == null || mDatas.size() == 0) return;
        final ListAdapter adapter = new ListAdapter(context, mDatas);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(), adapter.getItem(position), Toast.LENGTH_SHORT).show();
                mDismissControlPopup.dismiss();
            }
        });

    }

}
