(ns status-desktop-front.ui.screens.events
  (:require [re-frame.core :as re-frame]
            status-desktop-front.ui.screens.chat.events))

(re-frame/reg-event-db
  :set-view
  (fn [db [_ view-id]]
    (assoc db :view-id view-id)))