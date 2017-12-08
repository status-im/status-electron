(ns status-desktop-front.override.events
  (:require [status-im.utils.handlers :as handlers]
            [re-frame.core :as re-frame]
            [status-desktop-front.status-go :as status-go]
            [status-im.ui.screens.events :as events]
            [status-im.commands.handlers.debug :as debug]))

;;;; FX

(re-frame/reg-fx
  :initialize-geth-fx
  (fn [config]
    (status-go/start-node config)))

(re-frame/reg-fx ::events/init-store #())
(re-frame/reg-fx ::events/initialize-crypt-fx #())
(re-frame/reg-fx ::events/status-module-initialized-fx #())
(re-frame/reg-fx ::events/request-permissions-fx #())
(re-frame/reg-fx ::events/testfairy-alert #())
(re-frame/reg-fx ::events/get-fcm-token-fx #())

(re-frame/reg-fx :call-jail #())
(re-frame/reg-fx :call-jail-function #())
(re-frame/reg-fx :call-jail-function-n #())
(re-frame/reg-fx :http-post #())
(re-frame/reg-fx :http-get #())
(re-frame/reg-fx :http-get-n #())
(re-frame/reg-fx :show-error #())
(re-frame/reg-fx :show-confirmation #())
(re-frame/reg-fx :close-application #())

;;;; Handlers

(handlers/register-handler-fx
  :initialize-account
  (fn [_ [_ address events-after]]
    {:dispatch-n (cond-> [[:initialize-account-db address]
                          ;[:load-processed-messages]
                          [:initialize-protocol address]
                         ;[:initialize-sync-listener]
                         ;[:initialize-chats]
                          [:load-contacts]]
                         ;[:load-contact-groups]
                         ;[:init-discoveries]
                         ;[:initialize-debugging {:address address}]
                         ;[:send-account-update-if-needed]
                         ;[:start-requesting-discoveries]
                         ;[:remove-old-discoveries!]
                         ;[:update-wallet]
                         ;[:update-transactions]
                         ; [:get-fcm-token]]
                         (seq events-after)
                         (into events-after))}))

(handlers/register-handler-fx
  :check-console-chat
  (fn [{{:accounts/keys [accounts] :as db} :db} [_ open-console?]]
    (let [view (if (empty? accounts)
                 :create-account
                 :accounts)]
      {:db (assoc db
             :view-id view
             :navigation-stack (list view))})))


;; DEBUG

(re-frame/reg-fx ::debug/stop-debugging-fx #())

;; NETWORK


(handlers/register-handler-db
  :listen-to-network-status!
  (fn [db _]
    db))


;; DISCOVERY

(handlers/register-handler-fx :discoveries-send-portions #())
