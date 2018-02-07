(ns status-desktop-front.ui.screens.main.tabs.profile.views
  (:require [status-desktop-front.react-native-web :as react]
            [status-im.ui.screens.profile.styles :as styles]
            [status-desktop-front.ui.components.views :as components]
            [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [status-im.utils.utils :refer [hash-tag?]]
            [clojure.string :as string]
            [cljs.nodejs :as nodejs])
  (:require-macros [status-im.utils.views :as views]))

(def Electron (nodejs/require "electron"))
(def clipboard (.-clipboard Electron))

(defn profile-badge [{:keys [name last-online] :as contact}]
  [react/view {:style styles/profile-badge}
    [react/text {:style           styles/profile-name-text
                 :number-of-lines 1}
     name]])

(defn profile-info-item [{:keys [label value options]}]
  [react/touchable-highlight {:on-press #(.writeText clipboard value)}
   [react/view {:style styles/profile-setting-item}

    [react/view {:style (styles/profile-info-text-container options)}
     [react/text {:style styles/profile-setting-title}
      label]
     [react/view {:style styles/profile-setting-spacing}]
     [react/text {:style           styles/profile-setting-text
                  :number-of-lines 1
                  :ellipsizeMode   :head}
      value]]]])

(defn my-profile-info [{:keys [public-key status phone] :as contact}]
  [react/view
   [profile-info-item
    {:label "Contact Key (click to copy)"
     :value public-key}]])

(views/defview sound-view [sound label sound-name]
  [react/view {:style (merge styles/profile-setting-item {:height 50})}
   [react/text {:style styles/profile-setting-text} label]
   [react/view {:style {:flex 1}}]
   [components/checkbox {:on-value-change
                         #(re-frame/dispatch [:set-in [:desktop :notifications :sound] sound-name])
                         :checked?
                         (= sound sound-name)}]])

(views/defview notifications []
  (views/letsubs [notifications-enabled? [:get-in [:desktop :notifications :enabled?]]
                  sound [:get-in [:desktop :notifications :sound]]]
    [react/view {:style {:flex 1}}
     [react/view {:style styles/profile-setting-item}
      [react/text {:style styles/profile-setting-text}
       "Notifications"]
      [react/view {:style {:flex 1}}]
      [components/checkbox {:on-value-change
                            #(re-frame/dispatch [:set-in [:desktop :notifications :enabled?] %])
                            :checked?
                            notifications-enabled?}]]
     [react/scroll-view
      [react/view {:style {:flex 1}}
       [sound-view sound "No Sound" nil]
       [sound-view sound "Sound 1" :notif01]
       [sound-view sound "Sound 2" :notif02]
       [sound-view sound "Sound 3" :notif03]
       [sound-view sound "Sound 4" :notif04]
       [sound-view sound "Sound 5" :notif05]
       [sound-view sound "Sound 6" :notif06]
       [sound-view sound "Sound 7" :notif07]
       [sound-view sound "Sound 8" :notif08]
       [sound-view sound "Sound 9" :notif09]
       [sound-view sound "Sound 10" :notif10]
       [sound-view sound "Sound 11" :notif11]
       [sound-view sound "Sound 12" :notif12]
       [sound-view sound "Sound 13" :notif13]]]]))

(views/defview profile []
  (views/letsubs [{:keys [status public-key] :as current-account} [:get-current-account]]
    [react/view {:style (merge styles/profile {:background-color :white})}
     [react/view {:style styles/profile-form}
      [profile-badge current-account]]
     [react/view {:style {:height 1 :background-color "#e8ebec" :margin-horizontal 16}}]
     [react/view {:style styles/profile-info-container}
      [my-profile-info current-account]]
     [react/view {:style {:height 1 :background-color "#e8ebec" :margin-horizontal 16}}]
     [notifications]]))