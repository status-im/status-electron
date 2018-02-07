(ns status-desktop-front.ui.screens.main.views
  (:require-macros [status-im.utils.views :as views])
  (:require [status-desktop-front.react-native-web :as react]
    [status-desktop-front.ui.components.tabs :as tabs]
    [status-desktop-front.ui.screens.main.tabs.profile.views :as profile.views]
    [status-desktop-front.ui.screens.main.tabs.home.views :as home.views]
    [status-desktop-front.ui.screens.main.chat.views :as chat.views]
    [status-desktop-front.ui.screens.main.add-new.views :as add-new.views]))

(views/defview status-view []
  [react/view {:style {:flex 1 :background-color "#eef2f5" :align-items :center :justify-content :center}}
   [react/text {:style {:font-size 18 :color "#939ba1"}}
    "Status.im"]])

(views/defview tab-views []
  (views/letsubs [tab [:get :left-view-id]]
    (when tab
      (let [component (case tab
                        :profile profile.views/profile
                        :chat-list home.views/chat-list-view
                        react/view)]
        [react/view {:style {:flex 1}}
         [component]]))))

(views/defview main-view []
  (views/letsubs [view-id [:get :view-id]]
    (when view-id
      (let [component (case view-id
                        :chat chat.views/chat-view
                        :new-contact add-new.views/new-contact
                        status-view)]
        [react/view {:style {:flex 1}}
         [component]]))))

(views/defview main-views []
  [react/view {:style {:flex 1 :flex-direction :row}}
   [react/view {:style {:width 280 :background-color :white}}
    [react/view {:style {:flex 1}}
     [tab-views]]
    [tabs/main-tabs]]
   [react/view {:style {:width 1 :background-color "#e8ebec"}}]
   [main-view]])