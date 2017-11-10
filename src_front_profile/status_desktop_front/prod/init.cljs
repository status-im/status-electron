(ns status-desktop-front.init
    (:require [status-desktop-front.core :as core]
              [status-desktop-front.conf :as conf]))

(enable-console-print!)

(defn start-descjop! []
  (core/init! conf/setting))

(start-descjop!)
