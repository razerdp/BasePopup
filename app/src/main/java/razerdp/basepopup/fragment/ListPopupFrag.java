package razerdp.basepopup.fragment;

import android.view.View;
import android.widget.Button;
import razerdp.basepopup.R;
import razerdp.basepopup.base.BasePopupWindow;
import razerdp.basepopup.popup.ListPopup;
import razerdp.basepopup.utils.ToastUtils;

/**
 * Created by 大灯泡 on 2016/1/20.
 * 带有Listview的Popup
 */
public class ListPopupFrag extends SimpleBaseFrag {
    public static final int TAG_CREATE=0x01;
    public static final int TAG_DELETE=0x02;
    public static final int TAG_MODIFY=0x03;
    private ListPopup mListPopup;
    @Override
    public void bindEvent() {
        ListPopup.Builder builder=new ListPopup.Builder(mContext);
        builder.addItem(TAG_CREATE,"Create");
        builder.addItem(TAG_MODIFY,"Modify");
        builder.addItem(TAG_CREATE,"Create");
        builder.addItem(TAG_DELETE,"Delete");
        builder.addItem(TAG_MODIFY,"Modify");
        builder.addItem(TAG_DELETE,"Delete");
        mListPopup=builder.build();

        mListPopup.setOnListPopupItemClickListener(new ListPopup.OnListPopupItemClickListener() {
            @Override
            public void onItemClick(int what) {
                switch (what){
                    case TAG_CREATE:
                        ToastUtils.ToastMessage(mContext,"click create");
                        break;
                    case TAG_DELETE:
                        ToastUtils.ToastMessage(mContext,"click delete");
                        break;
                    case TAG_MODIFY:
                        ToastUtils.ToastMessage(mContext,"click modify");
                        break;
                    default:
                        break;
                }
            }
        });

    }

    @Override
    public BasePopupWindow getPopup() {
        return mListPopup;
    }

    @Override
    public Button getButton() {
        return (Button) mFragment.findViewById(R.id.popup_show);
    }

    @Override
    public View getFragment() {
        return mInflater.inflate(R.layout.frag_list_popup, container, false);
    }
}
