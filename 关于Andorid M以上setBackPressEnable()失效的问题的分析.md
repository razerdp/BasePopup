## 关于Andorid M（6.0）及以上setBackPressEnable()失效的问题的分析以及不完美解决方法

---

在文章开始之前，我们不妨看看在M之前，我们的popup库是怎么开关闭返回键的。。。

```java
    public BasePopupWindow setBackPressEnable(final boolean backPressEnable) {
        mPopupWindow.setBackgroundDrawable(backPressEnable ? new ColorDrawable() : null);
        return this;
    }
```

你没看错，就是对background进行设置，如此简单。。。

至于为何可以这么简单，各位看官不妨看下去。

对于`PopupWindow`的坑，绝大部分都是官方挖出来的，我们的库，其实。。。真的是在填坑啊- -

分析`PopupWindow`的代码，其中最重要的一部分，莫过于`preparePopup()`这一个方法了，因为我们大多数的坑，都来源于这个方法。。。

#### Before Android M

在M之前，`preparePopup()`的方法主要如下**(截取api 21的)**：

```java
 private void preparePopup(WindowManager.LayoutParams p) {
        if (mContentView == null || mContext == null || mWindowManager == null) {
            throw new IllegalStateException("You must specify a valid content view by "
                    + "calling setContentView() before attempting to show the popup.");
        }

        if (mBackground != null) {
            final ViewGroup.LayoutParams layoutParams = mContentView.getLayoutParams();
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            if (layoutParams != null &&
                    layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            // when a background is available, we embed the content view
            // within another view that owns the background drawable
            PopupViewContainer popupViewContainer = new PopupViewContainer(mContext);
            PopupViewContainer.LayoutParams listParams = new PopupViewContainer.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, height
            );
            popupViewContainer.setBackground(mBackground);
            popupViewContainer.addView(mContentView, listParams);

            mPopupView = popupViewContainer;
        } else {
            mPopupView = mContentView;
        }

        mPopupView.setElevation(mElevation);
        mPopupViewInitialLayoutDirectionInherited =
                (mPopupView.getRawLayoutDirection() == View.LAYOUT_DIRECTION_INHERIT);
        mPopupWidth = p.width;
        mPopupHeight = p.height;
    }
```

其中，我们可以很明显看到，在`mBackground`不为空的时候，`PopupWindow`的内容(mPopupView)是不同的，很明显看得出，我们的布局是被add到了创建出来的`PopupViewContainer`里面

而`PopupViewContainer`这家伙，其实是个`FrameLayout`

在这个`FrameLayout`里面，我们可以看到**KeyEvent**就是在这里被拦截的。（dispatchKeyEvent()）


```java
    private class PopupViewContainer extends FrameLayout {
        //..略
        @Override
        protected int[] onCreateDrawableState(int extraSpace) {
          //..略
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                if (getKeyDispatcherState() == null) {
                    return super.dispatchKeyEvent(event);
                }

                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getRepeatCount() == 0) {
                    KeyEvent.DispatcherState state = getKeyDispatcherState();
                    if (state != null) {
                        state.startTracking(event, this);
                    }
                    return true;
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                    KeyEvent.DispatcherState state = getKeyDispatcherState();
                    if (state != null && state.isTracking(event) && !event.isCanceled()) {
                        dismiss();
                        return true;
                    }
                }
                return super.dispatchKeyEvent(event);
            } else {
                return super.dispatchKeyEvent(event);
            }
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (mTouchInterceptor != null && mTouchInterceptor.onTouch(this, ev)) {
                return true;
            }
            return super.dispatchTouchEvent(ev);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            final int x = (int) event.getX();
            final int y = (int) event.getY();
            
            if ((event.getAction() == MotionEvent.ACTION_DOWN)
                    && ((x < 0) || (x >= getWidth()) || (y < 0) || (y >= getHeight()))) {
                dismiss();
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                dismiss();
                return true;
            } else {
                return super.onTouchEvent(event);
            }
        }

        @Override
        public void sendAccessibilityEvent(int eventType) {
            //..略
        }
    }
```

####  Over Android M

相信这里不用我多说，接下来我们看看M以及以上的`PopupWindow`是怎么玩的吧

截取api 25的`PopupWindow`的`preparePopup()`代码：

```java
    private void preparePopup(WindowManager.LayoutParams p) {
        if (mContentView == null || mContext == null || mWindowManager == null) {
            throw new IllegalStateException("You must specify a valid content view by "
                    + "calling setContentView() before attempting to show the popup.");
        }

        // The old decor view may be transitioning out. Make sure it finishes
        // and cleans up before we try to create another one.
        if (mDecorView != null) {
            mDecorView.cancelTransitions();
        }

        // When a background is available, we embed the content view within
        // another view that owns the background drawable.
        if (mBackground != null) {
            mBackgroundView = createBackgroundView(mContentView);
            mBackgroundView.setBackground(mBackground);
        } else {
            mBackgroundView = mContentView;
        }

        mDecorView = createDecorView(mBackgroundView);

        // The background owner should be elevated so that it casts a shadow.
        mBackgroundView.setElevation(mElevation);

        // We may wrap that in another view, so we'll need to manually specify
        // the surface insets.
        p.setSurfaceInsets(mBackgroundView, true /*manual*/, true /*preservePrevious*/);

        mPopupViewInitialLayoutDirectionInherited =
                (mContentView.getRawLayoutDirection() == View.LAYOUT_DIRECTION_INHERIT);
    }
```

