(ns status-desktop-front.status-go
  (:require [status-desktop-front.react-native-web :as react]))

;(def ipcRenderer (.-ipcRenderer (js/require "electron")))

(def status
  (when (exists? (.-NativeModules react/react-native-web))
        (.-Status (.-NativeModules react/react-native-web))))

(defn call-web3 [payload]
      (print "!!!!!!!!! call-web3")
  ;(.sendSync ipcRenderer "CallRPC" payload)
      )

(defn start-node [config]
    (.log js/console (.startNode status config)))

(defn create-account [password callback]
      (.createAccount status password (fn [data]
                                          (callback data))))

(defn login [address password callback]
  (.log js/console (str "Login " address " " password))
  (.login status address password (fn [data]
                                      (callback data))))


(defn recover-account [passphrase password]
      (print "!!!!!!!!! recover-account")
  ;(.sendSync ipcRenderer "RecoverAccount" passphrase password)
      )

;
;(.on ipcMain "CallRPC"
;     (fn [event payload]
;         (set! (.-returnValue event) (.CallRPC status-go payload))))
;
;(.on ipcMain "StartNode"
;     (fn [event config]
;         (let [app-path (.getPath app "userData")
;               config (.GenerateConfig status-go (str app-path "/ethereum") 3 0)
;               config' (.parse js/JSON config)
;               _ (set! (.-LogLevel config') "INFO")
;               _ (set! (.-LogFile config') (str app-path "/node.log"))
;               _ (set! (.-Enabled (.-UpstreamConfig config')) true)
;               config'' (.stringify js/JSON config')
;               res (.StartNode status-go config'')]
;              (.log js/console (str "Node started at " (str app-path "/ethereum")))
;              (set! (.-returnValue event) (str "Config " config'' " Node result: " res)))))
;
;(.on ipcMain "CreateAccount"
;     (fn [event password]
;         (set! (.-returnValue event) (.CreateAccount status-go password))))
;
;(.on ipcMain "Login"
;     (fn [event address password]
;         (set! (.-returnValue event) (.Login status-go address password))))
;
;(.on ipcMain "RecoverAccount"
;     (fn [event passphrase password]
;         (set! (.-returnValue event) (.RecoverAccount status-go passphrase password))))