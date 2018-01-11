(ns status-desktop-front.ui.screens.chat.events
  (:require [status-im.utils.handlers :as handlers]
            [re-frame.core :as re-frame]
            [cljs.nodejs :as nodejs]))

(def electron (nodejs/require "electron"))
(def dialog (.-dialog (.-remote electron)))
(def shell (.-shell electron))

(re-frame/reg-fx
  :contact-dialog
  (fn [[contact-key contact-name in-contacts?]]
    (when (= 1 (.showMessageBox dialog (clj->js {:type     :question
                                                 :message  contact-name
                                                 :cancelId 0
                                                 :buttons  ["Cancel" (if in-contacts? "Open chat" "Add to Contacts and open chat")]})))
      (if in-contacts?
        (re-frame/dispatch [:open-chat-with-contact {:whisper-identity contact-key :dapp? false}])
        (re-frame/dispatch [:add-contact-handler contact-key])))))

(handlers/register-handler-fx
  :show-contact-dialog
  (fn [_ [_ contact-key contact-name in-contacts?]]
    {:contact-dialog [contact-key contact-name in-contacts?]}))

(re-frame/reg-fx
  :url-dialog
  (fn [url]
    ;;TODO for internal use we don't need this
    #_(when (= 1 (.showMessageBox dialog (clj->js {:type     :question
                                                   :checkboxLabel "Don't ask anymore"
                                                   :message  (str "Do you want to open this link '" url "' in default system browser?")
                                                   :cancelId 0
                                                   :buttons  ["Cancel" "Open"]}))))
      (.openExternal shell url)))

(handlers/register-handler-fx
  :show-link-dialog
  (fn [_ [_ url]]
    {:url-dialog url}))