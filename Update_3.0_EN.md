[**Chinese**](./Update_3.0.md) | **English**

---

## Notes on the disruptive update to BasePopup 3.0

### Description of alterations

   * Delete **BaseLazyPopupWindow**: there is no need to distinguish between lazy loading and normal BasePopupWindow in the future, it inherits BasePopupWindow uniformly
   * Remove the method `onCreateConstructor`: this method is actually for the BaseLazyPopupWindow and is not needed now that there is no more BaseLazyPopupWindow
   * [Important] Delete method `onCreateContentView`: The deletion of this method will affect all BasePopupWindow subclasses and you will need to change it manually.
      * This method will be replaced by `setContentView(@LayoutRes int layoutResID)` or `setContentView(final View view)` and you will need to modify its use.
      * Of course, if you use `setContentView(final View view)`, we still recommend that you use setContentView(createPopupById(layoutResID)) so that we parse to the correct xml configuration.

### Why

I've actually wanted to change this for a long time, and I've had more than one developer mention it to me, e.g. [#368](https://github.com/razerdp/BasePopup/issues/368).

Initialising a ContentView should be like a Dialog or Activity, with permissions given to the user rather than forced by the framework.

The reason for the `onCreateContentView` forced method is that this is how the first version of BasePopup was designed, and the first version was designed back in '16, when I was a senior in college and designed the framework for job hunting purposes.

At that time I didn't expect that BasePopup would be maintained and iterated upon, nor did I expect that it would be used by so many developers.

But because so many people were using it, I didn't dare to touch it, because it was a disruptive update and there was a lot to change (although the changes were small, there were probably a lot of classes that depended on BasePopup).

However, with more and more people asking this question, I think this change is inevitable.

So for this change I will release version 3.0 straight away, to distinguish it from version 2.x.

Translated with www.DeepL.com/Translator (free version)


### Benefits

There are many advantages, starting with the previous disadvantages.

#### BaseLazyPopupWindow

In previous versions, I provided a BaseLazyPopupWindow to solve the problem of constructors passing parameters that were not available in onViewCreated.

At first glance it seems to work, but in reality there are still drawbacks, most notably in two areas.
    1. The timing of the inflate is in `showPopupWindow()`, which can cause complex layouts to lag on the first show (reason for lag: inflate)
    2. Because of lazy loading, its loading time is at show time, so we can't findViewById in the constructor, and secondly for `new popup().setText().showPopupWindow()` which accesses the control directly in setText is not feasible, it just gets an NPE error (there is no inflate before show There is no inflate before the show, so the access control will be empty).

These two problems may not be encountered by many people, but when they are, they can be a huge pain in the ass to understand why the framework is designed the way it is.

#### Problems with constructor passing and onViewCreated taking parameters

In previous versions, onViewCreated was called in the constructor of the parent class, where none of the subclass parameters had been assigned for initialisation, so we could not get the parameters when onViewCreated was executed.

Even with the later provision of BaseLazyPopupWindow, there were several problems with this.


### How

The advantage is that the timing of the `setContentView` is entirely under your control and you don't have to worry about the problems described above.

e.g.

```java
public class DemoPopup extends BasePopupWindow {
    @BindView(R.id.tv_desc)
    public TextView mTvDesc;

    int a;

    public DemoPopup(Context context,int a) {
        super(context);
        // The value passed in by the assignment constructor at this point
        this.a = a;
        setContentView(R.layout.popup_demo);
    }


    @Override
    public void onViewCreated(View contentView) {
        ButterKnifeUtil.bind(this, contentView);
        // At this point it is possible to correctly access the a
        mTvDesc.setText(String.valueOf(a));
    }

    // If you use new DemoPopup().setText(), you don't need to worry about mTvDesc being empty
    public DemoPopup setText(CharSequence text) {
        mTvDesc.setText(text);
        return this;
    }
}
```
