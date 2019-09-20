package razerdp.demo.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.widget.TextView;

import razerdp.basepopup.R;


/**
 * Created by 大灯泡 on 2019/4/16
 * <p>
 * Description：selector 工具类
 */
public class SelectorUtil {
    private Config mConfig;


    //因为懒。。所以直接枚举了
    public enum Shape {
        RECTANGLE,
        OVAL
    }

    public enum Type {
        STROKE,
        Fill
    }


    public static Config create() {
        return new Config();
    }


    public static class Config {
        private Shape mShape = Shape.RECTANGLE;
        private Type mType = Type.Fill;
        private int strokeWidth = 1;
        private float dashWidth = 0;
        private float dashGap = 0;

        private int strokeColor;
        private int backgroundColor;
        private int pressedColor;
        private int selectedColor;
        private int disableColor;

        private int textColor;
        private int pressedTextColor;
        private int selectedTextColor;
        private int disableTextColor;

        private float cornerRadius;
        private float topLeftRadius;
        private float topRightRadius;
        private float bottomLeftRadius;
        private float bottomRightRadius;


        private static final int TYPE_NORMAL = 0x0001;
        private static final int TYPE_PRESSED = 0x0001 << 1;
        private static final int TYPE_SELECTED = 0x0001 << 2;
        private static final int TYPE_DISABLE = 0x0001 << 3;


        public Config getDefault() {
            return shape(Shape.RECTANGLE)
                    .type(Type.Fill)
                    .backgroundColor(Color.TRANSPARENT)
                    .pressedColor(UIHelper.getColor(R.color.press_color))
                    .selectedColor(UIHelper.getColor(R.color.press_color))
                    .disableColor(UIHelper.getColor(R.color.disable))
                    .textColor(UIHelper.getColor(R.color.text_black1))
                    .pressedTextColor(UIHelper.getColor(R.color.text_black2))
                    .selectedTextColor(UIHelper.getColor(R.color.text_black1))
                    .disableTextColor(UIHelper.getColor(R.color.white));
        }

        public Config shape(Shape mShape) {
            this.mShape = mShape;
            return this;
        }

        public Config type(Type mType) {
            this.mType = mType;
            return this;
        }

        public Config strokeWidth(int strokeWidth) {
            this.strokeWidth = strokeWidth;
            return this;
        }

        public Config dashWidth(float dashWidth) {
            this.dashWidth = dashWidth;
            return this;
        }

        public Config dashGap(float dashGap) {
            this.dashGap = dashGap;
            return this;
        }

        public Config strokeColor(int strokeColor) {
            this.strokeColor = strokeColor;
            return this;
        }

        public Config backgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Config pressedColor(int pressedColor) {
            this.pressedColor = pressedColor;
            return this;
        }

        public Config selectedColor(int selectedColor) {
            this.selectedColor = selectedColor;
            return this;
        }

        public Config disableColor(int disableColor) {
            this.disableColor = disableColor;
            return this;
        }

        public Config textColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Config pressedTextColor(int pressedTextColor) {
            this.pressedTextColor = pressedTextColor;
            return this;
        }

        public Config selectedTextColor(int selectedTextColor) {
            this.selectedTextColor = selectedTextColor;
            return this;
        }

        public Config disableTextColor(int disableTextColor) {
            this.disableTextColor = disableTextColor;
            return this;
        }

        public Config cornerRadius(float cornerRadius) {
            this.cornerRadius = cornerRadius;
            return this;
        }

        public Config topLeftRadius(float topLeftRadius) {
            this.topLeftRadius = topLeftRadius;
            return this;
        }

        public Config topRightRadius(float topRightRadius) {
            this.topRightRadius = topRightRadius;
            return this;
        }

        public Config bottomLeftRadius(float bottomLeftRadius) {
            this.bottomLeftRadius = bottomLeftRadius;
            return this;
        }

        public Config bottomRightRadius(float bottomRightRadius) {
            this.bottomRightRadius = bottomRightRadius;
            return this;
        }


        public void into(View v) {
            if (v == null) return;
            StateListDrawable drawable;
            if (v.getBackground() instanceof StateListDrawable) {
                drawable = (StateListDrawable) v.getBackground();
            } else {
                drawable = new StateListDrawable();
            }
            drawable.addState(new int[]{android.R.attr.state_pressed}, createDrawable(TYPE_PRESSED));
            drawable.addState(new int[]{android.R.attr.state_selected}, createDrawable(TYPE_SELECTED));
            drawable.addState(new int[]{-android.R.attr.state_enabled}, createDrawable(TYPE_DISABLE));
            drawable.addState(new int[]{}, createDrawable(TYPE_NORMAL));

            ViewUtil.setBackground(v, drawable);
            if (v instanceof TextView) {
                int[][] textColorState = new int[4][];
                textColorState[0] = new int[]{android.R.attr.state_pressed};
                textColorState[1] = new int[]{android.R.attr.state_selected};
                textColorState[2] = new int[]{-android.R.attr.state_enabled};
                textColorState[3] = new int[]{};

                int[] textColors = {this.pressedTextColor == 0 ? ColorUtil.brightnessColor(this.textColor, ColorUtil.DEFAULT_BRIGHTNESS) : this.pressedTextColor,
                        this.selectedTextColor == 0 ? ColorUtil.brightnessColor(this.textColor, ColorUtil.DEFAULT_BRIGHTNESS) : this.selectedTextColor,
                        disableTextColor,
                        textColor};
                ((TextView) v).setTextColor(new ColorStateList(textColorState, textColors));
            }
        }

        private GradientDrawable createDrawable(int type) {
            GradientDrawable result = new GradientDrawable();
            int color = 0;
            switch (type) {
                case TYPE_NORMAL:
                    color = this.backgroundColor;
                    break;
                case TYPE_PRESSED:
                    color = this.pressedColor == 0 ? ColorUtil.brightnessColor(this.backgroundColor, ColorUtil.DEFAULT_BRIGHTNESS) : this.pressedColor;
                    break;
                case TYPE_SELECTED:
                    color = this.selectedColor == 0 ? ColorUtil.brightnessColor(this.backgroundColor, ColorUtil.DEFAULT_BRIGHTNESS) : this.selectedColor;
                    break;
                case TYPE_DISABLE:
                    color = this.disableColor;
                    break;
            }

            switch (mShape) {
                case OVAL:
                    result.setShape(GradientDrawable.OVAL);
                    break;
                case RECTANGLE:
                    result.setShape(GradientDrawable.RECTANGLE);
                    if (cornerRadius != 0) {
                        result.setCornerRadius(cornerRadius);
                    } else if (topLeftRadius > 0 || topRightRadius > 0 || bottomLeftRadius > 0 || bottomRightRadius > 0) {
                        result.setCornerRadii(new float[]{topLeftRadius,
                                topLeftRadius,
                                topRightRadius,
                                topRightRadius,
                                bottomRightRadius,
                                bottomRightRadius,
                                bottomLeftRadius,
                                bottomLeftRadius
                        });
                    }
                    break;
            }
            switch (mType) {
                case STROKE:
                    result.setColor(Color.TRANSPARENT);
                    result.setStroke(strokeWidth, this.strokeColor == 0 ? color : this.strokeColor, dashWidth, dashGap);
                    break;
                case Fill:
                    result.setColor(color);
                    break;
            }
            return result;
        }


    }

}
