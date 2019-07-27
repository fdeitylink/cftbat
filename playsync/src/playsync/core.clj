(ns playsync.core
  (:require [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan buffer close! thread alts! alts!! timeout]]))

(defn hot-dog-machine
  [hot-dog-count]
  (let [in (chan)
        out (chan)]
    (go (loop [count hot-dog-count]
          (if (> count 0)
            (let [input (<! in)]
              (if (= 3 input)
                (do
                  (>! out "hot dog")
                  (recur (dec count)))
                (do
                  (>! out "wilted lettuce")
                  (recur count))))
            (do
              (close! in)
              (close! out)))))
    [in out]))
