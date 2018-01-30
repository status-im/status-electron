(ns env.main
  (:require [figwheel.client :as fw :include-macros true]
            [status-desktop-front.core :as core]
            [taoensso.timbre :as log]
            [status-im.utils.handlers :as handlers]
            [re-frisk-remote.core :refer [enable-re-frisk-remote! pre-event-callback]]))

(enable-console-print!)
(log/set-level! :trace)

(defn start-descjop! []
  (core/mount-root))

(fw/watch-and-reload
 :websocket-url   "ws://localhost:3449/figwheel-ws"
 :jsload-callback start-descjop!)

(core/init)
