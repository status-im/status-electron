(ns status-desktop-front.storage
  (:require [alandipert.storage-atom :refer [local-storage]]
            [status-im.data-store.messages :as data-store.messages]
            [status-im.utils.random :as random]))
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
         :contacts (local-storage (atom {}) (keyword (str address "contacts")))
         :chats (local-storage (atom {}) (keyword (str address "chats")))
         :messages (local-storage (atom {}) (keyword (str address "messages")))))


;;;; CONTACTS

(defn save-contact [contact]
  (swap! (:contacts @account) assoc (:whisper-identity contact) contact))

(defn save-contacts [contacts]
  (mapv save-contact contacts))

(defn get-all-contacts []
  (vals @(:contacts @account)))


;;;; CHAT

(defn save-chat [chat]
  (swap! (:chats @account) assoc (:chat-id chat) chat))

(defn get-all-chats []
  (vals @(:chats @account)))

(defn get-chat-by-id [chat-id]
  (get @(:chats @account) chat-id))

;;;; MESSAGE

(defn save-message [{:keys [message-id content] :as message}]
  (let [content' (data-store.messages/prepare-content content)
        message' (merge data-store.messages/default-values
                        message
                        {:content   content'
                         :timestamp (random/timestamp)})]
    (swap! (:messages @account) assoc message-id message')))

(defn get-message-by-id [message-id]
  (get @(:messages @account) message-id))

(defn get-messages-by-chat-id [chat-id]
  (filter #(= chat-id (:chat-id %)) (vals @(:messages @account))))

(defn get-last-message [chat-id]
  (->> (vals @(:messages @account))
       (filter #(= chat-id (:chat-id %)))
       (sort-by :clock-value >)
       (first)))

(defn message-exists? [message-id]
  (get @(:messages @account) message-id))

(defn get-last-clock-value
  [chat-id]
  (if-let [message (get-last-message chat-id)]
    (:clock-value message)
    0))








