# BasePopup
</br>
##2016-01-18 增加了含有输入框的popup，同时修复了dismiss由于调用getExitAnima()但是setListener/addListener无效的问题（原因是getExitAnima()属于重新new出来的动画，调用多次后，listener指向的并非同一个对象，所以无效）</br>
###InputPopup.java:
![image](https://github.com/razerdp/BasePopup/blob/master/img/input_popup.gif)
##2016-01-16 增加仿朋友圈评论的popup</br>
###同日的16:39 尝试添加了退出动画</br>
解析:http://blog.csdn.net/mkfrank/article/details/50532956
![image](https://github.com/razerdp/BasePopup/blob/master/img/comment_popup_with_exitAnima.gif)
###CommentPopup.java(该frag标题名字忘改回来了。。。录制了gif也就懒得动了):
![image](https://github.com/razerdp/BasePopup/blob/master/img/comment_popup.gif)
##2016-01-15 增加两种继承basepopup实现的常见Popup</br>
###ScalePopup.java:</br>
解析:http://blog.csdn.net/mkfrank/article/details/50523702
![image](https://github.com/razerdp/BasePopup/blob/master/img/scale_popup.gif)
###SlideFromBottomPopup.java:</br>
解析：http://blog.csdn.net/mkfrank/article/details/50527159
![image](https://github.com/razerdp/BasePopup/blob/master/img/slide_from_bottom_popup.gif)

