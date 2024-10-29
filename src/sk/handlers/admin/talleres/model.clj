(ns sk.handlers.admin.talleres.model
  (:require [sk.models.crud :refer [Query db]]
            [clojure.string :as st]))

(def get-talleres-sql
  (str
   "
SELECT *
FROM talleres
ORDER BY nombre
"))

(defn get-talleres
  []
  (Query db get-talleres-sql))

(def get-talleres-id-sql
  (str
   "
SELECT *
FROM talleres
WHERE id = ?
"))

(defn get-talleres-id
  [id]
  (first (Query db [get-talleres-id-sql id])))
