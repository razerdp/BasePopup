package razerdp.demo.model.updatelog;

import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.R;
import razerdp.demo.utils.SpanUtil;
import razerdp.demo.utils.StringUtil;
import razerdp.demo.utils.ToolUtil;
import razerdp.demo.utils.UIHelper;
import razerdp.demo.widget.span.SpannableStringBuilderCompat;

/**
 * Created by 大灯泡 on 2019/9/24.
 */
public class UpdateLogInfo {

    static final String[] sCHILD_SEPS = {"▪", "•", "▫",};
    final String title;
    //二级
    List<ChildTextInfo> mChildTextInfos;

    private SpannableStringBuilderCompat mCache;

    public UpdateLogInfo(String title) {
        this.title = title;
        mChildTextInfos = new ArrayList<>();
    }

    public ChildTextInfo append(String content) {
        ChildTextInfo childTextInfo = new ChildTextInfo(this, content);
        mChildTextInfos.add(childTextInfo);
        return childTextInfo;
    }

    public SpannableStringBuilderCompat get() {
        if (mCache != null) return mCache;
        mCache = new SpannableStringBuilderCompat(SpanUtil.create(title).append(title).setTextStyle(Typeface.DEFAULT_BOLD).setTextSize(14).getSpannableStringBuilder());
        //添加一级
        apply(mCache, mChildTextInfos, 0);
        return mCache;
    }

    private void apply(SpannableStringBuilderCompat who, List<? extends ChildTextInfo> childTextInfos, int level) {
        for (ChildTextInfo childTextInfo : childTextInfos) {
            who.append('\n');
            for (int i = 0; i < level; i++) {
                who.append("\t\t");
            }
            String sep = level >= sCHILD_SEPS.length ? sCHILD_SEPS[sCHILD_SEPS.length - 1] : sCHILD_SEPS[level];

            who.append(sep)
                    .append("  ");
            if (!applySpan(who, childTextInfo)) {
                if (childTextInfo.bold) {
                    who.append(SpanUtil.create(childTextInfo.content).append(childTextInfo.content).setTextStyle(Typeface.DEFAULT_BOLD).getSpannableStringBuilder());
                } else {
                    who.append(childTextInfo.content);
                }
            }

            if (!ToolUtil.isEmpty(childTextInfo.mChildTextInfos)) {
                apply(who, childTextInfo.mChildTextInfos, level + 1);
            }
        }
    }

    private boolean applySpan(SpannableStringBuilderCompat who, ChildTextInfo childTextInfo) {
        if (!ToolUtil.isEmpty(childTextInfo.spans)) {
            SpanUtil.MultiSpanOption option = SpanUtil.create(childTextInfo.content);
            SpanUtil.ItemOption subOption = null;
            for (SpanTextInfo span : childTextInfo.spans) {
                subOption = subOption == null ? option.append(span.key) : subOption.append(span.key);
                if (StringUtil.noEmpty(span.url)) {
                    subOption.setUrl(true, UIHelper.getColor(R.color.color_link),
                            v -> ToolUtil.openInSystemBroswer(v.getContext(), span.url))
                            .setTextStyle(Typeface.DEFAULT_BOLD);
                }
                if (span.delete) {
                    subOption.setDeleteLine(true);
                }
                if (span.bold) {
                    subOption.setTextStyle(Typeface.DEFAULT_BOLD);
                }
                if (span.tag) {
                    subOption.setBackgroundResourceColor(R.color.color_F2F3F5);
                }
            }
            if (subOption != null) {
                who.append(subOption.getSpannableStringBuilder());
                return true;
            }
        }
        return false;
    }


    public class ChildTextInfo {

        final UpdateLogInfo root;
        final String content;
        List<SpanTextInfo> spans;
        //三级
        List<MultiChildTextInfo> mChildTextInfos;
        boolean bold;

        ChildTextInfo(UpdateLogInfo root, String content) {
            this.root = root;
            this.content = content;
        }

        public SpanTextInfo keySelf() {
            return keyAt(content);
        }

        public SpanTextInfo keyAt(String key) {
            if (spans == null) {
                spans = new ArrayList<>();
            }
            SpanTextInfo spanTextInfo = new SpanTextInfo(this, key);
            spans.add(spanTextInfo);
            return spanTextInfo;
        }

        public ChildTextInfo append(String content) {
            return root.append(content);
        }

        public MultiChildTextInfo child(String content) {
            if (mChildTextInfos == null) {
                mChildTextInfos = new ArrayList<>();
            }
            MultiChildTextInfo childTextInfo = new MultiChildTextInfo(root, this, content);
            mChildTextInfos.add(childTextInfo);
            return childTextInfo;
        }


        public ChildTextInfo end() {
            if (this instanceof MultiChildTextInfo) {
                return ((MultiChildTextInfo) this).parent;
            }
            return this;
        }

        public ChildTextInfo bold() {
            this.bold = true;
            return this;
        }

        public UpdateLogInfo root() {
            return root;
        }
    }

    public class MultiChildTextInfo extends ChildTextInfo {
        ChildTextInfo parent;

        MultiChildTextInfo(UpdateLogInfo root, ChildTextInfo parent, String content) {
            super(root, content);
            this.parent = parent;
        }

        public ChildTextInfo parent() {
            return parent;
        }
    }

    public class SpanTextInfo {
        final ChildTextInfo parent;
        final String key;
        String url;
        boolean delete;
        boolean bold;
        boolean tag;

        SpanTextInfo(ChildTextInfo parent, String key) {
            this.parent = parent;
            this.key = key;
            if (key.startsWith("#")) {
                this.url = "https://github.com/razerdp/BasePopup/issues/" + key.replace("#", "");
            }
        }

        public ChildTextInfo url(String url) {
            this.url = url;
            return parent;
        }

        public SpanTextInfo keyAt(String content) {
            return parent.keyAt(content);
        }

        public ChildTextInfo end() {
            if (parent instanceof MultiChildTextInfo) {
                return ((MultiChildTextInfo) parent).parent;
            }
            return parent;
        }

        public ChildTextInfo self() {
            return parent;
        }

        public UpdateLogInfo root() {
            return parent.root;
        }

        public SpanTextInfo delete() {
            this.delete = true;
            return this;
        }

        public SpanTextInfo bold() {
            this.bold = true;
            return this;
        }

        public SpanTextInfo tag() {
            this.tag = true;
            return this;
        }
    }
}
