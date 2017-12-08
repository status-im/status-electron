(ns status-desktop-front.override.chat
  (:require [re-frame.core :as re-frame]
            [status-im.chat.events :as chat.events]
            [status-im.utils.handlers :as handlers]
            [status-im.constants :as constants]))


;;;; Coeffects

(re-frame/reg-cofx
  :stored-unviewed-messages
  (fn [cofx _]
    (assoc cofx :stored-unviewed-messages [])));(msg-store/get-unviewed))))

(re-frame/reg-cofx
  :get-stored-message
  (fn [cofx _]
    (assoc cofx :get-stored-message (fn [_] nil))));msg-store/get-by-id)))

(re-frame/reg-cofx
  :get-stored-messages
  (fn [cofx _]
    (assoc cofx :get-stored-messages (fn [_] nil))));msg-store/get-by-chat-id)))

(re-frame/reg-cofx
  :get-last-stored-message
  (fn [cofx _]
    (assoc cofx :get-last-stored-message (fn [] nil))));msg-store/get-last-message)))

(re-frame/reg-cofx
  :get-message-previews
  (fn [cofx _]
    (assoc cofx :message-previews [])));(msg-store/get-previews)

(re-frame/reg-cofx
  :all-stored-chats
  (fn [cofx _]
    (assoc cofx :all-stored-chats [])));(chats-store/get-all))))

(re-frame/reg-cofx
  :get-stored-chat
  (fn [cofx _]
    (assoc cofx :get-stored-chat (fn [] nil))));chats-store/get-by-id)))

;;;; Effects

(re-frame/reg-fx
  :update-message
  (fn [message]))
    ;(msg-store/update-message message)))

(re-frame/reg-fx
  :save-message
  (fn [{:keys [chat-id] :as message}]))
    ;(msg-store/save chat-id message)))

(re-frame/reg-fx
  :save-chat
  (fn [chat]))
    ;(chats-store/save chat)))

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