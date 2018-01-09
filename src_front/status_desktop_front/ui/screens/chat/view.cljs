(ns status-desktop-front.ui.screens.chat.view
  (:require [status-desktop-front.react-native-web :as react]
            [status-desktop-front.ui.components.tabs :refer [main-tabs]]
            [re-frame.core :as re-frame]
            [status-desktop-front.ui.screens.chat.profile.views :as profile.views]
            [status-desktop-front.ui.components.icons :as icons]
            [status-desktop-front.web3-provider :as protocol]
            [clojure.string :as string]
            [status-im.chat.views.message.message :as message.views]
            [status-im.chat.styles.message.message :as message.style]
            [status-im.utils.gfycat.core :as gfycat.core]
            [status-im.utils.gfycat.core :as gfycat]
            [status-im.ui.screens.chats-list.styles :as chats-list.styles]
            [status-im.constants :as constants]
            [status-desktop-front.react-native-hyperlink :as rn-hl])
  (:require-macros [status-im.utils.views :as views]))

(views/defview message-author-name [{:keys [outgoing from] :as message}]
  (views/letsubs [current-account [:get-current-account]
                  incoming-name [:contact-name-by-identity from]]
    (if outgoing
      [react/text {:style message.style/author} (:name current-account)]
      (let [name (or incoming-name (gfycat/generate-gfy from))]
        [react/touchable-highlight {:on-press #(re-frame/dispatch [:show-contact-dialog from name (boolean incoming-name)])}
         [react/text {:style message.style/author} name]]))))

(defn message [text me? {:keys [outgoing message-id chat-id message-status user-statuses
                                from current-public-key content-type group-chat] :as message}]
  (when (= content-type constants/text-content-type)
    (reagent.core/create-class
      {:component-did-mount
       #(when (and message-id
                   chat-id
                   (not outgoing)
                   (not= :seen message-status)
                   (not= :seen (keyword (get-in user-statuses [current-public-key :status]))))
          (re-frame/dispatch [:send-seen! {:chat-id    chat-id
                                           :from       from
                                           :message-id message-id}]))
       :reagent-render
       (fn []
         [react/view {:style (merge
                               {:padding-bottom 8}
                               (if me?
                                 {:align-items   :flex-end
                                  :padding-left  90
                                  :padding-right 60}
                                 {:align-items   :flex-start
                                  :padding-left  60
                                  :padding-right 90}))}
          [react/view {:style {:padding 12 :background-color :white :border-radius 8}}
           (when group-chat [message-author-name message])
           [rn-hl/hyperlink {:linkStyle {:color "#2980b9"} :on-press #(re-frame/dispatch [:show-link-dialog %1])}
            [react/text
             text]]]])})))

