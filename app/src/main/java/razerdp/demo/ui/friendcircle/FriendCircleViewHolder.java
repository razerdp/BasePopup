package razerdp.demo.ui.friendcircle;

import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ComponentActivity;
import razerdp.basepopup.R;
import razerdp.demo.base.baseadapter.BaseSimpleRecyclerViewHolder;
import razerdp.demo.base.imageloader.ImageLoaderManager;
import razerdp.demo.model.friendcircle.FriendCircleInfo;
import razerdp.demo.popup.PopupFriendCircle;
import razerdp.demo.ui.photobrowser.PhotoBrowserImpl;
import razerdp.demo.ui.photobrowser.PhotoBrowserProcessor;
import razerdp.demo.utils.ActivityUtil;
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
    DPImageView ivAvatar;
    TextView tvNick;
    TextView tvContent;
    RecyclerViewBox rvBox;
    ImageView ivComment;

    NineGridLayoutManager manager;
    Adapter mAdapter;

    PopupFriendCircle popupFriendCircle;


    public FriendCircleViewHolder(@NonNull View itemView) {
        super(itemView);
        ivAvatar =  findViewById(R.id.iv_avatar);
        tvNick =  findViewById(R.id.tv_nick);
        tvContent =  findViewById(R.id.tv_content);
        rvBox =  findViewById(R.id.rv_box);
        ivComment =  findViewById(R.id.iv_comment);
        manager = new NineGridLayoutManager(UIHelper.dip2px(8));
        rvBox.setLayoutManager(manager);
        ivComment.setOnClickListener(this::showPopup);
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
            mAdapter.updateData(data.pics);
            mAdapter.notifyDataSetChanged();
        }
    }

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
        List<String> thumb;
        List<String> hight;
        List<Pair<String, String>> data;
        private PhotoBrowserProcessor mPhotoBrowserProcessor;
        private RecyclerViewBox mRecyclerViewBox;

        public Adapter(List<Pair<String, String>> datas) {
            this.data = datas;
            thumb = new ArrayList<>();
            hight = new ArrayList<>();
            updateData(datas);
        }

        public void updateData(List<Pair<String, String>> datas) {
            this.data = datas;
            thumb.clear();
            hight.clear();
            for (Pair<String, String> data : datas) {
                hight.add(data.first);
                thumb.add(data.second);
            }
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
                    new SingleViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_ninegrid_image_single_view, parent, false))
                    :
                    new ViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_ninegrid_image_view, parent, false));
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
                    .loadImage(iv, thumb.get(position));
        }

        @Override
        public void onClick(View v) {
            if (v instanceof ImageView) {
                if (mPhotoBrowserProcessor == null) {
                    mPhotoBrowserProcessor = PhotoBrowserProcessor.with(PhotoBrowserImpl.fromList(hight,thumb))
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
                        .setPhotos(PhotoBrowserImpl.fromList(hight,thumb))
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
