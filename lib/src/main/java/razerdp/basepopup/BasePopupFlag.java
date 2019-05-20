package razerdp.basepopup;

/**
 * Created by 大灯泡 on 2019/5/8
 * <p>
 * Description：
 */
public interface BasePopupFlag {

    /**
     * 2.2.0开始，定义flag~因为太多boolean了，不好管理
     * <p>
     * | -------- | -------- | -------- | -------- |
     *                                           --- EVENT_SHIFT
     *                                           1     OUT_SIDE_DISMISS
     *                                          1      OUT_SIDE_TOUCHABLE
     *                                         1       BACKPRESS_ENABLE
     *                                        ------ DISPLAY_SHIFT
     *                                        1        FULL_SCREEN
     *                                       1         ALIGN_BACKGROUND
     *                                      1          CLIP_CHILDREN
     *                                     1           CLIP_TO_SCREEN
     *                                    ---------- CONTROL_SHIFT
     *                                    1            FADE_ENABLE
     *                                   1             AUTO_LOCATED
     *                                  1              AS_DROP_DOWN
     *                                 1               AUTO_INPUT_METHOD
     *                                1                ALIGN_BACKGROUND
     *                               1                 hold
     *                              1                  hold
     *                             1                   hold
     *                            ------------------ QUICK_POPUP_CONFIG
     *                            1                    BLUR_BACKGROUND
     *
     *          ------------------------------------ INNER_USAGE_SHIFT
     *         1                                       CUSTOM_WIDTH
     *        1                                        CUSTOM_HEIGHT
     */

    //事件控制 3 bit
    int EVENT_SHIFT = 0;
    int OUT_SIDE_DISMISS = 0x1 << EVENT_SHIFT;//001，点击外部消失
    int OUT_SIDE_TOUCHABLE = 0x2 << EVENT_SHIFT;//010，外部可以响应事件
    int BACKPRESS_ENABLE = 0x4 << EVENT_SHIFT;//100，backpress消失

    //显示控制 3 bit
    int DISPLAY_SHIFT = 3;
    int FULL_SCREEN = 0x1 << DISPLAY_SHIFT;//001，全屏（允许覆盖状态栏）
    int CLIP_CHILDREN = 0x2 << DISPLAY_SHIFT;//010，裁剪子控件
    int CLIP_TO_SCREEN = 0x4 << DISPLAY_SHIFT;//100，限制显示在屏幕内

    //popup控制 6 bit
    int CONTROL_SHIFT = 6;
    int FADE_ENABLE = 0X1 << CONTROL_SHIFT;// 0000 0001，淡入淡出
    int AUTO_LOCATED = 0x2 << CONTROL_SHIFT;//0000 0010，自动定位
    int AS_DROP_DOWN = 0x4 << CONTROL_SHIFT;//0000 0100，关联Anchor
    int AUTO_INPUT_METHOD = 0x8 << CONTROL_SHIFT;//0000 1000，自动弹出输入法
    int ALIGN_BACKGROUND = 0x10 << CONTROL_SHIFT;//0001 0000，对齐蒙层
    int KEEP_SIZE = 0x20 << CONTROL_SHIFT;//保持高度，不自动测量

    //quick popup config
    int QUICK_POPUP_CONFIG_SHIFT = 11;
    int BLUR_BACKGROUND = 0x1 << QUICK_POPUP_CONFIG_SHIFT;//blur background

    //内部使用 高8位
    int INNER_USAGE_SHIFT = 24;
    int CUSTOM_WIDTH = 0x1 << INNER_USAGE_SHIFT;
    int CUSTOM_HEIGHT = 0x2 << INNER_USAGE_SHIFT;


    int IDLE = OUT_SIDE_DISMISS | BACKPRESS_ENABLE | FULL_SCREEN | CLIP_CHILDREN | CLIP_TO_SCREEN | FADE_ENABLE;

}
