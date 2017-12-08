(ns status-desktop-front.override.recieve-message
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-cofx
  :pop-up-chat?
  (fn [cofx]
    (assoc cofx :pop-up-chat? (fn [chat-id]
                                false))))

(re-frame/reg-cofx
  :get-last-clock-value
  (fn [cofx]
    (assoc cofx :get-last-clock-value (fn [chat-id] 0))))

(re-frame/reg-cofx
  :message-exists?
  (fn [cofx]
    (assoc cofx :message-exists? (fn [message-id] false))))