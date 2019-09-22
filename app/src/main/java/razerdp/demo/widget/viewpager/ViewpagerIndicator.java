package razerdp.demo.widget.viewpager;

/**
 * Created by 大灯泡 on 2019/4/24
 * <p>
 * Description：Banner指示器
 */
public interface ViewpagerIndicator {

    void onDataSizeChange(int dataSize);

    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    void onPageSelected(int position);
}
