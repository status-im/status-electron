(ns status-desktop-front.ui.screens.chat.view
  (:require [status-desktop-front.react-native-web :as react]
            [status-desktop-front.ui.components.tabs :refer [main-tabs]]
            [re-frame.core :as re-frame]
            [status-desktop-front.ui.screens.chat.profile.views :as profile.views]
            [status-desktop-front.ui.components.icons :as icons]
            [status-desktop-front.web3-provider :as protocol])
  (:require-macros [status-im.utils.views :as views]))

(defn message [text & [me?]]
  [react/view {:style {:padding-bottom 8 :padding-horizontal 60 :flex-direction :row :flex 1}}
   (when-not me?
     [react/view {:style {:flex 1}}])
   [react/view {:style {:padding 12 :background-color :white :border-radius 8}}
    [react/text
     text]]])

(views/defview messages []
  ;(views/letsubs [messages [:messages]]
    [react/view {:style {:flex 1 :background-color "#eef2f5"}}
     [react/scroll-view
      [react/view {:style {:padding-vertical 60}}
       (for [[index {:keys [me? text]}] (map-indexed vector [])]
         ^{:key index} [message text me?])]]])

(views/defview status-view []
  [react/view {:style {:flex 1 :background-color "#eef2f5" :align-items :center  :justify-content :center}}
   [react/text {:style {:font-size 18 :color "#939ba1"}}
    "Status.im"]])

(views/defview chat-view []
  ;(views/letsubs [message-text [:message-text]]
    [react/view {:style {:flex 1 :background-color "#eef2f5"}}
     [react/view {:style {:height 64 :align-items :center :padding-horizontal 11 :justify-content :center}}
      [react/text {:style {:font-size 16 :color :black :font-weight "600"}}
       "Baha Men"]
      [react/text {:style {:font-size 14 :color "#939ba1" :margin-top 3}}
       "Super excited now"]]
     [react/view {:style {:height 1 :background-color "#e8ebec" :margin-horizontal 16}}]
     [messages]
     [react/view {:style {:height     90 :margin-horizontal 16 :margin-bottom 16 :background-color :white :border-radius 12
                          :box-shadow "0 0.5px 4.5px 0 rgba(0, 0, 0, 0.04)"}}
      [react/view {:style {:flex-direction :row :margin-horizontal 16 :margin-top 16}}
       [react/view {:style {:flex 1}}
        [react/text-input {;:value       (or message-text "")
                           :placeholder "Type a message..."
                           :on-change   (fn [e]
                                          (let [native-event (.-nativeEvent e)
                                                text (.-text native-event)]))}]]
                                            ;(re-frame/dispatch [:set-message-text text])))}]]
       [react/touchable-highlight {:on-press #(re-frame/dispatch [:send-message])}
        [react/view {:style {:margin-left     16 :width 30 :height 30 :border-radius 15 :background-color "#eef2f5" :align-items :center
                             :justify-content :center}}
         [icons/icon :icons/dropdown-up]]]]]])

(views/defview new-contact []
  (views/letsubs [new-contact-identity [:get :contacts/new-identity]]
    [react/view {:style {:flex 1 :background-color "#eef2f5"}}
     [react/view {:style {:height 64 :align-items :center :padding-horizontal 11 :justify-content :center}}
      [react/text {:style {:font-size 16 :color :black :font-weight "600"}}
       "Add new contact"]]
     [react/view {:style {:height 1 :background-color "#e8ebec" :margin-horizontal 16}}]
     [react/view {:style {:height     90 :margin-horizontal 16 :margin-bottom 16 :background-color :white :border-radius 12
                          :box-shadow "0 0.5px 4.5px 0 rgba(0, 0, 0, 0.04)"}}
      [react/view {:style {:flex-direction :row :margin-horizontal 16 :margin-top 16}}
       [react/view {:style {:flex 1}}
        [react/text-input {:placeholder "Public key"
                           :on-change   (fn [e]
                                          (let [native-event (.-nativeEvent e)
                                                text (.-text native-event)]
                                            (re-frame/dispatch [:set :contacts/new-identity text])))}]]
       [react/touchable-highlight {:on-press #(re-frame/dispatch [:add-contact-handler new-contact-identity])}
        [react/view {:style {:margin-left     16 :width 30 :height 30 :border-radius 15 :background-color "#eef2f5" :align-items :center
                             :justify-content :center}}
         [icons/icon :icons/ok]]]]]]))

(defn contact-item [name]
  [react/view {:style {:padding 12 :background-color :white}}
   [react/text
    name]])

(views/defview contacts-list-view []
  (views/letsubs [contacts [:all-added-group-contacts-filtered nil]]
    [react/view {:style {:flex 1 :background-color "#eef2f5"}}
     [react/scroll-view
      [react/view
       (for [[index {:keys [whisper-identity name]}] (map-indexed vector contacts)]
         ^{:key index} [contact-item name])]]]))


(views/defview tab-views []
  (views/letsubs [tab [:get :left-view-id]]
    (when tab
      (let [component (case tab
                        :profile profile.views/profile
                        :contact-list contacts-list-view
                        react/view)]
        [react/view {:style {:flex 1}}
         [component]]))))

(views/defview main-views []
  (views/letsubs [view-id [:get :view-id]]
    (when view-id
      (let [component (case view-id
                        :chat chat-view
                        :chat-list status-view
                        :new-contact new-contact
                        react/view)]
        [react/view {:style {:flex 1}}
         [component]]))))

(views/defview chat []
  [react/view {:style {:flex 1 :flex-direction :row}}
   [react/view {:style {:width 340 :background-color :white}}
    [react/view {:style {:height 64 :align-items :center :flex-direction :row :padding-horizontal 11}}
     [icons/icon :icons/hamburger]
     [react/view {:style {:flex 1 :margin-horizontal 11 :height 38 :border-radius 8 :background-color "#edf1f3"}}]
     [react/touchable-highlight {:on-press #(re-frame/dispatch [:navigate-to :new-contact])}
      [icons/icon :icons/add]]]
    [react/view {:style {:height 1 :background-color "#e8ebec" :margin-horizontal 16}}]
    [react/view {:style {:flex 1}}
     [tab-views]]
    [main-tabs]]
   [main-views]])
