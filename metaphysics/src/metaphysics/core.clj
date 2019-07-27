(ns metaphysics.core)

(defn quotes-word-count
  [n-quotes]
  (let [counts-atom (atom {})
        futures (doall (repeatedly n-quotes
                                   (fn [] (future
                                            (let [quote (slurp "https://braveclojure.com/random-quote")]
                                              (swap! counts-atom (partial merge-with +)
                                                     (into {} (map #(vector % 1) (clojure.string/split quote #" ")))))))))]
    (while (some #(not (future-done? %)) futures)
      (Thread/sleep 500))
    @counts-atom))

(def char-1 (ref {:name "One" :hp 15 :inventory #{}}))

(def char-2 (ref {:name "Two" :hp 40 :inventory #{:healing-potion}}))

(dosync
 (when (some #{:healing-potion} (:inventory @char-2))
   (commute char-1 assoc :hp 40)
   (commute char-2 update :inventory disj :healing-potion)))
