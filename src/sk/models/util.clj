(ns sk.models.util
  (:require [noir.session :as session]
            [clojure.string :refer [join]]
            [sk.models.crud :refer [Query db]]))

(defn parse-int
  "Attempt to convert to integer or on error return nil or itself if it's already an integer"
  [s]
  (try
    (Integer/parseInt s)
    (catch Exception _ (if (integer? s) s nil))))

(defn get-session-id []
  (try
    (if (session/get :user_id) (session/get :user_id) 0)
    (catch Exception e (.getMessage e))))

(defn user-level []
  (let [id   (get-session-id)
        type (if (nil? id)
               nil
               (:level (first (Query db ["select level from users where id = ?" id]))))]
    type))

(defn user-email []
  (let [id    (get-session-id)
        email (if (nil? id)
                nil
                (:username (first (Query db ["select username from users where id = ?" id]))))]
    email))

(defn user-name []
  (let [id (get-session-id)
        username (if (nil? id)
                   nil
                   (:name (first (Query db ["select CONCAT(firstname,' ',lastname) as name from users where id = ?" id]))))]
    username))

(defn seconds->string [seconds]
  (let [n seconds
        day (int (/ n (* 24 3600)))
        day-desc (if (= day 1) " day " " days ")

        n (mod n (* 24 3600))
        hour (int (/ n 3600))
        hour-desc (if (= hour 1) " hour " " hours ")

        n (mod n 3600)
        minutes (int (/ n 60))
        minutes-desc (if (= minutes 1) " minute " " minutes ")

        n (mod n 60)
        seconds (int n)
        seconds-desc (if (= seconds 1) " second " " seconds ")

        minutes-desc (str day day-desc hour hour-desc minutes minutes-desc)
        seconds-desc (str day day-desc hour hour-desc minutes minutes-desc seconds seconds-desc)]
    minutes-desc))

;; Start build-turismo
(def turismo-sql
  (str
   "
    select distinct aventuras.cmt_id,
    cmt.nombre
    from aventuras
    join cmt on cmt.id = aventuras.cmt_id
    order by cmt.nombre
    "))

(defn build-turismo-link
  "Builds the menu link for each menu"
  [row]
  (list
   [:li [:a.dropdown-item {:href (str "/aventuras/" (:cmt_id row))} (:nombre row)]]))

(defn build-turismo
  "Builds cicloturismo menus"
  []
  (let [rows (Query db [turismo-sql])]
    (map build-turismo-link rows)))
;; End build-turismo

(defn capitalize-words
  "Captitalizar todas las palabras en una hilera"
  [s]
  (->> (clojure.string/split (str s) #"\b")
       (map clojure.string/capitalize)
       (join)))

(comment
  (build-turismo)
  (seconds->string 90061))
