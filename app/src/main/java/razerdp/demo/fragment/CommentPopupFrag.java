package razerdp.demo.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.base.baseadapter.BaseRecyclerViewAdapter;
import razerdp.demo.base.baseadapter.BaseRecyclerViewHolder;
import razerdp.demo.popup.AutoLocatedPopup;
import razerdp.demo.popup.CommentPopup;
import razerdp.demo.utils.ToastUtils;

/**
 * Created by 大灯泡 on 2016/1/16.
 */
public class CommentPopupFrag extends SimpleBaseFrag {
    private RecyclerView mRecyclerView;

    @Override
    public void bindEvent() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add("test" + String.valueOf(i));
        }
        mRecyclerView.setAdapter(new InnerAdapter(getContext(), data));
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
        return mInflater.inflate(R.layout.frag_comment_popup, container, false);
    }

    class InnerAdapter extends BaseRecyclerViewAdapter<String> {
        private CommentPopup mCommentPopup;

        private AutoLocatedPopup mAutoLocatedPopup;

        public InnerAdapter(@NonNull Context context, @NonNull List<String> datas) {
            super(context, datas);
            mCommentPopup = new CommentPopup(mContext);
            mCommentPopup.setOnCommentPopupClickListener(new CommentPopup.OnCommentPopupClickListener() {
                @Override
                public void onLikeClick(View v, TextView likeText) {
                    if (v.getTag() == null) {
                        v.setTag(1);
                        likeText.setText("取消");
                    } else {
                        switch ((int) v.getTag()) {
                            case 0:
                                v.setTag(1);
                                likeText.setText("取消");
                                break;
                            case 1:
                                v.setTag(0);
                                likeText.setText("赞  ");
                                break;
                        }
                    }
                }

                @Override
                public void onCommentClick(View v) {
                    ToastUtils.ToastMessage(mContext, "评论");
                }
            });
        }

        @Override
        protected int getViewType(int position, @NonNull String data) {
            return 0;
        }

        @Override
        protected int getLayoutResId(int viewType) {
            return R.layout.item_comment_popup;
        }

        @Override
        protected BaseRecyclerViewHolder getViewHolder(ViewGroup parent, View rootView, int viewType) {
            return new InnerViewHolder(rootView, viewType);
        }

        class InnerViewHolder extends BaseRecyclerViewHolder<String> {
            TextView content;
            ImageView show;

            public InnerViewHolder(View itemView, int viewType) {
                super(itemView, viewType);
                content = findViewById(R.id.tv_content);
                show = findViewById(R.id.iv_show);
                show.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCommentPopup.showPopupWindow(v);
                    }
                });
            }

            @Override
            public void onBindData(String data, int position) {
                content.setText(data);
            }
        }
    }

}
