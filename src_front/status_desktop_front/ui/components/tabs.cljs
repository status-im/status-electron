(ns status-desktop-front.ui.components.tabs
  (:require [status-desktop-front.react-native-web :as react]
            [status-im.ui.components.tabs.styles :as tabs.styles]
            [re-frame.core :as re-frame]
            [status-desktop-front.ui.components.icons :as icons])
  (:require-macros [status-im.utils.views :as views]))

;;TODO copy-pate with minimum modifications of status-react tabs

(def tabs-list-data
  [{:view-id :profile
    :content {:title         "Profile"
              :icon-inactive :icons/wallet
              :icon-active   :icons/wallet-active}}
   {:view-id :chat-list
    :content {:title         "Chats"
              :icon-inactive :icons/chats
              :icon-active   :icons/chats-active}}
   {:view-id :discover
    :content {:title         "Discover"
              :icon-inactive :icons/discover
              :icon-active   :icons/discover-active}}
   {:view-id :contact-list
    :content {:title         "Contacts"
              :icon-inactive :icons/contacts
              :icon-active   :icons/contacts-active}}])

(defn- tab-content [{:keys [title icon-active icon-inactive]}]
  (fn [active?]
    [react/view {:style tabs.styles/tab-container}
     (let [icon (if active? icon-active icon-inactive)]
       [react/view
        [icons/icon icon {:color (:color (tabs.styles/tab-icon active?))}]])
     [react/view
      [react/text {:style (tabs.styles/tab-title active?)}
       title]]]))

(def tabs-list-indexed (map-indexed vector (map #(update % :content tab-content) tabs-list-data)))

(defn tab [index content view-id active?]
  [react/touchable-highlight {:style    (tabs.styles/tab active?)
                              :disabled active?
                              :on-press #(re-frame/dispatch [:set :left-view-id view-id])}
   [react/view
    [content active?]]])

(views/defview main-tabs []
  (views/letsubs [current-tab [:get :left-view-id]]
    [react/view {:styles {:border-color :red :border-width 1}}
     [react/view {:style tabs.styles/tabs-container}
      (for [[index {:keys [content view-id]}] tabs-list-indexed]
        ^{:key index} [tab index content view-id (= current-tab view-id)])]]))