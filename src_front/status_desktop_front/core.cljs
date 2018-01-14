(ns status-desktop-front.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            status-im.ui.screens.subs
            status-desktop-front.storage
            status-desktop-front.ui.screens.events
            [status-desktop-front.ui.screens.views :as views]
            [cljs.nodejs :as nodejs]))


(defn mount-root []
  (reagent/render [views/main]
                  (.getElementById js/document "app")))

(defn init []
  (mount-root)
  (re-frame/dispatch-sync [:initialize-app]))

(defn log [message]
  (re-frame/dispatch [:log-message message]))