(views/defview messages-view [{:keys [chat-id group-chat]}]
  (views/letsubs [chat-id* (atom nil)
                  scroll-ref (atom nil)
                  scroll-timer (atom nil)
                  scroll-height (atom nil)]
    (let [_ (when (or (not @chat-id*) (not= @chat-id* chat-id))
              (reset! chat-id* chat-id)
              (js/setTimeout #(when scroll-ref (.scrollToEnd @scroll-ref)) 400))
          messages (re-frame/subscribe [:get-chat-messages chat-id])
          current-public-key (re-frame/subscribe [:get-current-public-key])]
      [react/view {:style {:flex 1 :background-color "#eef2f5"}}
       [react/scroll-view {:scrollEventThrottle 16
                           :on-scroll (fn [e]
                                        (let [ne (.-nativeEvent e)
                                              y (.-y (.-contentOffset ne))]
                                          (when (zero? y)
                                            (when @scroll-timer (js/clearTimeout @scroll-timer))
                                            (reset! scroll-timer (js/setTimeout #(re-frame/dispatch [:load-more-messages]) 300)))
                                          (reset! scroll-height (+ y (.-height (.-layoutMeasurement ne))))))
                           :on-content-size-change #(when (or (not @scroll-height) (< (- %2 @scroll-height) 500))
                                                      (.scrollToEnd @scroll-ref))
                           :ref #(reset! scroll-ref %)}
        [react/view {:style {:padding-vertical 60}}
         (for [[index {:keys [from content message-id] :as message-obj}] (map-indexed vector (reverse @messages))]
           ^{:key message-id} [message content (= from @current-public-key) (assoc message-obj :group-chat group-chat)])]]])))

(views/defview status-view []
  [react/view {:style {:flex 1 :background-color "#eef2f5" :align-items :center :justify-content :center}}
   [react/text {:style {:font-size 18 :color "#939ba1"}}
    "Status.im"]])

(views/defview toolbar-chat-view []
  (views/letsubs [name [:chat :name]
                  chat-id [:get-current-chat-id]
                  pending-contact? [:current-contact :pending?]
                  public-key [:chat :public-key]]
    (let [chat-name (if (string/blank? name)
                      (gfycat.core/generate-gfy public-key)
                      (or name                              ;(get-contact-translated chat-id :name name)
                          "Chat name"))]                    ;(label :t/chat-name)))]
      [react/view {:style {:height 64 :align-items :center :padding-horizontal 11 :justify-content :center}}
       [react/text {:style {:font-size 16 :color :black :font-weight "600"}}
        chat-name]
       (when pending-contact?
         [react/touchable-highlight
          {:on-press #(re-frame/dispatch [:add-pending-contact chat-id])}
          [react/view                                       ;style/add-contact
           [react/text {:style {:font-size 14 :color "#939ba1" :margin-top 3}}
            "Add to contacts"]]])])))
;[react/text {:style {:font-size 14 :color "#939ba1" :margin-top 3}}
;"Contact status not implemented"]])))

(views/defview chat-text-input []
  (views/letsubs [input-text [:chat :input-text]
                  inp-ref (atom nil)]
    [react/view {:style {:height     90 :margin-horizontal 16 :margin-bottom 16 :background-color :white :border-radius 12
                         :box-shadow "0 0.5px 4.5px 0 rgba(0, 0, 0, 0.04)"}}
     [react/view {:style {:flex-direction :row :margin-horizontal 16 :margin-top 16 :flex 1 :margin-bottom 16}}
      [react/view {:style {:flex 1}}
       [react/text-input {:default-value  (or input-text "")
                          :placeholder    "Type a message..."
                          :auto-focus     true
                          :multiline      true
                          :blur-on-submit true
                          :style          {:flex 1}
                          :ref            #(reset! inp-ref %)
                          :on-key-press   (fn [e]
                                            (let [native-event (.-nativeEvent e)
                                                  key (.-key native-event)]
                                              (when (= key "Enter")
                                                (js/setTimeout #(do
                                                                  (.clear @inp-ref)
                                                                  (.focus @inp-ref)) 200)
                                                (re-frame/dispatch [:send-current-message]))))
                          :on-change      (fn [e]
                                            (let [native-event (.-nativeEvent e)
                                                  text (.-text native-event)]
                                              (re-frame/dispatch [:set-chat-input-text text])))}]]
      [react/touchable-highlight {:on-press (fn []
                                              (js/setTimeout #(do (.clear @inp-ref)(.focus @inp-ref)) 200)
                                              (re-frame/dispatch [:send-current-message]))}
       [react/view {:style {:margin-left     16 :width 30 :height 30 :border-radius 15 :background-color "#eef2f5" :align-items :center
                            :justify-content :center}}
        [icons/icon :icons/dropdown-up]]]]]))

(views/defview chat-view []
  (views/letsubs [current-chat [:get-current-chat]]
    [react/view {:style {:flex 1 :background-color "#eef2f5"}}
     [toolbar-chat-view]
     [react/view {:style {:height 1 :background-color "#e8ebec" :margin-horizontal 16}}]
     [messages-view current-chat]
     [chat-text-input]]))


