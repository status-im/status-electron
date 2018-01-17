(ns status-desktop-front.storage
  (:require                                                 ;[alandipert.storage-atom :refer [local-storage]]
            [status-im.data-store.messages :as data-store.messages]
            [status-im.utils.random :as random]
            [status-im.constants :as constants]))
;;;; ACCOUNTS

;; I would love to have something similar in status-react instead realm
;(def accounts (local-storage (atom []) :accounts))
(def accounts (atom {}))
(def account (atom {}))

(defn save-account [account]
  (swap! accounts conj account))

(defn get-accounts []
  @accounts)

(defn change-account [address new-account?]
  ;(swap! account assoc
  ;       :contacts (local-storage (atom {}) (keyword (str address "contacts")))
  ;       :chats (local-storage (atom {}) (keyword (str address "chats")))
  ;       :messages (local-storage (atom {}) (keyword (str address "messages"))))
      )


;;;; CONTACTS

(defn save-contact [contact]
  (swap! (:contacts @account) update-in [(:whisper-identity contact)] merge contact))

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

(defn chat-exists? [chat-id]
  (boolean (get-chat-by-id chat-id)))

(defn chat-is-active? [chat-id]
  (get (get-chat-by-id chat-id) :is-active))

(defn chat-new-update? [timestamp chat-id]
  (let
    [{:keys [added-to-at removed-at removed-from-at added-at]}
     (get-chat-by-id chat-id)]
    (and (> timestamp added-to-at)
         (> timestamp removed-at)
         (> timestamp removed-from-at)
         (> timestamp added-at))))

(defn- groups [active?]
  (filter #(and (:group-chat %) (= (:is-active %) active?)) (get-all-chats)))

(defn get-active-group-chats []
  (map (fn [{:keys [chat-id public-key private-key public?]}]
         (let [group {:group-id chat-id
                      :public?  public?}]
           (if (and public-key private-key)
             (assoc group :keypair {:private private-key
                                    :public  public-key})
             group)))
       (groups true)))

;;;; MESSAGE

(defn save-message [{:keys [message-id content] :as message}]
  (let [content' (data-store.messages/prepare-content content)
        message' (merge data-store.messages/default-values
                        message
                        {:content   content'
                         :timestamp (random/timestamp)})]
    (swap! (:messages @account) assoc message-id message')))

(defn update-message [{:keys [message-id content] :as message}]
  (swap! (:messages @account) update-in [message-id] merge message))

(defn get-message-by-id [message-id]
  (get @(:messages @account) message-id))

(defn get-messages-by-chat-id
  ([chat-id]
   (get-messages-by-chat-id chat-id 0))
  ([chat-id from]
   (let [chats (sort-by :timestamp > (filter #(= chat-id (:chat-id %)) (vals @(:messages @account))))
         to (+ from constants/default-number-of-messages)]
     (if (< to (count chats))
       ;;TODO yeah i know i know
       (subvec (into [] chats)
               from
               to)
       chats))))

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








