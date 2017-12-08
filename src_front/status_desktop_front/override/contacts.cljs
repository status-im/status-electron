(ns status-desktop-front.override.contacts
  (:require [re-frame.core :as re-frame]
            [status-im.utils.js-resources :as js-res]
            [status-im.ui.screens.contacts.events :as contacts.events]
            [status-desktop-front.storage :as storage]))
;;;; COFX

(re-frame/reg-cofx
  ::contacts.events/get-all-contacts
  (fn [coeffects _]
    (assoc coeffects :all-contacts (storage/get-all-contacts))))

(re-frame/reg-cofx
  ::contacts.events/get-default-contacts-and-groups
  (fn [coeffects _]
    (assoc coeffects
      :default-contacts js-res/default-contacts
      :default-groups js-res/default-contact-groups)))

;;;; FX

(re-frame/reg-fx
  ::contacts.events/save-contact
  (fn [contact]
    (storage/save-contact contact)))

(re-frame/reg-fx
  ::contacts.events/save-contacts!
  (fn [new-contacts]))
    ;(contacts/save-all new-contacts)))

(re-frame/reg-fx
  ::contacts.events/delete-contact
  (fn [contact]))
    ;(contacts/delete contact)))

(re-frame/reg-fx
  ::contacts.events/fetch-contacts-from-phone!
  (fn [on-contacts-event-creator]))

(re-frame/reg-fx
  ::contacts.events/request-stored-contacts
  (fn [{:keys [contacts on-contacts-event-creator]}]))

(re-frame/reg-fx
  ::contacts.events/request-contact-by-address
  (fn [id]))