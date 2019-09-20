package razerdp.demo.widget.decoration;

/**
 * Created by 大灯泡 on 2019/8/1
 * <p>
 * Description：均分Gridlayoutmanager
 */
public class SpaceOption extends GridItemDecoration.DecorationDrawOption {
    private Builder builder;

    SpaceOption(Builder builder) {
        this.builder = builder;
    }

    @Override
    public int leftDividerSize(int itemPosition) {
        if (isHeader(itemPosition)) return 0;
        int realSize = itemPosition - builder.headerCount;
        int column = realSize % getSpanCount();
        if (!builder.horizontalEdge) {
            return column * builder.size / getSpanCount();
        } else {
            return isFirstColumn(realSize) ? builder.size : (getSpanCount() - column) * builder.size / getSpanCount();
        }
    }

    @Override
    public int topDividerSize(int itemPosition) {
        if (isHeader(itemPosition)) return 0;
        int realSize = itemPosition - builder.headerCount;
        if (!builder.verticalEdge) {
            return isFirstRow(realSize) ? 0 : builder.size >> 1;
        } else {
            return isFirstRow(realSize) ? builder.size : builder.size >> 1;
        }
    }

    @Override
    public int rightDividerSize(int itemPosition) {
        if (isHeader(itemPosition)) return 0;
        int realSize = itemPosition - builder.headerCount;
        int column = realSize % getSpanCount();
        if (!builder.horizontalEdge) {
            return (getSpanCount() - column) * builder.size / getSpanCount();
        } else {
            return isLastColumn(realSize) ? builder.size : (column + 1) * builder.size / getSpanCount();
        }
    }

    @Override
    public int bottomDividerSize(int itemPosition) {
        if (isHeader(itemPosition)) return 0;
        int realSize = itemPosition - builder.headerCount;
        if (!builder.verticalEdge) {
            return isLastRow(realSize) ? 0 : builder.size >> 1;
        } else {
            return isLastRow(realSize) ? builder.size : builder.size >> 1;
        }
    }

    private boolean isHeader(int itemPosition) {
        return builder.headerCount > 0 && itemPosition < builder.headerCount;
    }


    public static class Builder {
        private int size;
        private int headerCount;
        private boolean verticalEdge = true;
        private boolean horizontalEdge = true;

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder headerCount(int headerCount) {
            this.headerCount = headerCount;
            return this;
        }

        public Builder verticalEdge(boolean verticalEdge) {
            this.verticalEdge = verticalEdge;
            return this;
        }

        public Builder horizontalEdge(boolean horizontalEdge) {
            this.horizontalEdge = horizontalEdge;
            return this;
        }

        public SpaceOption build() {
            return new SpaceOption(this);
        }
    }
}
