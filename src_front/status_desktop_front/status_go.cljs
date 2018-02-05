(ns status-desktop-front.status-go
  (:require [status-desktop-front.react-native-web :as react]))

;(def ipcRenderer (.-ipcRenderer (js/require "electron")))

(def status
  (when (exists? (.-NativeModules react/react-native-web))
        (.-Status (.-NativeModules react/react-native-web))))

(defn call-web3 [payload callback]
    (.sendWeb3Request status payload (fn [data]
                                  (callback data))))

(defn start-node [config]
    (.log js/console (.startNode status config)))

(defn create-account [password callback]
      (.createAccount status password (fn [data]
                                          (callback data))))

(defn login [address password callback]
  (.log js/console (str "Login " address " " password))
  (.login status address password (fn [data]
                                      (callback data))))


(defn recover-account [passphrase password callback]
      (.recoverAccount status passphrase password (fn [data]
                                          (callback data))))