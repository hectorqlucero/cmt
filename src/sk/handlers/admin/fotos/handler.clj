(ns sk.handlers.admin.fotos.handler
  (:require [sk.models.crud :refer [build-form-row build-form-save build-form-delete]]
            [sk.models.grid :refer [build-grid]]
            [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.handlers.admin.fotos.view :refer [fotos-view fotos-scripts]]))

(defn fotos [_]
  (let [title "Fotos"
        ok (get-session-id)
        js (fotos-scripts)
        content (fotos-view title)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "solo <strong>los administradores </strong> pueden accessar esta opción!!!"))))

(defn fotos-grid
  "builds grid. parameters: params table & args args: {:join 'other-table' :search-extra name='pedro' :sort-extra 'name,lastname'}"
  [{params :params}]
  (let [table "fotos"
        args {:sort-extra "fecha desc"}]
    (build-grid params table args)))

(defn fotos-form [id]
  (let [table "fotos"]
    (build-form-row table id)))

(defn fotos-save [{params :params}]
  (let [table "fotos"]
    (build-form-save params table)))

(defn fotos-delete [{params :params}]
  (let [table "fotos"]
    (build-form-delete params table)))
