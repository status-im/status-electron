(ns status-desktop-front.ui.screens.chat.events
  (:require [re-frame.core :as re-frame]
            [status-desktop-front.protocol :as protocol]))

(re-frame/reg-event-db
  :set-message-text
  (fn [db [_ text]]
    (assoc db :message-text text)))

(re-frame/reg-event-db
  :send-message
  (fn [{:keys [message-text] :as db} _]
    (protocol/send-message message-text)
    (-> db
        (assoc :message-text "")
        (update :messages conj {:me? true :text message-text}))))