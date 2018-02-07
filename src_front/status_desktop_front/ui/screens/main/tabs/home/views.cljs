(ns status-desktop-front.ui.screens.main.tabs.home.views
  (:require-macros [status-im.utils.views :as views])
  (:require [status-desktop-front.react-native-web :as react]
            [re-frame.core :as re-frame]
            [status-im.ui.screens.chats-list.styles :as chats-list.styles]
            [status-im.utils.gfycat.core :as gfycat]
            [status-desktop-front.ui.components.icons :as icons]))

(views/defview unviewed-indicator [chat-id]
  (let [unviewed-messages-count (re-frame/subscribe [:unviewed-messages-count chat-id])]
    (when (pos? @unviewed-messages-count)
      [react/view {:style (merge chats-list.styles/new-messages-container {:justify-content :center})}
       [react/text {:style chats-list.styles/new-messages-text
                    :font  :medium}
        @unviewed-messages-count]])))

(views/defview chat-list-item-inner-view [{:keys [chat-id name color online
                                                  group-chat contacts public?
                                                  public-key unremovable?] :as chat}]
  (let [name (str
               (if public? "#" "")
               (or name
                   (gfycat/generate-gfy public-key)))]
    [react/view {:style {:padding 12 :background-color :white :flex-direction :row :align-items :center}}
     (when public?
       [icons/icon :icons/public-chat])
     (when (and group-chat (not public?))
       [icons/icon :icons/group-chat])
     [react/text
      name]
     [react/view {:style {:flex 1}}]
     [unviewed-indicator chat-id]]))

(defn chat-list-item [[chat-id chat]]
  [react/touchable-highlight {:on-press #(re-frame/dispatch [:navigate-to-chat chat-id])}
   [react/view
    [chat-list-item-inner-view (assoc chat :chat-id chat-id)]]])

(views/defview chat-list-view []
  (views/letsubs [chats [:filtered-chats]]
    [react/view {:style {:flex 1 :background-color :white}}
     [react/view {:style {:align-items :center :flex-direction :row :padding 11}}
      [react/view {:style {:flex 1}}]
      [react/touchable-highlight {:on-press #(re-frame/dispatch [:navigate-to :new-contact])}
       [icons/icon :icons/add]]]
     [react/view {:style {:height 1 :background-color "#e8ebec" :margin-horizontal 16}}]
     [react/scroll-view
      [react/view
       (for [[index chat] (map-indexed vector chats)]
         ^{:key (:chat-id chat)} [chat-list-item chat])]]]))