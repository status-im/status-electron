(ns status-desktop-front.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            status-im.ui.screens.subs
            status-desktop-front.storage
            status-desktop-front.ui.screens.events
            [status-desktop-front.ui.screens.views :as views]
            [cljs.nodejs :as nodejs]))

(def electron  (nodejs/require "electron"))
(def remote    (.-remote electron))
(def Menu      (.-Menu remote))
(def MenuItem  (.-MenuItem remote))

(defn mount-root []
  (reagent/render [views/main]
                  (.getElementById js/document "app")))

(defn init []
  (let [menu (.getApplicationMenu Menu)]
    (.append menu
             (MenuItem.
               (clj->js {:label "Edit"
                         :submenu [{ :role "cut"}
                                   { :role "copy"}
                                   { :role "paste"}
                                   { :role "pasteandmatchstyle"}
                                   { :role "delete"}
                                   { :role "selectall"}]})))
    (.append menu
             (MenuItem.
               (clj->js {:label "Status" :submenu [{ :label "Logs" :click #(re-frame/dispatch [:logs]) :accelerator "CmdOrCtrl+L"}]})))
    (.setApplicationMenu Menu menu))
  (mount-root)
  (re-frame/dispatch-sync [:initialize-app]))

(defn log [message]
  (re-frame/dispatch [:log-message message]))


