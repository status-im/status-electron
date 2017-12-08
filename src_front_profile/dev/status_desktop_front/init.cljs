(ns status-desktop-front.init
  (:require [figwheel.client :as fw :include-macros true]
            [status-desktop-front.core :as core]
            [taoensso.timbre :as log]
            [re-frisk-remote.core :refer [enable-re-frisk-remote!]]))

(enable-console-print!)
(log/set-level! :trace)

(defn start-descjop! []
  (core/mount-root))

(fw/watch-and-reload
 :websocket-url   "ws://localhost:3449/figwheel-ws"
 :jsload-callback start-descjop!)

(defn ^:export run
  []
  (enable-re-frisk-remote! {:on-init core/init}))

(defn ^:export log
  [message]
  (core/log message))

