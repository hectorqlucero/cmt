(ns sk.handlers.admin.aventuras.model
  (:require [sk.models.crud :refer [Query db]]
            [clojure.string :as st]))

(def get-aventuras-sql
  (str
   "
SELECT *
FROM aventuras
ORDER BY fecha desc
"))

(defn get-aventuras
  []
  (Query db get-aventuras-sql))

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
  (cmt-options))
