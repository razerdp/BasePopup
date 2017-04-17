# BasePopup
</br>

## 2017/01/12

使用PopupWindowProxy，覆写dismiss();

## 2016-12-12

现在`showPopupWindow(View v)`或者`showPopupWindow(int resid)`将会把popupwindow与anchorView挂钩哦，左上角会对齐（width=match_parent除外）

另外增加一个执行popup前的回调`OnBeforeShowCallback`，与beforedismiss一样，返回false则不执行showpopup，另外在这里可以先实现offsetX或者offsetY哦~

详情看[issue11](https://github.com/razerdp/BasePopup/issues/11)

## 2016-12-07

`ondismissListener`增加`onBeforeDismiss()`方法，在执行dismiss之前根据该值确定是否执行dismiss

也可以在执行dismiss前执行一些操作，详情操作请看`SlideFromTopPopupFrag.java`



## 2016-12-06

因为某些情况下需要用到showAsDropDown，因此增加dropdown方法`setShowAtDown()`详情看issue:[#issue10](https://github.com/razerdp/BasePopup/issues/10)

另外增加一个点击popup外部不消失的方法，默认点击外部消失`setDismissWhenTouchOuside()`

去除复杂的setRelativePivot方法，更新demo工程


## 2016-12-02

修复setOnDismissListener的错误 [#issue9](https://github.com/razerdp/BasePopup/issues/9)


## 2016-11-23

构造方法的view点击事件设置问题修复/getInputView必须返回edittext

## 2016-11-23
增加了一些方法：

- `setRelativePivot()`：该方法用于设置popup的参考中心点（相对于anchorView左上角）,使用注解，@RelativePivot
    + 在上述方法的前提下，增加了偏移量的方法
        + `setOffsetX()`:x偏移量，跟中心点有关,假如参考点在右边，那么正数则是远离参考view，负数相反，类似于margin
        + `setOffsetX()`:y偏移量，同上

- `setAutoLocatePopup()`:设置popup是否自适配屏幕，当popup显示位置不足以支撑其完整显示的时候，将会自动调整(比如上方正常显示，在下方无法显示的时候则显示在上方)
    + 此时同样可以使用上述方法`setRelativePivot()`等

- `getPopupViewWidth()/getPopupViewHeight()`:在创建view的时候就进行了view.measure，但是不能保证这两个值是完全可信的(比如popup内部是个listview?)
- `setRelativeToAnchorView()`:是否参考锚点view，事实上，如果使用了`setRelativePivot()`，该值自动为ture
- `setPopupGravity()`:设置poup的gravity，虽然一般情况下不建议设置，毕竟它的gravity很多时候都是相对于整个rootView来说的，容易混乱



## 2016-10-11
修正api>21下出现的popup无法突破状态栏的问题(method:setPopupWindowFullScreen(boolean))

***
## 2016-05-20
增加了popup的淡入淡出效果，道理很简单。。。弄回sytle，同时开放style的设置方法。淡入淡出效果是默认的，如果您不需要淡入淡出效果，可以通过setNeedPopupFade（false）取消

***

## 2016-05-20
因为发现展示了Popup后，如果不断地dismiss，会导致动画重复播放，然后最终留下一个蒙层，因此加入了防止执行动画过程中再次被执行的标志
下一阶段期望增加：蒙层淡入淡出的方法

***
## 2016-05-18
降低最高版本要求23->21，因为开发过程中发现有时候需要dismiss动画有时候不需要，因此增加dismissWithOutAnima方法

***
## 2016-03-05
修改名字ViewCreate->BasePopup

***
## 2016-02-23
去除master分支的兼容包，最低要求api 16，需要兼容到2.3请查看master-api 9分支

***
## 2016-01-28
修改了BasePopupWindow，将部分抽象方法改为public，防止子类继承的时候必须实现过多无用方法，保持整洁性。

***
## 2016-01-28 因为好奇，研究了一下插值器，发现了一个很好玩的网站http://inloop.github.io/interpolator/ </br>
这个网站有着可视化插值器和公式，于是这回就把公式集成了下来，做出了各种插值器的popup，但其实只有第一个最好玩，其他的马马虎虎。这次的自定义插值器是继承LinearInterpolator然后通过网站上的公式进行计算后复写对应接口实现的。同时将BasePopupWindow的一些anima变量改为protected。
### CustomInterpolatorPopup.java:
![image](https://github.com/razerdp/BasePopup/blob/master/img/interpolator_popup.gif)

***
## 2016-01-25 修复了inputView无法自动弹出输入法的问题（原因可能是因为popup在show出来后才可以获取焦点，而我们的inputview一开始就findViewById出来了），所以可能是null。

***
## 2016-01-25 增加了一个好玩的其实并无卵用的的dialog popup，看着好玩~gif图因为帧率问题，高帧慢，低帧丢细节，所以看起来效果不太好，实际效果很好玩的-V-</br>
### DialogPopup.java:
![image](https://github.com/razerdp/BasePopup/blob/master/img/dialog_popup.gif)

***
## 2016-01-23 忽然发现一直以来提交代码的帐号是我的子帐号，现在切换回来。。。。

***
## 2016-01-22 增加了一个常见的菜单式的popup(demo)，关于动画问题，我配置的是简单的缩放和透明度变化，可以按照个人爱好定义,顺便修正了BasePopupWindow的一个小小的坑</br>
### MenuPopup.java:
![image](https://github.com/razerdp/BasePopup/blob/master/img/menu_popup.gif)

***
## 2016-01-20 增加了包含listview的popup(demo)，这个popup将采用builder模式构造，同时点击事件可以通过绑定clickTag来建立一个映射关系，这样就不用判断点击的位置来执行对应的步骤（当然，点击位置这个传统的操作还是保留的）</br>
### ListPopup.java:
![image](https://github.com/razerdp/BasePopup/blob/master/img/list_popup.gif)

***
## 2016-01-19 稍微重构BasePopupWindow，在构造器把getXXX各种get方法赋值，防止每次调用的时候都new一个对象导致的性能问题&因为对象地址不对导致的各种奇葩问题</br>

***
## 2016-01-18 增加了含有输入框的popup(demo)，同时修复了dismiss由于调用getExitAnima()但是setListener/addListener无效的问题（原因是getExitAnima()属于重新new出来的动画，调用多次后，listener指向的并非同一个对象，所以无效）</br>
### InputPopup.java:
![image](https://github.com/razerdp/BasePopup/blob/master/img/input_popup.gif)

***
## 2016-01-16 增加仿朋友圈评论的popup(demo)</br>

***
### 同日的16:39 尝试添加了退出动画
![image](https://github.com/razerdp/BasePopup/blob/master/img/comment_popup_with_exitAnima.gif)
解析:http://blog.csdn.net/mkfrank/article/details/50532956

***
### CommentPopup.java(该frag标题名字忘改回来了。。。录制了gif也就懒得动了):
![image](https://github.com/razerdp/BasePopup/blob/master/img/comment_popup.gif)

***
## 2016-01-15 增加两种继承basepopup实现的常见Popup(demo)</br>
### ScalePopup.java:
![image](https://github.com/razerdp/BasePopup/blob/master/img/scale_popup.gif)
解析:http://blog.csdn.net/mkfrank/article/details/50523702

***
### SlideFromBottomPopup.java:
![image](https://github.com/razerdp/BasePopup/blob/master/img/slide_from_bottom_popup.gif)
解析：http://blog.csdn.net/mkfrank/article/details/50527159

