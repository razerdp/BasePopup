package razerdp.demo.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import razerdp.basepopup.R;


/**
 * Created by 大灯泡 on 2019/9/4
 * <p>
 * Description：
 */
public class LoadingDialog extends BaseDialog {
    private ImageView progress;
    private TextView desc;
    AnimationDrawable fireDrawable;
    private String descStr;
    private TextView actionText;
    private String actionStr;

    LoadingDialog(@NonNull Context context) {
        this(context, null);
    }

    LoadingDialog(@NonNull Context context, String desc) {
        super(context);
        this.descStr = desc;
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (fireDrawable != null) {
                    fireDrawable.selectDrawable(0);
                    fireDrawable.setVisible(false, true);
                    fireDrawable.stop();
                }
            }
        });
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (fireDrawable != null) {
                    fireDrawable.setVisible(true, true);
                    fireDrawable.start();
                }
            }
        });
    }

    public static LoadingDialog create(@NonNull Context context) {
        return create(context, null);
    }

    public static LoadingDialog create(@NonNull Context context, String desc) {
        return new LoadingDialog(context, desc);
    }

    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater) {
        return inflater.inflate(R.layout.dialog_loading, null);
    }

    @Override
    protected void onFindView(@NonNull View dialogView) {
        progress = dialogView.findViewById(R.id.view_progress);
        desc = dialogView.findViewById(R.id.tv_desc);
        actionText = dialogView.findViewById(R.id.tv_action);
        fireDrawable = (AnimationDrawable) getContext().getResources().getDrawable(R.drawable.drawable_loading);
        progress.setImageDrawable(fireDrawable);

        if (!TextUtils.isEmpty(descStr)) {
            desc.setText(descStr);
        }
    }

    public void setDesc(String desc) {
        this.descStr = desc;
        if (this.desc != null) {
            this.desc.setText(desc);
        }
    }

    public void setAction(String desc) {
        this.actionStr = desc;
        if (this.actionText != null) {
            this.actionText.setText(desc);
            if (TextUtils.isEmpty(desc)) {
                this.actionText.setVisibility(View.GONE);
            } else {
                this.actionText.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setAction(String desc, View.OnClickListener l) {
        this.actionStr = desc;
        if (this.actionText != null) {
            this.actionText.setText(desc);
            this.actionText.setOnClickListener(l);
            if (TextUtils.isEmpty(desc)) {
                this.actionText.setVisibility(View.GONE);
            } else {
                this.actionText.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    protected void onInitMode(int mode) {

    }
}
