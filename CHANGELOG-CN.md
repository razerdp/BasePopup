# v1.3.0

因为在使用过程中发现了一些方法的命名存在误导性，这个问题是因为在初期写下了方法名之后一直忘了改，因此在 **v 1.3.0**之后这些名字将会改变，具体改动如下：

| 原方法名 | 现方法名 | 备注 |
| -------- | :-----------: | :---------: |
| getPopupView() | onCreatePopupView() | 本方法改名为的是减少误导性 |
| getAnimaView() | initAnimaView() | 理由同上 |
| mContext | getContext() | context将会改为private，需要使用方法获取 |
| mPopupView | getPopupWindowView() | mPopupView将会改为private，需要用方法获取 |
| getShowAnimation()/getExitAnimation() |**【protect】** initShowAnimation()/initExitAnimation() | getShowAnimation()/getExitAnimation()将会改为获取进行过初始化的animation |
| getShowAnimator()/getExitAnimator() |**【protect】** initShowAnimator()/initExitAnimator() | getShowAnimator()/getExitAnimator()将会改为获取进行过初始化的animator |


事实上在下也清楚有很多方法命名还是不太好，如果您有更好的命名欢迎提交pr，同时这次的改动对您的使用造成万分不便，在下深表歉意，希望得到您的谅解。
