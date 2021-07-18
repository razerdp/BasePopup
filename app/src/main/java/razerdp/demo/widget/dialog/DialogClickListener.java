package razerdp.demo.widget.dialog;

/**
 * Created by 大灯泡 on 2019/4/22.
 *
 * dialog两个button点击事件
 */
public interface DialogClickListener {

    /**
     * @return true for dismiss
     */
    boolean onPositiveClicked();

    /**
     * @return true for dismiss
     */
    boolean onNegativeClicked();
}
