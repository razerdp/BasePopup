package razerdp.util.animation;

import android.view.Gravity;

/**
 * 方向枚举
 * 讲道理，系统的gravity真的好用，不用我自己去慢慢定各种位，而且位运算十分爽啊
 * 要不是gravity不太符合【方向】这个描述，我还真的想直接用它
 */
public enum Direction {
    CURRENT(Gravity.NO_GRAVITY),
    LEFT(Gravity.LEFT),
    TOP(Gravity.TOP),
    RIGHT(Gravity.RIGHT),
    BOTTOM(Gravity.BOTTOM),
    CENTER(Gravity.CENTER),
    CENTER_HORIZONTAL(Gravity.CENTER_HORIZONTAL),
    CENTER_VERTICAL(Gravity.CENTER_VERTICAL);

    final int gravity;

    Direction(int gravity) {
        this.gravity = gravity;
    }
}
