(ns status-desktop-front.ui.components.views
  (:require
    [status-desktop-front.react-native-web :as react]
    [status-desktop-front.ui.components.icons :as icons]
    [status-im.ui.components.common.styles :as styles]
    [status-im.ui.components.action-button.styles :as st]
    [status-im.ui.components.styles :as common]))

;; TODO copy-pate with minimum modifications of status-react components

(defn action-button [{:keys [label icon icon-opts on-press label-style cyrcle-color]}]
  [react/touchable-highlight {:on-press on-press}
   [react/view {:style st/action-button}
    [react/view {:style (st/action-button-icon-container cyrcle-color)}
     [icons/icon icon icon-opts]]
    [react/view {:style st/action-button-label-container}
     [react/text {:style (merge st/action-button-label label-style)}
      label]]]])

(defn separator [style & [wrapper-style]]
  [react/view {:style (merge styles/separator-wrapper wrapper-style)}
   [react/view {:style (merge styles/separator style)}]])

(def sticky-button-style
  {:flex-direction   :row
   :height           52
   :justify-content  :center
   :align-items      :center
   :background-color common/color-light-blue})

(def sticky-button-label-style
  {:color   common/color-white
   :font-size      17
   :line-height    20
   :letter-spacing -0.2})

(defn sticky-button [label on-press]
  [react/touchable-highlight {:on-press on-press}
   [react/view {:style sticky-button-style}
    [react/text {:style sticky-button-label-style}
     label]]])