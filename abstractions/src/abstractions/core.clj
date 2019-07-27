(ns abstractions.core)

(defmulti full-moon-behavior :were-type)

(defmethod full-moon-behavior :wolf [were-creature] (str (:name were-creature) " will howl and murder"))
(defmethod full-moon-behavior :simmons [were-creature] (str (:name were-creature) " will encourage people and sweat to the oldies"))
(defmethod full-moon-behavior :twilight [were-creature] (str (:name were-creature) " will play a key role in terrible young adult fiction"))

(defprotocol Were-Creature
  (full-moon-behavior [x]))

(defrecord Were-Simmons [name]
  Were-Creature
  (full-moon-behavior [x] (str "The Were-Simmons " name " will encourage people and sweat to the oldies")))

(defprotocol Emotion
  (laugh [x])
  (cry [x]))

(extend-protocol Emotion
  java.lang.String
  (laugh [x] (str x " is so funny!"))
  (cry [x] (str x " is so sad!"))

  java.lang.Long
  (laugh [x] (str x " is not funny! It's a number!"))
  (cry [x] (str x " is not sad! It's a number!")))

(defmulti attack :class)

(defmethod attack :mage [character] (str (:name character) " casts a spell to attack the enemy!"))
(defmethod attack :knight [character] (str (:name character) " slashes his sword to attack the enemy!"))
(defmethod attack :default [character] (str (:name character) " is a weakling " (:class character)  " and can't do anything to attack!"))

(defrecord RPG-Character [name class])
