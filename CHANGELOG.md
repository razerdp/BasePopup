# v1.3.0

Because of some methods' name are misleading,we will change them after **ver 1.3.0**

commit details:

| Old name | New name | Note |
| -------- | :-----------: | :---------: |
| getPopupView() | onCreatePopupView() | getPopupView() will be **Deprecated** |
| getAnimaView() | initAnimaView() | getAnimaView will be **Deprecated**  |
| mContext | getContext() | the **mContext** field will be changed to **private**,use `getContext()` instead  |
| mPopupView | getPopupWindowView() | the **mPopupView ** field will be changed to **private**,use `getPopupWindowView()` instead |
| getShowAnimation()/getExitAnimation() |**【protect】** initShowAnimation()/initExitAnimation() | getShowAnimation()/getExitAnimation() will return animation whitch has been initialized |
| getShowAnimator()/getExitAnimator() |**【protect】** initShowAnimator()/initExitAnimator() | getShowAnimator()/getExitAnimator() will return animator whitch has been initialized |

Please forgive us for the inconvenience brought to you.