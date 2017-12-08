(ns status-desktop-front.ui.screens.views
  (:require-macros [status-im.utils.views :as views])
  (:require [status-desktop-front.react-native-web :as react]
            [status-desktop-front.ui.screens.chat.view :as chat.view]
            [status-desktop-front.ui.screens.accounts.views :as accounts.views]
            [status-desktop-front.ui.screens.accounts.login.views :as login.views]))

(views/defview main []
  (views/letsubs [view-id [:get :view-id]
                  logs [:get :logs]]
    (if view-id
      (let [component (case view-id
                        :accounts accounts.views/accounts
                        :create-account accounts.views/create-account
                        (:chat-list :new-contact :chat) chat.view/chat
                        :login login.views/login)]
        [react/view {:style {:flex 1}}
         [react/view {:style {:flex 3}}
          [component]]
         [react/view {:style {:flex 1 :background-color :white}}
          [react/text "Logs"]
          [react/scroll-view
           [react/view
            [react/text logs]]]]]))))

