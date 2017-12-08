(ns status-desktop-front.storage
  (:require [alandipert.storage-atom :refer [local-storage]]))

;;;; ACCOUNTS

;; I would love to have something similar in status-react instead realm
(def accounts (local-storage (atom []) :accounts))
(def account (atom {}))

(defn save-account [account]
  (swap! accounts conj account))

(defn get-accounts []
  @accounts)

(defn change-account [address new-account?]
  (swap! account assoc
         :contacts (local-storage (atom {}) (keyword (str address "contacts")))))


;;;; CONTACTS

(defn save-contact [contact]
  (swap! (:contacts @account) assoc (:whisper-identity contact) contact))

(defn get-all-contacts []
  (vals @(:contacts @account)))
