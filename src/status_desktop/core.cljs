(ns status-desktop.core
    (:require [cljs.nodejs :as nodejs]))

(def electron  (nodejs/require "electron"))
(def path      (nodejs/require "path"))
(def status-go (nodejs/require "status-nodejs"))

(def app           (.-app electron))
(def BrowserWindow (.-BrowserWindow electron))
(def ipcMain       (.-ipcMain electron))
(def Menu          (.-Menu electron))

(def *win* (atom nil))

(defn -main []
  ;; error listener
  (.on nodejs/process "error"
       (fn [err] (.log js/console err)))

  ;; window all closed listener
  (.on app "window-all-closed"
       (fn [] (if (not= (.-platform nodejs/process) "darwin")
                (.quit app))))

  ;; ready listener
  (.on app "ready"
       (fn []
         (when (= (.-platform nodejs/process) "darwin")
           (.setApplicationMenu Menu
             (.buildFromTemplate Menu
               (clj->js [{:label "Edit"
                          :submenu [{ :role "cut"}
                                    { :role "copy"}
                                    { :role "paste"}
                                    { :role "pasteandmatchstyle"}
                                    { :role "delete"}
                                    { :role "selectall"}
                                    { :role "quit"}]}]))))

         (reset! *win* (BrowserWindow. (clj->js {:width 1400 :height 1000 :icon (.resolve path (js* "__dirname") "../status.icns")})))

         ;; when no optimize comment out
         (.loadURL @*win* (str "file://" (.resolve path (js* "__dirname") "../index.html")))
         ;; when no optimize uncomment
         ;; (.loadURL @*win* (str "file://" (.resolve path (js* "__dirname") "../../../index.html")))

         (.on @*win* "closed" (fn [] (reset! *win* nil))))))

(nodejs/enable-util-print!)

(set! *main-cli-fn* -main)

;; STATUS-GO

;; TODO in electron there are two processes, main - this one, it runs on nodejs and second one - render it runs on chromium,
;; TODO for some reason requiring status-nodejs in render process crash the process, but in simple example it works fine,
;; TODO so i decided to do not waste time and use it here with ipc

(.on ipcMain "CallRPC"
     (fn [event payload]
       (set! (.-returnValue event) (.CallRPC status-go payload))))

(.on ipcMain "StartNode"
     (fn start-node [event config]
       (let [config (.GenerateConfig status-go (.resolve path (js* "__dirname") "../ethereum") 3 0)
             config' (.parse js/JSON config)
             _ (set! (.-LogLevel config') "INFO")
             _ (set! (.-LogFile config') (.resolve path (js* "__dirname") "../node.log"))
             _ (set! (.-Enabled (.-UpstreamConfig config')) true)
             config'' (.stringify js/JSON config')
             res (.StartNode status-go config'')]
         (.log js/console (str "Node started at " (.resolve path (js* "__dirname") "../ethereum")))
         (set! (.-returnValue event) (str "Config " config'' " Node result: " res)))))

(.on ipcMain "CreateAccount"
     (fn create-account [event password]
       (set! (.-returnValue event) (.CreateAccount status-go password))))

(.on ipcMain "Login"
     (fn login [event address password]
       (set! (.-returnValue event) (.Login status-go address password))))