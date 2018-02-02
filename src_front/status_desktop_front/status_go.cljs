(ns status-desktop-front.status-go
  (:require [status-desktop-front.react-native-web :as react]))

;(def ipcRenderer (.-ipcRenderer (js/require "electron")))

(def status
  (when (exists? (.-NativeModules react/react-native-web))
        (.-Status (.-NativeModules react/react-native-web))))

(defn call-web3 [payload]
      (print "commented: call-web3")
  ;(.sendSync ipcRenderer "CallRPC" payload)
      )

(defn start-node [config]
      (print "commented: start-node")
  ;(.log js/console (.sendSync ipcRenderer "StartNode" config))
      )

(defn create-account [password]
  (let [res (.createAccount status password (fn [data] (print data)))]
    (.log js/console (str "CreateAccount result " res))
    res)
      )

(defn login [address password]
  (.log js/console (str "Login " address " " password))
  (print "commented: login")
  (let [res (.sendSync ipcRenderer "Login" address password)]
    (.log js/console (str "Login result " res))
    res)
      )

(defn recover-account [passphrase password]
      (print "commented: recover-account")
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