我们依然可以看到这里是有对mBackground的判空，但是有一点不同的是，在M上，`PopupWindow`把Background和主控的View都分开了

其中`PopupBackgroundView`类也是一个`FrameLayout`，其代码如下：

```java
private class PopupBackgroundView extends FrameLayout {
        public PopupBackgroundView(Context context) {
            super(context);
        }

        @Override
        protected int[] onCreateDrawableState(int extraSpace) {
            if (mAboveAnchor) {
                final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
                View.mergeDrawableStates(drawableState, ABOVE_ANCHOR_STATE_SET);
                return drawableState;
            } else {
                return super.onCreateDrawableState(extraSpace);
            }
        }
    }
```

非常简单。。。只有一个针对Drawable状态的切换，并没有其他

回到`preparePopup`方法，我们留意到，在mBackground判空的if/else代码块外，有一个DecorView，那么这货又是什么呢

我们看一下:

```java
  private class PopupDecorView extends FrameLayout {
        private TransitionListenerAdapter mPendingExitListener;

        public PopupDecorView(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                if (getKeyDispatcherState() == null) {
                    return super.dispatchKeyEvent(event);
                }

                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                    final KeyEvent.DispatcherState state = getKeyDispatcherState();
                    if (state != null) {
                        state.startTracking(event, this);
                    }
                    return true;
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                    final KeyEvent.DispatcherState state = getKeyDispatcherState();
                    if (state != null && state.isTracking(event) && !event.isCanceled()) {
                        dismiss();
                        return true;
                    }
                }
                return super.dispatchKeyEvent(event);
            } else {
                return super.dispatchKeyEvent(event);
            }
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (mTouchInterceptor != null && mTouchInterceptor.onTouch(this, ev)) {
                return true;
            }
            return super.dispatchTouchEvent(ev);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            final int x = (int) event.getX();
            final int y = (int) event.getY();

            if ((event.getAction() == MotionEvent.ACTION_DOWN)
                    && ((x < 0) || (x >= getWidth()) || (y < 0) || (y >= getHeight()))) {
                dismiss();
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                dismiss();
                return true;
            } else {
                return super.onTouchEvent(event);
            }
        }
        public void requestEnterTransition(Transition transition) {
            //..
        }
        private void startEnterTransition(Transition enterTransition) {
          //..
        }
        public void startExitTransition(Transition transition, final View anchorRoot,
                final TransitionListener listener) {
           //..
        }
        public void cancelTransitions() {
          //..
        }

        private final OnAttachStateChangeListener mOnAnchorRootDetachedListener =
               //..
                };
    }
```

我们不看那些忽略掉的新增的Transition方法，随便一眼，我们就能看到`KeyEvent`。。。。

至此，我们已经明白，，，在M之后，返回键的捕捉已经不跟background挂钩了，无论有没有BackGround，都会被DecorView处理这个事件

而这，也正是我们的BasePopup库的setBackPressEnable()在M之后失效的原因

同时因为G大大们直接在dispatchKeyEvent里面写的，，，暂时，，没有好的解决方法呢


---  

不完美解决方法

