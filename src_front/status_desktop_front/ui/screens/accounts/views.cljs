(ns status-desktop-front.ui.screens.accounts.views
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require [status-im.ui.screens.accounts.styles :as st]
            [status-desktop-front.ui.screens.accounts.styles :as styles]
            [status-desktop-front.react-native-web :as react]
            [status-desktop-front.ui.components.views :as components]
            [status-desktop-front.ui.components.icons :as icons]
            status-desktop-front.ui.screens.events
            [re-frame.core :as re-frame]
            [clojure.string :as string]))

(defn account-badge [address photo-path name]
  [react/view {:style st/account-badge}
   [react/image {:source {:uri (if (string/blank? photo-path) :avatar photo-path)}
                 :style  st/photo-image}]
   [react/view {:style st/account-badge-text-view}
    [react/text {:style st/account-badge-text}
     (or name address)]]])

(defn account-view [{:keys [address photo-path name] :as account}]
  [react/view {:style {:margin-top 10}}
   [react/touchable-highlight {:on-press #(re-frame/dispatch [:open-login address photo-path name])}
    [react/view {:style styles/account-item-view}
     [account-badge address photo-path name]]]])

(defview accounts []
  (letsubs [accounts [:get-accounts]]
    [react/view {:style (merge st/accounts-container {:align-items :center})}
     [react/view {:style {:flex 1 :width 290 :align-items :center :margin-top 160}}
      [react/image {:source (js/require "./resources/images/logo.png")
                    :style  {:width 192 :height 61}}]
      [react/text {:style styles/account-title-text}
       "Sign in to your account"];(i18n/label :t/sign-in-to-status)]]
      [react/view {:style {:flex 1 :margin-bottom 32 :margin-top 22}} ;:background-color "#eef2f5"}}
       [react/scroll-view
        [react/view
         (for [[index account] (map-indexed vector (vals accounts))]
           ^{:key index} [account-view account])]]]
      [react/view {:style {:flex-direction :row :margin-bottom 32 :width 290}}
       [components/text-button "Create new account" #(re-frame/dispatch [:navigate-to :create-account])]
       [react/view {:style {:flex 1}}]
       [components/text-button "Recover access" #(re-frame/dispatch [:navigate-to :recover])]]]]))

(defview create-account []
  (letsubs [accounts [:get-accounts]
            profile-name [:get-in [:accounts/create :name]]
            password [:get-in [:accounts/create :password]]
            password-confirm [:get-in [:accounts/create :password-confirm]]]
    [react/view {:style st/accounts-container}
     (when (> (count accounts) 0)
       [components/back-button #(re-frame/dispatch [:navigate-back])])
     [react/view {:style {:flex 1 :align-items :center :justify-content :center}}
      [react/view {:style {:width 290 :align-items :center}}
       [react/image {:source (js/require "./resources/images/logo.png")
                     :style  {:width 192 :height 61 :resize-mode "stretch"}}]
       [react/view {:style st/account-title-conatiner}
        [react/text {:style styles/account-title-text}
         "To create an account set up \na password"]];(i18n/label :t/sign-in-to-status)]]
       [react/view
        [react/view {:style {:height 52 :width 290 :background-color :white
                             :opacity 0.2 :border-radius 8 :margin-top 22 :justify-content :center}}
         [react/text-input {:value       (or profile-name "")
                            :auto-focus  true
                            :style {:padding-horizontal 17 :flex 1}
                            :placeholder "Name"
                            :on-change   (fn [e]
                                           (let [native-event (.-nativeEvent e)
                                                 text (.-text native-event)]
                                             (re-frame/dispatch [:set-in [:accounts/create :name] text])))}]]
        [react/view {:style {:height 52 :width 290 :background-color :white
                             :opacity 0.2 :border-radius 8 :margin-top 22 :justify-content :center}}
         [react/text-input {:value       (or password "")
                            :style {:padding-horizontal 17 :flex 1 }
                            :placeholder "Password"
                            :secure-text-entry true
                            :on-change   (fn [e]
                                           (let [native-event (.-nativeEvent e)
                                                 text (.-text native-event)]
                                             (re-frame/dispatch [:set-in [:accounts/create :password] text])))}]]
        [react/view {:style {:height 52 :width 290 :background-color :white
                             :opacity 0.2 :border-radius 8 :margin-top 8 :justify-content :center}}
         [react/text-input {:value       (or password-confirm "")
                            :placeholder "Repeat your password"
                            :style {:padding-horizontal 17 :flex 1}
                            :on-key-press (fn [e]
                                            (let [native-event (.-nativeEvent e)
                                                  key (.-key native-event)]
                                              (when (= key "Enter")
                                                (re-frame/dispatch [:create-desktop-account password]))))
                            :secure-text-entry true
                            :on-change   (fn [e]
                                           (let [native-event (.-nativeEvent e)
                                                 text (.-text native-event)]
                                             (re-frame/dispatch [:set-in [:accounts/create :password-confirm] text])))}]]
        [react/view {:style {:margin-top 15}}
         [components/button
          "Create account"
          (and (not (string/blank? profile-name)) (> (count password) 6) (= password password-confirm))
          #(re-frame/dispatch [:create-desktop-account password])]]]]]
     [react/view {:style {:align-items :center :margin-bottom 32}}
      (when-not (> (count accounts) 0)
        [components/text-button "Recover access" #(re-frame/dispatch [:navigate-to :recover])])]]))