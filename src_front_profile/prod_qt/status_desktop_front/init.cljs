(ns status-desktop-front.init
  (:require [status-desktop-front.core :as core]
            [status-desktop-front.react-native-web :as react]
            status-im.react-native.js-dependencies
            )
  )

(enable-console-print!)

(core/init)

;;(defn ^:export run
;;  []
;;  (core/init))
;
;(defn ^:export log
;  [message]
;  (core/log message))