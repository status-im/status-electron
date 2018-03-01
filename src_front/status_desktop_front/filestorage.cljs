(ns status-desktop-front.filestorage
 (:require [cognitect.transit :as t]
            [goog.Timer :as timer]
            [clojure.string :as string]))

(enable-console-print!)
(print "!!! console print enabled in filestorage")

(def fs (js/require "react-native-fs"))

(def transit-read-handlers (atom {}))

(def transit-write-handlers (atom {}))

(defn clj->json [x]
  (t/write (t/writer :json {:handlers @transit-write-handlers}) x))

(defn json->clj [x]
  (t/read (t/reader :json {:handlers @transit-read-handlers}) x))

(defprotocol IStorageBackend
  "Represents a storage resource."
  (-get [this on-exists on-not-exists])
  (-commit! [this value] "Commit value to storage at location."))

(defn readValueFromFile [file on-read]
  (.then (.readFile fs file) (fn [val] (on-read (json->clj val)))))


(deftype StorageBackend [fileName]
  IStorageBackend
  (-get [this on-exists on-not-exists]
    (.then (.exists fs fileName) (fn [ex] (if ex
                                            (readValueFromFile fileName on-exists)
                                            (on-not-exists))))
    )
  (-commit! [this value]
    (.writeFile fs fileName (clj->json value))
    ))

(defn store
  [atom backend]
  (-get backend #((print "set atom " %) (reset! atom %)) #(-commit! backend @atom) )
  (doto atom
    (add-watch ::storage-watch
               #(-commit! backend %4))))

(defn file-storage
  [atom file]
  (store atom (StorageBackend. file)))

