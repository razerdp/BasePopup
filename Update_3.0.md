 **中文** | [**English**](./Update_3.0_EN.md)

---

## 关于BasePopup 3.0的破坏性更新说明

### 改动说明

   * 删除**BaseLazyPopupWindow**：往后不需要区分懒加载和正常的BasePopupWindow，统一依赖BasePopupWindow
   * 删除方法`onCreateConstructor`：该方法实际上是给BaseLazyPopupWindow使用的，现在没有了BaseLazyPopupWindow，自然不需要该方法
   * 【重要】删除方法`onCreateContentView`：该方法的删除将会影响所有的BasePopupWindow子类，您需要手动去改动
      * 该方法将会被`setContentView(@LayoutRes int layoutResID)`或`setContentView(final View view)`所代替，您需要修改其使用。
      * 当然，如果使用`setContentView(final View view)`，我们依然建议您用setContentView(createPopupById(layoutResID))，以便我们解析到正确的xml配置。

### 为什么要这么改

事实上想改这个很久了，而且也不止一位开发者向我提过这个issue，如[#368](https://github.com/razerdp/BasePopup/issues/368)。

初始化ContentView其实就应该跟Dialog或者Activity一样，把权限交给使用者而非框架强行设定。

之所以会出现`onCreateContentView`这样的强制实现的方法，是因为第一版的BasePopup就是这么设计的，而第一版的设计时间是16年，那时候我大四，设计这个框架的目的就是为了求职。

那时候我也想不到BasePopup会被我一直维护且迭代更新，也没想到会有那么多开发者使用。

但也正因为是那么多人使用，因此我一直不太敢去动他，毕竟这是一个破坏性更新，要改的很多很多（虽然改动很小，但可能依赖BasePopup的类很多）。

但是，随着越来越多人的提出这个问题，我认为这种修改是躲不过的。

因此这次改动我将直接发布3.0版本，与2.x版本区分开来。


### 修改的好处

优点有很多，先来说说以前的缺点：

#### BaseLazyPopupWindow

在以前的版本中，我提供了一个BaseLazyPopupWindow来解决构造器传参，onViewCreated中无法获取的问题。

咋一眼似乎还行，实际上仍有弊端，最主要体现在两个：

    1. inflate的时机在showPopupWindow()，这会导致复杂的布局在第一次show的时候会卡顿（卡顿原因：inflate）
    2. 由于懒加载，其加载时机在show的时候，因此我们无法在构造器中findViewById，其次对于 new popup().setText().showPopupWindow()这样的在setText中直接访问控件的方法是不可行的，只会得到NPE报错（在show之前都没有inflate呢，访问控件只能是空的）。

这两个问题也许很多人没遇到过，但一旦遇到了就会觉得巨麻烦，框架为啥这么设计。

#### 构造器传参及onViewCreated取参的问题

在以前的版本中，onViewCreated是在父类的构造器中调用的，此时子类的参数都还没赋值进行初始化，因此执行onViewCreated时我们无法得到参数。

哪怕后来提供了BaseLazyPopupWindow，也会有上述几个问题。


### 修改后的使用

修改后的使用上跟以前其实没啥区别，优点是`setContentView`的时机完全由您控制，不必担心上面所说的问题。

e.g.

```java
public class DemoPopup extends BasePopupWindow {
    @BindView(R.id.tv_desc)
    public TextView mTvDesc;

    int a;

    public DemoPopup(Context context,int a) {
        super(context);
        // 此时赋值构造器传入的值
        this.a = a;
        setContentView(R.layout.popup_demo);
    }


    @Override
    public void onViewCreated(View contentView) {
        ButterKnifeUtil.bind(this, contentView);
        // 此时能正确访问到a
        mTvDesc.setText(String.valueOf(a));
    }

    // 如果使用 new DemoPopup().setText()，不需要担心mTvDesc为空
    public DemoPopup setText(CharSequence text) {
        mTvDesc.setText(text);
        return this;
    }
}
```
