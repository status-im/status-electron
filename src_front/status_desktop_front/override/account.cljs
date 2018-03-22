(ns status-desktop-front.override.account
  (:require [re-frame.core :as re-frame]
            [status-desktop-front.status-go :as status-go]
            [status-desktop-front.storage :as storage]
            [status-im.ui.screens.accounts.events :as accounts.events]
            [status-im.ui.screens.accounts.recover.events :as recover.events]
            [status-im.utils.types :refer [json->clj]]
            [clojure.string :as string]))

;;;; COFX

(re-frame/reg-cofx
  ::accounts.events/get-all-accounts
  (fn [coeffects _]
    (assoc coeffects :all-accounts (storage/get-accounts))))

;;;; FX

(re-frame/reg-fx
  ::accounts.events/save-account
  (fn [account]
    (storage/save-account account)))

(re-frame/reg-fx
  ::accounts.events/create-account
  (fn [password]
      (status-go/create-account password (fn [data]
                                             (re-frame/dispatch [::accounts.events/account-created (json->clj data) password])))))


;; RECOVER FX

(re-frame/reg-fx
  ::recover.events/recover-account-fx
  (fn [[passphrase password]]
      (status-go/recover-account (string/trim passphrase) password (fn [data]
                                             (re-frame/dispatch [:account-recovered data])))))