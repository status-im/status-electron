(ns status-desktop-front.ui.screens.chat.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  :messages
  (fn [db _]
    (get db :messages)))

(re-frame/reg-sub
  :message-text
  (fn [db _]
    (get db :message-text)))