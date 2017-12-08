(ns status-desktop-front.ui.screens.events
  (:require [re-frame.core :as re-frame]
            [status-im.utils.handlers :as handlers]
            [status-im.ui.screens.accounts.events :as accounts.events]
            status-desktop-front.override.events
            status-desktop-front.override.chat
            status-desktop-front.override.protocol
            status-desktop-front.override.login
            status-desktop-front.override.account
            status-desktop-front.override.contacts
            status-desktop-front.override.recieve-message))

(handlers/register-handler-fx
  :create-desktop-account
  (fn [{db :db} [_ password]]
    {:db                             (assoc db :accounts/creating-account? true)
     ::accounts.events/create-account password}))

(handlers/register-handler-db
  :log-message
  (fn [db [_ message]]
     (update db :logs #(str % "\n" message))))