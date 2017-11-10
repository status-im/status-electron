(ns status-desktop-front.utils
  (:require [clojure.string :as string]
            [hickory.core :as hickory]))

(def svg-tags #{:g :rect :path :use :defs})

(defmacro slurp-web-svg [file]
  (let [svg (-> (clojure.core/slurp file)
                (string/replace #"[\n]\s*" ""))
        svg-hiccup (first (map hickory/as-hiccup (hickory/parse-fragment svg)))
        color (gensym "args")]
    `(fn [~color]
       ~(into []
          (clojure.walk/prewalk
            (fn [node]
              (if (svg-tags node)
                node
                (if (vector? node)
                  (let [[k v] node]
                    (if (and (= :fill k) v)
                      [k color]
                      node))
                  node)))
            (rest (rest svg-hiccup)))))))