package razerdp.demo.ui.friendcircle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ComponentActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.R;
import razerdp.demo.base.baseadapter.BaseSimpleRecyclerViewHolder;
import razerdp.demo.base.imageloader.ImageLoaderManager;
import razerdp.demo.model.friendcircle.FriendCircleInfo;
import razerdp.demo.popup.PopupFriendCircle;
import razerdp.demo.ui.photobrowser.PhotoBrowserProcessor;
import razerdp.demo.utils.ActivityUtil;
import razerdp.demo.utils.ButterKnifeUtil;
import razerdp.demo.utils.ToolUtil;
import razerdp.demo.utils.UIHelper;
import razerdp.demo.widget.DPImageView;
import razerdp.demo.widget.recyclerviewbox.RecyclerViewBox;
import razerdp.demo.widget.recyclerviewbox.layoutmanager.NineGridLayoutManager;

/**
 * Created by 大灯泡 on 2019/9/24
 * <p>
 * Description：
 */
public class FriendCircleViewHolder extends BaseSimpleRecyclerViewHolder<FriendCircleInfo> {
    @BindView(R.id.iv_avatar)
    DPImageView ivAvatar;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.rv_box)
    RecyclerViewBox rvBox;
    @BindView(R.id.iv_comment)
    ImageView ivComment;

    NineGridLayoutManager manager;
    Adapter mAdapter;

    PopupFriendCircle popupFriendCircle;


    public FriendCircleViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnifeUtil.bind(this, itemView);
        manager = new NineGridLayoutManager(UIHelper.dip2px(8));
        rvBox.setLayoutManager(manager);
    }

    @Override
    public int inflateLayoutResourceId() {
        return R.layout.item_friend_circle;
    }

    @Override
    public void onBindData(FriendCircleInfo data, int position) {
        ImageLoaderManager.INSTANCE.loadImage(ivAvatar, data.avatar);
        tvContent.setText(data.content);
        tvNick.setText(data.name);

        if (mAdapter == null) {
            mAdapter = new Adapter(data.pics);
            rvBox.setAdapter(mAdapter);
        } else {
            if (mAdapter.data == null || ToolUtil.isEmpty(data.pics)) {
                mAdapter.data = data.pics;
            } else {
                mAdapter.data.clear();
                mAdapter.data.addAll(data.pics);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.iv_comment)
    void showPopup(View v) {
        if (popupFriendCircle == null) {
            popupFriendCircle = new PopupFriendCircle(getContext());
            popupFriendCircle.setPopupGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }
        popupFriendCircle.setInfo(getData());
        popupFriendCircle.linkTo(v);
        popupFriendCircle.showPopupWindow(v);
    }

    static class Adapter extends RecyclerViewBox.Adapter implements View.OnClickListener {
        List<String> data;
        private PhotoBrowserProcessor mPhotoBrowserProcessor;
        private RecyclerViewBox mRecyclerViewBox;

        public Adapter(List<String> datas) {
            this.data = datas;
        }

        @Override
        public int getItemCount() {
            return ToolUtil.isEmpty(data) ? 0 : data.size();
        }

        @Override
        public void onAttachedToRecyclerViewBox(@NonNull RecyclerViewBox recyclerViewBox) {
            super.onAttachedToRecyclerViewBox(recyclerViewBox);
            this.mRecyclerViewBox = recyclerViewBox;
        }

        @Override
        public RecyclerViewBox.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
            return getItemCount() == 1 ?
                    new SingleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ninegrid_image_single_view, parent, false))
                    :
                    new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ninegrid_image_view, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerViewBox.ViewHolder viewHolder, int position) {
            ImageView iv = null;
            if (viewHolder instanceof SingleViewHolder) {
                iv = ((SingleViewHolder) viewHolder).iv;
            } else if (viewHolder instanceof ViewHolder) {
                iv = ((ViewHolder) viewHolder).iv;
            }

            if (iv == null) return;
            iv.setOnClickListener(this);
            iv.setTag(R.id.friend_circle_imageview_tag, position);
            ImageLoaderManager
                    .INSTANCE
                    .option()
                    .setError(R.drawable.ic_error_gray)
                    .loadImage(iv, data.get(position));
        }

        @Override
        public void onClick(View v) {
            if (v instanceof ImageView) {
                if (mPhotoBrowserProcessor == null) {
                    mPhotoBrowserProcessor = PhotoBrowserProcessor.with(data)
                            .setExitViewProvider((from, exitPosition) -> {
                                RecyclerViewBox.ViewHolder holder = mRecyclerViewBox.findViewHolderForPosition(exitPosition);
                                ImageView iv = null;
                                if (holder instanceof SingleViewHolder) {
                                    iv = ((SingleViewHolder) holder).iv;
                                } else if (holder instanceof ViewHolder) {
                                    iv = ((ViewHolder) holder).iv;
                                }
                                return iv;
                            });
                }
                int pos = ToolUtil.cast(v.getTag(R.id.friend_circle_imageview_tag), Integer.class, 0);
                mPhotoBrowserProcessor
                        .setPhotos(data)
                        .fromView((ImageView) v)
                        .setStartPosition(pos)
                        .start(ToolUtil.cast(ActivityUtil.getActivity(v.getContext()), ComponentActivity.class));
            }
        }

        class SingleViewHolder extends RecyclerViewBox.ViewHolder {
            ImageView iv;

            SingleViewHolder(View rootView) {
                super(rootView);
                iv = findViewById(R.id.iv_img);
            }
        }

        class ViewHolder extends RecyclerViewBox.ViewHolder {
            ImageView iv;

            ViewHolder(View rootView) {
                super(rootView);
                iv = findViewById(R.id.iv_img);
            }
        }
    }

}
