(ns status-desktop-front.override.recieve-message
  (:require [re-frame.core :as re-frame]
            [status-desktop-front.storage :as storage]))

(re-frame/reg-cofx
  :pop-up-chat?
  (fn [cofx]
    (assoc cofx :pop-up-chat? (fn [chat-id]
                                true))))

(re-frame/reg-cofx
  :get-last-clock-value
  (fn [cofx]
    (assoc cofx :get-last-clock-value storage/get-last-clock-value)))

(re-frame/reg-cofx
  :message-exists?
  (fn [cofx]
    (assoc cofx :message-exists? storage/message-exists?)))