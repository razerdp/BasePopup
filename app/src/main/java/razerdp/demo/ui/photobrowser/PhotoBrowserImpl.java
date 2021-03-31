package razerdp.demo.ui.photobrowser;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import razerdp.demo.utils.ToolUtil;

/**
 * Created by 大灯泡 on 2019/8/22
 * <p>
 * Description：
 */
public class PhotoBrowserImpl implements IPhotoBrowserProvider {
    private String url;
    private String thumb;

    public PhotoBrowserImpl(String url) {
        this(url, null);
    }

    public PhotoBrowserImpl(String url, String thumb) {
        this.url = url;
        this.thumb = thumb;
    }

    @Override
    public String getPhoto() {
        return url;
    }

    @Override
    public String getThumb() {
        return thumb;
    }


    public static List<IPhotoBrowserProvider> fromList(List<String> urls, @Nullable List<String> thumbs) {
        if (ToolUtil.isEmpty(urls)) {
            return Collections.emptyList();
        }
        List<IPhotoBrowserProvider> result = new ArrayList<>(urls.size());
        for (String url : urls) {
            result.add(new PhotoBrowserImpl(url));
        }
        if (thumbs != null && thumbs.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                if (ToolUtil.indexIn(thumbs, i)) {
                    PhotoBrowserImpl ret = (PhotoBrowserImpl) result.get(i);
                    ret.thumb = thumbs.get(i);
                }
            }
        }
        return result;
    }
}
