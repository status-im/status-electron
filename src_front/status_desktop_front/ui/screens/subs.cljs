(ns status-desktop-front.ui.screens.subs
  (:require [re-frame.core :as re-frame]
            status-desktop-front.ui.screens.chat.subs))

(re-frame/reg-sub
  :view-id
  (fn [db _]
    (get db :view-id)))