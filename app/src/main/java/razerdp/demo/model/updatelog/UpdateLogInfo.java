package razerdp.demo.model.updatelog;

import android.graphics.Typeface;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.R;
import razerdp.demo.utils.SpanUtil;
import razerdp.demo.utils.StringUtil;
import razerdp.demo.utils.ToolUtil;
import razerdp.demo.widget.span.SpannableStringBuilderCompat;

/**
 * Created by 大灯泡 on 2019/9/24.
 */
public class UpdateLogInfo {
    final String title;
    //二级
    List<SubTextInfo> mSubTextInfos;

    private SpannableStringBuilderCompat mCache;

    public UpdateLogInfo(String title) {
        this.title = title;
        mSubTextInfos = new ArrayList<>();
    }

    public SubTextInfo append(String content) {
        SubTextInfo subTextInfo = new SubTextInfo(this, content);
        mSubTextInfos.add(subTextInfo);
        return subTextInfo;
    }

    public SpannableStringBuilderCompat get() {
        if (mCache != null) return mCache;
        mCache = new SpannableStringBuilderCompat(SpanUtil.create(title).append(title).setTextStyle(Typeface.DEFAULT_BOLD).setTextSize(14).getSpannableStringBuilder());
        //添加一级
        for (SubTextInfo subTextInfo : mSubTextInfos) {
            mCache.append('\n')
                    .append('\t')
                    .append('▪')
                    .append("  ");
            if (!ToolUtil.isEmpty(subTextInfo.links)) {
                SpanUtil.MultiSpanOption option = SpanUtil.create(subTextInfo.content);
                for (LinkTextInfo link : subTextInfo.links) {
                    SpanUtil.ItemOption subOption = option.append(link.key);
                    if (StringUtil.noEmpty(link.url)) {
                        subOption.setSpanClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToolUtil.openInSystemBroswer(v.getContext(), link.url);
                            }
                        }).setTextColorRes(R.color.color_link).setTextStyle(Typeface.DEFAULT_BOLD);
                    }
                    mCache.append(subOption.getSpannableStringBuilder());
                }
            } else {
                mCache.append(subTextInfo.content);
            }
        }
        return mCache;
    }


    public class SubTextInfo {

        final UpdateLogInfo parent;
        final String content;
        List<LinkTextInfo> links;
        //三级
        List<SubTextInfo> mSubTextInfos;

        SubTextInfo(UpdateLogInfo parent, String content) {
            this.parent = parent;
            this.content = content;
        }

        public LinkTextInfo keyAt(String content) {
            if (links == null) {
                links = new ArrayList<>();
            }
            LinkTextInfo linkTextInfo = new LinkTextInfo(this, content);
            links.add(linkTextInfo);
            return linkTextInfo;
        }

        public SubTextInfo append(String content) {
            return parent.append(content);
        }

        public UpdateLogInfo end() {
            return parent;
        }
    }


    public class LinkTextInfo {
        final SubTextInfo parent;
        final String key;
        String url;

        public LinkTextInfo(SubTextInfo parent, String key) {
            this.parent = parent;
            this.key = key;
        }

        public SubTextInfo url(String url) {
            this.url = url;
            return parent;
        }

        public LinkTextInfo keyAt(String content) {
            return parent.keyAt(content);
        }
    }
}
