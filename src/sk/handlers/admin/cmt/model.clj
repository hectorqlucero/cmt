(ns sk.handlers.admin.cmt.model
  (:require [sk.models.crud :refer [Query db]]
            [clojure.string :as st]))

(def get-cmt-sql
  (str
   "
SELECT *
FROM cmt
"))

(defn get-cmt
  []
  (Query db get-cmt-sql))

(def get-cmt-id-sql
  (str
   "
SELECT *
FROM cmt
WHERE id = ?
"))

(defn get-cmt-id
  [id]
  (first (Query db [get-cmt-id-sql id])))
