(ns status-desktop-front.override.chat
  (:require [re-frame.core :as re-frame]
            [status-im.chat.events :as chat.events]
            [status-im.chat.handlers :as chat.handlers]
            [status-im.utils.handlers :as handlers]
            [status-im.constants :as constants]
            [status-desktop-front.storage :as storage]))

;;;; DESKTOP

;(def audio {:notif01 (js/Audio. (str js/__dirname "/resources/notif01.mp3"))
;            :notif02 (js/Audio. (str js/__dirname "/resources/notif02.mp3"))
;            :notif03 (js/Audio. (str js/__dirname "/resources/notif03.mp3"))
;            :notif04 (js/Audio. (str js/__dirname "/resources/notif04.mp3"))
;            :notif05 (js/Audio. (str js/__dirname "/resources/notif05.mp3"))
;            :notif06 (js/Audio. (str js/__dirname "/resources/notif06.mp3"))
;            :notif07 (js/Audio. (str js/__dirname "/resources/notif07.mp3"))
;            :notif08 (js/Audio. (str js/__dirname "/resources/notif08.mp3"))
;            :notif09 (js/Audio. (str js/__dirname "/resources/notif09.wav"))
;            :notif10 (js/Audio. (str js/__dirname "/resources/notif10.wav"))
;            :notif11 (js/Audio. (str js/__dirname "/resources/notif11.wav"))
;            :notif12 (js/Audio. (str js/__dirname "/resources/notif12.wav"))
;            :notif13 (js/Audio. (str js/__dirname "/resources/notif13.wav"))})

(re-frame/reg-fx
  :send-desktop-notification
  (fn [{:keys [content from sound]}]
    (when-not (.hasFocus js/document)
      ;(when sound (.play (get audio sound)))
      (js/Notification. from (clj->js {:body content :silent true})))))

;;;; old events

(re-frame/reg-fx
  ::chat.handlers/save-public-chat
  (fn [chat]
    (storage/save-chat chat)))


(re-frame/reg-fx
  ::chat.handlers/save-chat
  (fn [new-chat]
    (storage/save-chat new-chat)))

(re-frame/reg-cofx
  ::chat.handlers/chat-exists?
  (fn [coeffects _]
    (let [[{{:keys [group-id]} :payload}] (:event coeffects)]
      (assoc coeffects :chat-exists?
                       (storage/chat-exists? group-id)))))

(re-frame/reg-cofx
  ::chat.handlers/new-update?
  (fn [coeffects _]
    (let [[{{:keys [group-id timestamp]} :payload}] (:event coeffects)]
      (assoc coeffects :new-update?
                       (storage/chat-new-update? timestamp group-id)))))

(re-frame/reg-cofx
  ::chat.handlers/chat-is-active?
  (fn [coeffects _]
    (let [[{{:keys [group-id]} :payload}] (:event coeffects)]
      (assoc coeffects :chat-is-active?
                       (storage/chat-is-active? group-id)))))

    ;;;; Coeffects

(re-frame/reg-cofx
  :stored-unviewed-messages
  (fn [cofx _]
    (assoc cofx :stored-unviewed-messages [])));(msg-store/get-unviewed))))

(re-frame/reg-cofx
  :get-stored-message
  (fn [cofx _]
    (assoc cofx :get-stored-message storage/get-message-by-id)));msg-store/get-by-id)))

(re-frame/reg-cofx
  :get-stored-messages
  (fn [cofx _]
    (assoc cofx :get-stored-messages storage/get-messages-by-chat-id)));msg-store/get-by-chat-id)))

(re-frame/reg-cofx
  :get-last-stored-message
  (fn [cofx _]
    (assoc cofx :get-last-stored-message storage/get-last-message)));msg-store/get-last-message)))

(re-frame/reg-cofx
  :get-message-previews
  (fn [cofx _]
    (assoc cofx :message-previews [])));(msg-store/get-previews)

(re-frame/reg-cofx
  :all-stored-chats
  (fn [cofx _]
    (assoc cofx :all-stored-chats (storage/get-all-chats))))

(re-frame/reg-cofx
  :get-stored-chat
  (fn [cofx _]
    (assoc cofx :get-stored-chat storage/get-chat-by-id)));chats-store/get-by-id)))


(re-frame/reg-cofx
  :get-stored-unanswered-requests
  (fn [cofx _]
    (assoc cofx :stored-unanswered-requests []))) ;(requests-store/get-all-unanswered))))

(re-frame/reg-cofx
  :get-local-storage-data
  (fn [cofx]
    (assoc cofx :get-local-storage-data (fn [] nil)))) ;local-storage/get-data)))

;;;; Effects

(re-frame/reg-fx
  :update-message
  (fn [message]
    (storage/update-message message)))

(re-frame/reg-fx
  :save-message
  (fn [message]
    (storage/save-message message)))

(re-frame/reg-fx
  :save-chat
  (fn [chat]
    (storage/save-chat chat)))

(re-frame/reg-fx
  :save-all-contacts
  (fn [contacts]))
    ;(contacts-store/save-all contacts)))

(re-frame/reg-fx
  :protocol-send-seen
  (fn [params]))
    ;(protocol/send-seen! params)))

(re-frame/reg-fx
  :browse
  (fn [[command link]]))
   ; (list-selection/browse command link)))

;;;; Handlers

(handlers/register-handler-fx
  :show-mnemonic
  (fn [{db :db} _]
    {:db db}))

(handlers/register-handler-fx
  :navigate-to-chat
  [(re-frame/inject-cofx :get-stored-messages) re-frame/trim-v]
  (fn [cofx [chat-id {:keys [navigation-replace?]}]]
    (when-not (= chat-id constants/console-chat-id)
      (chat.events/navigate-to-chat cofx chat-id navigation-replace?))))

(handlers/register-handler :update-message-overhead! (fn [db _]
                                                       db))