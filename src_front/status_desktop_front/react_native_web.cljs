(ns status-desktop-front.react-native-web
  (:require [reagent.core :as reagent]))

(def react-native-web (js/require "react-native-web"))

(defn get-react-property [name]
  (aget react-native-web name))

(defn adapt-class [class]
  (when class
    (reagent/adapt-react-class class)))

(defn get-class [name]
  (adapt-class (get-react-property name)))

(def view (get-class "View"))
(def text (get-class "Text"))
(def touchable-highlight (get-class "TouchableOpacity"))
(def scroll-view (get-class "ScrollView"))
(def text-input (get-class "TextInput"))