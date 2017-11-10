(ns status-desktop-front.init
  (:require [figwheel.client :as fw :include-macros true]
            [status-desktop-front.core :as core]
            [status-desktop-front.conf :as conf]
            [reagent.core :as r]))

(enable-console-print!)

(fw/watch-and-reload
 :websocket-url   "ws://localhost:3449/figwheel-ws"
 :jsload-callback 'start-descjop!)

(def cnt (r/atom 0))
(defn reloader [] @cnt [status-desktop-front.ui.screens.chat.view/chat])
(def root-el (r/as-element [reloader]))


(defn start-descjop! []
  (println "CALLBACK")
  (swap! cnt inc)
  (core/init! conf/setting))

(start-descjop!)
