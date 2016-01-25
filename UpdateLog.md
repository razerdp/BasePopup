# BasePopup
</br>
##2016-01-25 增加了一个好玩的其实并无卵用的的dialog popup，看着好玩~gif图因为帧率问题，高帧慢，低帧丢细节，所以看起来效果不太好，实际效果很好玩的-V-</br>
###DialogPopup.java:
![image](https://github.com/razerdp/BasePopup/blob/master/img/dialog_popup.gif)
##2016-01-23 忽然发现一直以来提交代码的帐号是我的子帐号，现在切换回来。。。。
##2016-01-22 增加了一个常见的菜单式的popup(demo)，关于动画问题，我配置的是简单的缩放和透明度变化，可以按照个人爱好定义,顺便修正了BasePopupWindow的一个小小的坑</br>
###MenuPopup.java:
![image](https://github.com/razerdp/BasePopup/blob/master/img/menu_popup.gif)
##2016-01-20 增加了包含listview的popup(demo)，这个popup将采用builder模式构造，同时点击事件可以通过绑定clickTag来建立一个映射关系，这样就不用判断点击的位置来执行对应的步骤（当然，点击位置这个传统的操作还是保留的）</br>
###ListPopup.java:
![image](https://github.com/razerdp/BasePopup/blob/master/img/list_popup.gif)
##2016-01-19 稍微重构BasePopupWindow，在构造器把getXXX各种get方法赋值，防止每次调用的时候都new一个对象导致的性能问题&因为对象地址不对导致的各种奇葩问题</br>
##2016-01-18 增加了含有输入框的popup(demo)，同时修复了dismiss由于调用getExitAnima()但是setListener/addListener无效的问题（原因是getExitAnima()属于重新new出来的动画，调用多次后，listener指向的并非同一个对象，所以无效）</br>
###InputPopup.java:
![image](https://github.com/razerdp/BasePopup/blob/master/img/input_popup.gif)
##2016-01-16 增加仿朋友圈评论的popup(demo)</br>
###同日的16:39 尝试添加了退出动画
![image](https://github.com/razerdp/BasePopup/blob/master/img/comment_popup_with_exitAnima.gif)
解析:http://blog.csdn.net/mkfrank/article/details/50532956
###CommentPopup.java(该frag标题名字忘改回来了。。。录制了gif也就懒得动了):
![image](https://github.com/razerdp/BasePopup/blob/master/img/comment_popup.gif)
##2016-01-15 增加两种继承basepopup实现的常见Popup(demo)</br>
###ScalePopup.java:
![image](https://github.com/razerdp/BasePopup/blob/master/img/scale_popup.gif)
解析:http://blog.csdn.net/mkfrank/article/details/50523702
###SlideFromBottomPopup.java:
![image](https://github.com/razerdp/BasePopup/blob/master/img/slide_from_bottom_popup.gif)
解析：http://blog.csdn.net/mkfrank/article/details/50527159

