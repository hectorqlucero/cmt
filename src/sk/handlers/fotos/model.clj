(ns sk.handlers.fotos.model
  (:require [sk.models.crud :refer [Query db]]
            [clojure.string :as st]))

(def get-fotos-sql
  (str
   "
SELECT
DATE_FORMAT(fecha, '%W ') as dia,
DATE_FORMAT(fecha, '%e de %M %Y') as f_fecha,
enlace
FROM fotos
ORDER BY fecha desc
"))

(defn get-fotos
  []
  (Query db get-fotos-sql))

(def get-fotos-id-sql
  (str
   "
SELECT *
FROM fotos
WHERE id = ?
"))

(defn get-fotos-id
  [id]
  (first (Query db [get-fotos-id-sql id])))
