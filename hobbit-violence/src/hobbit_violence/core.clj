(ns hobbit-violence.core
  (:gen-class))

(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn symmetrize-body-parts
  "Expects a seq of maps with :name and :size"
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts
         final-parts []]
    (if (empty? remaining-asym-parts)
      final-parts
      (let [[part & remaining] remaining-asym-parts]
        (recur remaining
               (into final-parts
                     (set [part (matching-part part)])))))))

(defn symmetrize-body-parts-reduce
  "Expects a seq of maps with :name and :size"
  [asym-body-parts]
  (reduce (fn [final-parts part]
            (into final-parts (set [part (matching-part part)])))
          []
          asym-body-parts))

(defn hit
  [asym-body-parts]
  (let [sym-parts (symmetrize-body-parts-reduce asym-body-parts)
        body-part-size-sum (reduce + (map :size sym-parts))
        target (rand body-part-size-sum)]
     (loop [[part & remaining] sym-parts
           accumulated-size (:size part)]
      (if (> accumulated-size target)
        part
        (recur remaining (+ accumulated-size (:size (first remaining))))))))

(defn matching-alien-parts
  [part]
  (map #(assoc part :name (str (:name part) "-" %)) (range 1 6)))

(defn symmetrize-alien-body-parts
  [asym-alien-parts]
  (reduce (fn [final-parts part]
            (into final-parts (matching-alien-parts part)))
          []
          asym-alien-parts))

(defn matching-body-parts-general
  [part n]
  (map #(assoc part :name (str (:name part) "-" %)) (range 1 (inc n))))

(defn symmetrize-body-parts-general
  [n asym-parts]
  (reduce (fn [final-parts part]
            (into final-parts (matching-parts-general part n)))
          []
          asym-parts))

(defn -main
  [& args]
  (hit asym-hobbit-body-parts))
