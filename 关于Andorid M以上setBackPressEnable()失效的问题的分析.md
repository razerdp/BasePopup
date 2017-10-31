## 关于Andorid M（6.0）及以上setBackPressEnable()失效的问题的分析

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