在 Issue :[#33](https://github.com/razerdp/BasePopup/issues/33)里，有朋友给出了一个不完美的解决方法，感谢他的思路[@zl277287818](https://github.com/zl277287818)，如果不是他的评论，我都忘了我写过这个回调了哈哈。



---

## 1.8.9 解决方案：

再次研究源码，发现了一个突破点：`invokePopup()`

依然是源码走起：

先看看M之前的代码：

```java
  private void invokePopup(WindowManager.LayoutParams p) {
        if (mContext != null) {
            p.packageName = mContext.getPackageName();
        }
        mPopupView.setFitsSystemWindows(mLayoutInsetDecor);
        setLayoutDirectionFromAnchor();
        mWindowManager.addView(mPopupView, p);
    }
```

然后看看M之后的代码：

```java
    private void invokePopup(WindowManager.LayoutParams p) {
        if (mContext != null) {
            p.packageName = mContext.getPackageName();
        }

        final PopupDecorView decorView = mDecorView;
        decorView.setFitsSystemWindows(mLayoutInsetDecor);

        setLayoutDirectionFromAnchor();

        mWindowManager.addView(decorView, p);

        if (mEnterTransition != null) {
            decorView.requestEnterTransition(mEnterTransition);
        }
    }
```

上面说过，在M之前和M之后，引起返回键失效或者成功的原因在于`decorView`，M之前是根据background，M之后不关注background。

然而因为按键的监听是在`dispatchKeyEvent`，而KeyListener是在其之后才有效，所以我们没有办法好好监听按键事件。

然而我一直忽略了一件事：`decorView`作为popupwindow的最顶层，是直接被WindowManager给add到phone中的

![image](https://github.com/razerdp/BasePopup/blob/master/img/1.png)


无论是哪一份代码，但这一个操作是不会改变的，既然我们没法监听`KeyEvent`，那么我们直接给顶层View套上一层我们自定义的View（相当于代理）是否就可以成功拦截呢？

于是我就瞄准了`WindowManager`，至于添加时机，很明显，就是addView方法，因此我们需要代理一下`WindowManager`...

首先我们需要反射替换掉`PopupWindow`的`WindowManager`....

[BasePopupWindowProxy#176](https://github.com/razerdp/BasePopup/blob/master/lib/src/main/java/razerdp/basepopup/BasePopupWindowProxy.java#L176)

```java
  private void tryToProxyWindowManagerMethod(PopupWindow popupWindow) {
        try {
            if (hackWindowManager != null) return;
            Field fieldWindowManager = PopupWindow.class.getDeclaredField("mWindowManager");
            fieldWindowManager.setAccessible(true);
            final WindowManager windowManager = (WindowManager) fieldWindowManager.get(popupWindow);
            if (windowManager == null) return;
            hackWindowManager = new HackWindowManager(windowManager,mController);
            fieldWindowManager.set(popupWindow, hackWindowManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```

接着在我们的hackerWindowManager中进行代理。。。事实上本来是想动态代理的（因为WindowManager是个接口，条件允许动态代理）
但是因为当我们hack掉`addView()`方法后，`removeView()`方法也需要hack掉，否则会出现不对等的情况，而在动态代理中我们需要写一堆类似于：

```java
if(method.getName().equals("addView")){}
```

最后决定，还是直接代理算了- -而且也便于扩展

回到主题，我们代理中的处理需要针对不同版本来进行，但因为目前为止针对两个`decorView`操作都是一样的，所以直接采取判断View的名字而不判断系统版本：

```java
  private boolean checkProxyValided(View v) {
        if (v == null) return false;
        String viewSimpleClassName = v.getClass().getSimpleName();
        return TextUtils.equals(viewSimpleClassName, "PopupDecorView") || TextUtils.equals(viewSimpleClassName, "PopupViewContainer");
    }
```

最后处理一下其他方法就可以了：

```java
final class HackWindowManager implements WindowManager {
    private static final String TAG = "HackWindowManager";
    private WindowManager mWindowManager;
    private PopupController mPopupController;
    HackPopupDecorView mHackPopupDecorView;

    public HackWindowManager(WindowManager windowManager, PopupController popupTouchController) {
        mWindowManager = windowManager;
        mPopupController = popupTouchController;
    }

    @Override
    public Display getDefaultDisplay() {
        return mWindowManager.getDefaultDisplay();
    }

    @Override
    public void removeViewImmediate(View view) {
        if (checkProxyValided(view) && mHackPopupDecorView != null) {
            mWindowManager.removeViewImmediate(mHackPopupDecorView);
            mHackPopupDecorView.setPopupController(null);
            mHackPopupDecorView = null;
        } else {
            mWindowManager.removeViewImmediate(view);
        }
    }

    @Override
    public void addView(View view, ViewGroup.LayoutParams params) {
        Log.i(TAG, "addView:  " + view.getClass().getSimpleName());

        if (checkProxyValided(view)) {
            mHackPopupDecorView = new HackPopupDecorView(view.getContext());
            mHackPopupDecorView.setPopupController(mPopupController);
            mHackPopupDecorView.addView(view);
            mWindowManager.addView(mHackPopupDecorView, params);
        } else {
            mWindowManager.addView(view, params);
        }
    }

    @Override
    public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
        if (checkProxyValided(view) && mHackPopupDecorView != null) {
            mWindowManager.updateViewLayout(mHackPopupDecorView, params);
        } else {
            mWindowManager.updateViewLayout(view, params);
        }

    }

    @Override
    public void removeView(View view) {
        if (checkProxyValided(view) && mHackPopupDecorView != null) {
            mWindowManager.removeView(mHackPopupDecorView);
            mHackPopupDecorView.setPopupController(null);
            mHackPopupDecorView = null;
        } else {
            mWindowManager.removeView(view);
        }
    }


    private boolean checkProxyValided(View v) {
        if (v == null) return false;
        String viewSimpleClassName = v.getClass().getSimpleName();
        return TextUtils.equals(viewSimpleClassName, "PopupDecorView") || TextUtils.equals(viewSimpleClassName, "PopupViewContainer");
    }
}
```

最后的层级图如下：

![image](https://github.com/razerdp/BasePopup/blob/master/img/2.png)