(views/defview new-contact []
  (views/letsubs [new-contact-identity [:get :contacts/new-identity]
                  topic [:get :public-group-topic]]
    [react/view {:style {:flex 1 :background-color "#eef2f5"}}
     ^{:key "newcontact"}
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
         [icons/icon :icons/ok]]]]]
     ^{:key "publicchat"}
     [react/view {:style {:height 64 :align-items :center :padding-horizontal 11 :justify-content :center}}
      [react/text {:style {:font-size 16 :color :black :font-weight "600"}}
       "Join to public chat"]]
     [react/view {:style {:height 1 :background-color "#e8ebec" :margin-horizontal 16}}]
     [react/view {:style {:height     90 :margin-horizontal 16 :margin-bottom 16 :background-color :white :border-radius 12
                          :box-shadow "0 0.5px 4.5px 0 rgba(0, 0, 0, 0.04)"}}
      [react/view {:style {:flex-direction :row :margin-horizontal 16 :margin-top 16}}
       [react/text "#"]
       [react/view {:style {:flex 1}}
        [react/text-input {:placeholder "topic"
                           :on-change   (fn [e]
                                          (let [native-event (.-nativeEvent e)
                                                text (.-text native-event)]
                                            (re-frame/dispatch [:set :public-group-topic text])))}]]
       [react/touchable-highlight {:on-press #(re-frame/dispatch [:create-new-public-chat topic])}
        [react/view {:style {:margin-left     16 :width 30 :height 30 :border-radius 15 :background-color "#eef2f5" :align-items :center
                             :justify-content :center}}
         [icons/icon :icons/ok]]]]]]))

(defn contact-item [{:keys [whisper-identity name] :as contact}]
  [react/touchable-highlight {:on-press #(re-frame/dispatch [:open-chat-with-contact contact])}
   [react/view {:style {:padding 12 :background-color :white}}
    [react/text
     name]]])

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
  (let [name (or name
                 (gfycat/generate-gfy public-key))]
    [react/view {:style {:padding 12 :background-color :white :flex-direction :row :align-items :center}}
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
     [react/view {:style {:height 64 :align-items :center :flex-direction :row :padding-horizontal 11}}
      [icons/icon :icons/hamburger]
      [react/view {:style {:flex 1 :margin-horizontal 11 :height 38 :border-radius 8 :background-color "#edf1f3"}}]
      [react/touchable-highlight {:on-press #(re-frame/dispatch [:navigate-to :new-contact])}
       [icons/icon :icons/add]]]
     [react/view {:style {:height 1 :background-color "#e8ebec" :margin-horizontal 16}}]
     [react/scroll-view
      [react/view
       (for [[index chat] (map-indexed vector chats)]
         ^{:key (:chat-id chat)} [chat-list-item chat])]]]))

(views/defview contacts-list-view []
  (views/letsubs [contacts [:all-added-group-contacts-filtered nil]]
    [react/view {:style {:flex 1 :background-color "#eef2f5"}}
     [react/view {:style {:height 64 :align-items :center :flex-direction :row :padding-horizontal 11}}
      [icons/icon :icons/hamburger]
      [react/view {:style {:flex 1 :margin-horizontal 11 :height 38 :border-radius 8 :background-color "#edf1f3"}}]
      [react/touchable-highlight {:on-press #(re-frame/dispatch [:navigate-to :new-contact])}
       [icons/icon :icons/add]]]
     [react/view {:style {:height 1 :background-color "#e8ebec" :margin-horizontal 16}}]
     [react/scroll-view
      [react/view
       (for [[index contact] (map-indexed vector contacts)]
         ^{:key index} [contact-item contact])]]]))


(views/defview tab-views []
  (views/letsubs [tab [:get :left-view-id]]
    (when tab
      (let [component (case tab
                        :profile profile.views/profile
                        :contact-list contacts-list-view
                        :chat-list chat-list-view
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
    [react/view {:style {:flex 1}}
     [tab-views]]
    [main-tabs]]
   [main-views]])
