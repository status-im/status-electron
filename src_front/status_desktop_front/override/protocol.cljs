(ns status-desktop-front.override.protocol
  (:require [re-frame.core :as re-frame]
            [status-desktop-front.web3-provider :as web3-provider]
            [status-im.protocol.handlers :as protocol.handlers]
            [status-im.constants :as constants]))

;;;; COFX

(re-frame/reg-cofx
  ::protocol.handlers/get-web3
  (fn [coeffects _]
    (assoc coeffects :web3 (web3-provider/make-web3))))

(re-frame/reg-cofx
  ::protocol.handlers/get-chat-groups
  (fn [coeffects _]
    (assoc coeffects :groups [])))

(re-frame/reg-cofx
  ::protocol.handlers/get-pending-messages
  (fn [coeffects _]
    (assoc coeffects :pending-messages [])))

(re-frame/reg-cofx
  ::protocol.handlers/get-all-contacts
  (fn [coeffects _]
    (assoc coeffects :contacts [])))

(re-frame/reg-cofx
  ::protocol.handlers/message-get-by-id
  (fn [coeffects _]
    (let [[{{:keys [message-id]} :payload}] (:event coeffects)]
      (assoc coeffects :message-by-id nil))));(messages/get-by-id message-id)))))

(re-frame/reg-cofx
  ::protocol.handlers/chats-new-update?
  (fn [coeffects _]
    (let [[{{:keys [group-id timestamp]} :payload}] (:event coeffects)]
      (assoc coeffects :new-update? false))));(chats/new-update? timestamp group-id)))))

(re-frame/reg-cofx
  ::protocol.handlers/chats-is-active-and-timestamp
  (fn [coeffects _]
    (let [[{{:keys [group-id timestamp]} :payload}] (:event coeffects)]
      (assoc coeffects :chats-is-active-and-timestamp
                       false))))
(re-frame/reg-cofx
  ::protocol.handlers/chats-is-active?
  (fn [coeffects _]
    (let [[{{:keys [group-id timestamp]} :payload}] (:event coeffects)]
      (assoc coeffects :chats-is-active-and-timestamp
                       false))))

(re-frame/reg-cofx
  ::protocol.handlers/has-contact?
  (fn [coeffects _]
    (let [[{{:keys [group-id identity]} :payload}] (:event coeffects)]
      (assoc coeffects :has-contact? false))));(chats/has-contact? group-id identity)))))

;;;; FX

(re-frame/reg-fx
  ::protocol.handlers/save-processed-messages
  (fn [processed-message]))
    ;(processed-messages/save processed-message)))

(defn system-message
  ([message-id timestamp content]
   {:from         "system"
    :message-id   message-id
    :timestamp    timestamp
    :content      content
    :content-type constants/text-content-type}))

(re-frame/reg-fx
  ::protocol.handlers/participant-removed-from-group-message
  (fn [{:keys [identity from message-id timestamp group-id]}]
    #_(let [remover-name (:name (contacts/get-by-id from))
            removed-name (:name (contacts/get-by-id identity))
            message (->> [(or remover-name from) (i18n/label :t/removed) (or removed-name identity)]
                         (string/join " ")
                         (system-message message-id timestamp))
            message' (assoc message :group-id group-id)]
        (re-frame/dispatch [:received-message message']))))

(re-frame/reg-fx
  ::protocol.handlers/chats-add-contact
  (fn [[group-id identity]]))
    ;(chats/add-contacts group-id [identity])))

(re-frame/reg-fx
  ::protocol.handlers/chats-remove-contact
  (fn [[group-id identity]]))
    ;(chats/remove-contacts group-id [identity])))

#_(defn you-removed-from-group-message
    [from {:keys [message-id timestamp]}]
    (let [remover-name (:name (contacts/get-by-id from))]
      (->> [(or remover-name from) (i18n/label :t/removed-from-chat)]
           (string/join " ")
           (system-message message-id timestamp))))

(re-frame/reg-fx
  ::protocol.handlers/you-removed-from-group-message
  (fn [{:keys [from message-id timestamp group-id]}]
    #_(let [remover-name (:name (contacts/get-by-id from))
            message  (->> [(or remover-name from) (i18n/label :t/removed-from-chat)]
                          (string/join " ")
                          (system-message message-id timestamp))
            message' (assoc message :group-id group-id)]
        (re-frame/dispatch [:received-message message']))))

(re-frame/reg-fx
  ::protocol.handlers/participant-left-group-message
  (fn [{:keys [chat-id from message-id timestamp]}]
    #_(let [left-name (:name (contacts/get-by-id from))]
        (->> (str (or left-name from) " " (i18n/label :t/left))
             (system-message message-id timestamp)
             (messages/save chat-id)))))

(re-frame/reg-fx
  ::protocol.handlers/participant-invited-to-group-message
  (fn [{:keys [chat-id current-identity identity from message-id timestamp]}]
    #_(let [inviter-name (:name (contacts/get-by-id from))]
          invitee-name (if (= identity current-identity)
                         (i18n/label :t/You)
                         (:name (contacts/get-by-id identity)))
        (re-frame/dispatch
          [:received-message
           {:from         "system"
            :group-id     chat-id
            :timestamp    timestamp
            :message-id   message-id
            :content      (str (or inviter-name from) " " (i18n/label :t/invited) " " (or invitee-name identity))
            :content-type constants/text-content-type}]))))

(re-frame/reg-fx
  ::protocol.handlers/save-message-status!
  (fn [{:keys [message-id ack-of-message group-id from status]}]
    #_(let [message-id' (or ack-of-message message-id)]
        (when-let [{:keys [message-status] :as message} (messages/get-by-id message-id')]
          (when-not (= (keyword message-status) :seen)
            (let [group?   (boolean group-id)
                  message' (-> (if (and group? (not= status :sent))
                                 (update-in message
                                            [:user-statuses from]
                                            (fn [{old-status :status}]
                                              {:id               (random/id)
                                               :whisper-identity from
                                               :status           (if (= (keyword old-status) :seen)
                                                                   old-status
                                                                   status)}))
                                 (assoc message :message-status status))
                               ;; we need to dissoc preview because it has been saved before
                               (dissoc :preview))]
              (messages/update-message message')))))))

(re-frame/reg-fx
  ::protocol.handlers/pending-messages-delete
  (fn [message]))
    ;(pending-messages/delete message)))

(re-frame/reg-fx
  ::protocol.handlers/pending-messages-save
  (fn [{:keys [type id pending-message]}]
    ;(pending-messages/save pending-message)
    #_(when (#{:message :group-message} type)
        (messages/update-message {:message-id id}
                  :delivery-status    :pending))))

(re-frame/reg-fx
  ::protocol.handlers/status-init-jail
  (fn []))
    ;(status/init-jail)))

(re-frame/reg-fx
  ::protocol.handlers/load-processed-messages!
  (fn []
    #_(let [now      (datetime/now-ms)]
          messages (processed-messages/get-filtered (str "ttl > " now))
        (cache/init! messages)
        (processed-messages/delete (str "ttl <=" now)))))