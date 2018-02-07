(ns status-desktop-front.status-go)

(def ipcRenderer (.-ipcRenderer (js/require "electron")))

(defn call-web3 [payload]
  (.sendSync ipcRenderer "CallRPC" payload))

(defn start-node [config]
  (.log js/console (.sendSync ipcRenderer "StartNode" config)))

(defn create-account [password]
  (.log js/console (str "CreateAccount " password))
  (let [res (.sendSync ipcRenderer "CreateAccount" password)]
    (.log js/console (str "CreateAccount result " res))
    res))

(defn login [address password]
  (.log js/console (str "Login " address " " password))
  (let [res (.sendSync ipcRenderer "Login" address password)]
    (.log js/console (str "Login result " res))
    res))

(defn recover-account [password passphrase]
  (.sendSync ipcRenderer "RecoverAccount" password passphrase))

(defn add-peer [enode]
  (.sendSync ipcRenderer "AddPeer" enode))