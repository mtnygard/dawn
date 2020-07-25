(ns frenpl
  "The user-friendly REPL"
  (:require clansi
            [clojure.java.io :as io]
            [expound.alpha :as expound]
            [clojure.spec.alpha :as s]
            [nrepl.server :as nrepl-server]
            [cider.nrepl :refer (cider-nrepl-handler)]
            [refactor-nrepl.middleware :as refactor]
            [rebel-readline.main]))

(def styles
  {\b :bg-blue
   \g :bg-green
   \w :bg-white})

(defn colorize-line [line]
  (->> line
       (map #(clansi/style "  " (get styles % :bg-default)))
       (apply str)))

(defn -main [& args]
  (with-open [r (io/reader (io/resource "logo.dat"))]
    (run! #(println (colorize-line %)) (line-seq r)))
  (set! s/*explain-out* (expound/custom-printer {:print-specs?       false
                                                 :show-valid-values? true}))
  (spit ".nrepl-port" "7888\n")
  (nrepl-server/start-server :bind "0.0.0.0" :port 7888 :handler (refactor/wrap-refactor cider-nrepl-handler))
  (println "\n\nNREPL server started on port 7888\n\n")
  (apply rebel-readline.main/-main args))