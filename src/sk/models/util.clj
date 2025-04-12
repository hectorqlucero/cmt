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
    where cmt.flag = '1'
    and cmt.maximo = '0'
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

;; Start build-mturismo
(def mturismo-sql
  (str
   "
    select distinct aventuras.cmt_id,
    cmt.nombre
    from aventuras
    join cmt on cmt.id = aventuras.cmt_id
    where cmt.flag = '0'
    order by cmt.nombre
    "))

(defn build-mturismo-link
  "Builds the menu link for each menu"
  [row]
  (list
   [:li [:a.dropdown-item {:href (str "/aventuras/" (:cmt_id row))} (:nombre row)]]))

(defn build-mturismo
  "Builds cicloturismo menus"
  []
  (let [rows (Query db [mturismo-sql])]
    (map build-mturismo-link rows)))
;; End build-turismo

;; Start build-ciudad
(def rodadas-sql
  "
   select distinct aventuras.cmt_id,
   cmt.nombre
   from aventuras
   join cmt on cmt.id = aventuras.cmt_id
   where cmt.maximo >= '30'
   order by cmt.nombre
   ")

(defn build-rodadas-link
  "Builds the menu link for each menu"
  [row]
  (list
   [:li [:a.dropdown-item {:href (str "/aventuras/" (:cmt_id row))} (:nombre row)]]))

(defn build-ciudad
  "Builds bicycle rides on the city"
  []
  (let [rows (Query db [rodadas-sql])]
    (map build-rodadas-link rows)))
;; End build-ciudad

(defn capitalize-words
  "Captitalizar todas las palabras en una hilera"
  [s]
  (->> (clojure.string/split (str s) #"\b")
       (map clojure.string/capitalize)
       (join)))

(comment
  (Query db [rodadas-sql])
  (build-ciudad)
  (build-turismo)
  (seconds->string 90061))