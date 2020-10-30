package razerdp.basepopup;

/**
 * Created by 大灯泡 on 2019/5/8
 * <p>
 * Description：
 */
public interface BasePopupFlag {

    int MODE_ADD = -1;
    int MODE_REMOVE = -2;

    //事件控制 3 bit
    int EVENT_SHIFT = 0;
    int OUT_SIDE_DISMISS = 0x1 << EVENT_SHIFT;//点击外部消失
    int OUT_SIDE_TOUCHABLE = 0x2 << EVENT_SHIFT;//外部可以响应事件
    int BACKPRESS_ENABLE = 0x4 << EVENT_SHIFT;//backpress消失

    //显示控制 3 bit
    int DISPLAY_SHIFT = 3;
    int OVERLAY_STATUS_BAR = 0x1 << DISPLAY_SHIFT;//允许覆盖状态栏
    int CLIP_CHILDREN = 0x2 << DISPLAY_SHIFT;//裁剪子控件
    int OVERLAY_NAVIGATION_BAR = 0x4 << DISPLAY_SHIFT;//允许覆盖导航栏

    //popup控制 6 bit
    int CONTROL_SHIFT = 7;
    int FADE_ENABLE = 0X1 << CONTROL_SHIFT;// 淡入淡出
    int AUTO_LOCATED = 0x2 << CONTROL_SHIFT;//自动定位
    int WITH_ANCHOR = 0x4 << CONTROL_SHIFT;//关联Anchor
    int AUTO_INPUT_METHOD = 0x8 << CONTROL_SHIFT;//自动弹出输入法
    int ALIGN_BACKGROUND = 0x10 << CONTROL_SHIFT;//对齐蒙层
    int FITSIZE = 0x20 << CONTROL_SHIFT;//允许popup重设大小

    //quick popup config
    int QUICK_POPUP_CONFIG_SHIFT = 14;
    int BLUR_BACKGROUND = 0x1 << QUICK_POPUP_CONFIG_SHIFT;//blur background

    //键盘
    int KEYBOARD_CONTROL_SHIFT = 16;
    int KEYBOARD_ALIGN_TO_VIEW = 0x1 << KEYBOARD_CONTROL_SHIFT;
    int KEYBOARD_ALIGN_TO_ROOT = 0x2 << KEYBOARD_CONTROL_SHIFT;
    int KEYBOARD_IGNORE_OVER_KEYBOARD = 0x4 << KEYBOARD_CONTROL_SHIFT;
    int KEYBOARD_ANIMATE_ALIGN = 0x8 << KEYBOARD_CONTROL_SHIFT;
    int KEYBOARD_FORCE_ADJUST = 0x10 << KEYBOARD_CONTROL_SHIFT;


    //其他用
    int OTHER_SHIFT = 22;
    int CUSTOM_ON_UPDATE = 0x1 << OTHER_SHIFT;
    int CUSTOM_ON_ANIMATE_DISMISS = 0x2 << OTHER_SHIFT;
    int SYNC_MASK_ANIMATION_DURATION = 0x4 << OTHER_SHIFT;//同步蒙层和用户动画的时间
    int AS_WIDTH_AS_ANCHOR = 0x8 << OTHER_SHIFT;//宽度与anchor一致
    int AS_HEIGHT_AS_ANCHOR = 0x10 << OTHER_SHIFT;//高度与anchor一致
    int TOUCHABLE = 0x20 << OTHER_SHIFT;
    int OVERLAY_MASK = 0x40 << OTHER_SHIFT; //用于overlay status/navigation 覆盖mask层
    int OVERLAY_CONTENT = 0x80 << OTHER_SHIFT;//用于overlay status/navigation覆盖content层


    int IDLE = OUT_SIDE_DISMISS
            | BACKPRESS_ENABLE
            | OVERLAY_STATUS_BAR
            | OVERLAY_NAVIGATION_BAR
            | CLIP_CHILDREN
            | FADE_ENABLE
            | KEYBOARD_ALIGN_TO_ROOT
            | KEYBOARD_IGNORE_OVER_KEYBOARD
            | KEYBOARD_ANIMATE_ALIGN
            | SYNC_MASK_ANIMATION_DURATION
            | TOUCHABLE;

}
