(ns com.cognitect.vase.main
  (:require [com.cognitect.vase.try :as try :refer [try->]]
            [fern.easy :as fe]
            [com.cognitect.vase.fern :as fern]
            [com.cognitect.vase.api :as a])
  (:gen-class))

(def vase-fern-url "https://github.com/cognitect-labs/vase/blob/master/docs/vase_and_fern.md")

(def usage
  (str
   "Usage: vase _filename_\n\nVase takes exactly one filename, which must be in Fern format.\nSee "
   vase-fern-url
   " for details."))

(defn- parse-args
  [[filename & stuff]]
  (if (or (not filename) (not (empty? stuff)))
    (throw (ex-info usage {:filename filename}))
    {:filename filename}))

(defn -main
  [& args]
  (let [file (try-> args
               parse-args
               (:! clojure.lang.ExceptionInfo ei (fe/print-other-exception ei))
               :filename)]
    (when (and file (not= ::try/exit file))
      (fern/server file))))
