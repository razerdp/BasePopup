package razerdp.demo.fragment;

import android.view.View;
import android.widget.Button;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.popup.ListPopup;
import razerdp.demo.utils.ToastUtils;

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
        builder.addItem(TAG_CREATE,"Create-01");
        builder.addItem(TAG_MODIFY,"Modify-01");
        builder.addItem(TAG_CREATE,"Create-02");
        builder.addItem(TAG_DELETE,"Delete-01");
        builder.addItem(TAG_MODIFY,"Modify-02");
        builder.addItem(TAG_CREATE,"Create-03");
        builder.addItem(TAG_DELETE,"Delete-02");
        builder.addItem(TAG_MODIFY,"Modify-03");
        builder.addItem(TAG_DELETE,"Delete-03");
        builder.addItem(TAG_MODIFY,"Modify-04");
        builder.addItem(TAG_DELETE,"Delete-04");
        builder.addItem(TAG_CREATE,"Create-04");
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
