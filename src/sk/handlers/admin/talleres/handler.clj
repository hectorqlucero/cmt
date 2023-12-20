(ns sk.handlers.admin.talleres.handler
  (:require [sk.models.crud :refer [build-form-row build-form-save build-form-delete]]
            [sk.models.grid :refer [build-grid]]
            [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.handlers.admin.talleres.view :refer [talleres-view talleres-scripts]]))

(defn talleres [_]
  (let [title "Talleres"
        ok (get-session-id)
        js (talleres-scripts)
        content (talleres-view title)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "solo <strong>los administradores </strong> pueden accessar esta opción!!!"))))

(defn talleres-grid
  "builds grid. parameters: params table & args args: {:join 'other-table' :search-extra name='pedro' :sort-extra 'name,lastname'}"
  [{params :params}]
  (let [table "talleres"
        args {:sort-extra "nombre"}]
    (build-grid params table args)))

(defn talleres-form [id]
  (let [table "talleres"]
    (build-form-row table id)))

(defn talleres-save [{params :params}]
  (let [table "talleres"]
    (build-form-save params table)))

(defn talleres-delete [{params :params}]
  (let [table "talleres"]
    (build-form-delete params table)))
