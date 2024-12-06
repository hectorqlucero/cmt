(ns sk.handlers.admin.aventuras.model
  (:require [sk.models.crud :refer [Query db]]
            [sk.models.util :refer [user-level user-email]]))

(def get-aventuras-sql
  (str
   "
SELECT *
FROM aventuras
WHERE leader_email = ?
ORDER BY fecha desc
"))

(defn get-aventuras
  []
  (let [username (user-email)
        level (user-level)
        sql "SELECT * FROM aventuras ORDER BY fecha desc"]
    (if (= level "S")
      (Query db [sql])
      (Query db [get-aventuras-sql username]))))

(def get-aventuras-id-sql
  (str
   "
SELECT *
FROM aventuras
WHERE id = ?
"))

(defn get-aventuras-id
  [id]
  (first (Query db [get-aventuras-id-sql id])))
;; Start cmt-options
(def cmt-options-sql
  (str
   "
    select
    nombre as label,
    id as value
    from cmt
    order by nombre
    "))

(defn cmt-options
  []
  (let [rows (Query db [cmt-options-sql])]
    (list* {:value "" :label "Seleccionar Aventura"} rows)))
;; End cmt-options

(comment
  (get-aventuras)
  (cmt-options))
