(ns fwpd.core)

(def filename "suspects.csv")

(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((vamp-key conversions) value))

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\r\n")))

(defn mapify
  "Return a seq of maps containing a :name and :glitter-index"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

(defn append
  [record-string records]
  (let [record (mapify (parse record-string))]
    (if (apply validate validators record)
      (into records record)
      records)))

(def validators {:name string? :glitter-index #(and (int? %) (<= 0 % 10))})

(defn validate
  [validator-map record]
  (reduce (fn [ret [k v]]
            (and ret ((k validators) v)))
          (= (set vamp-keys) (set (keys record)))
          record))

(defn records->csv
  [records]
  (clojure.string/join
   "\r\n"
   (map (partial clojure.string/join ",") (map vals records))))
