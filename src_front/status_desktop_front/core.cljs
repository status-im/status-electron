(ns status-desktop-front.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            status-im.ui.screens.subs
            ;status-desktop-front.storage
            status-desktop-front.ui.screens.events
            [status-desktop-front.ui.screens.views :as views]
            ;[cljs.nodejs :as nodejs]
            [status-desktop-front.react-native-web :as react]
            ))

(defn app-root []

  (reagent/create-class
    {
     :component-did-mount (fn [] ())
     :display-name "root"
     :reagent-render views/main}))

(defn mount-root [root-el]
    (.registerComponent react/app-registry "StatusDesktop" #(reagent/reactify-component root-el)))


(defn init [root-el]
  (mount-root root-el)
  (re-frame/dispatch-sync [:initialize-app]))

(defn log [message]
  (re-frame/dispatch [:log-message message]))


