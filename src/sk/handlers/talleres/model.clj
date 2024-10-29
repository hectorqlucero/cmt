(ns sk.handlers.talleres.model
  (:require [sk.models.crud :refer [Query db]]))

(def get-talleres-sql
  (str
   "
    select *
    from talleres
    order by nombre
    "))

(defn get-talleres
  []
  (Query db [get-talleres-sql]))
