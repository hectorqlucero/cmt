(ns sk.handlers.admin.cmt.handler
  (:require [sk.models.crud :refer [build-form-row build-form-save build-form-delete]]
            [sk.models.grid :refer [build-grid]]
            [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.handlers.admin.cmt.view :refer [cmt-view cmt-scripts]]))

(defn cmt [_]
  (let [title "Cmt"
        ok (get-session-id)
        js (cmt-scripts)
        content (cmt-view title)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "solo <strong>los administradores </strong> pueden accessar esta opción!!!"))))

(defn cmt-grid
  "builds grid. parameters: params table & args args: {:join 'other-table' :search-extra name='pedro' :sort-extra 'name,lastname'}"
  [{params :params}]
  (let [table "cmt"
        args {:sort-extra "id"}]
    (build-grid params table args)))

(defn cmt-form [id]
  (let [table "cmt"]
    (build-form-row table id)))

(defn cmt-save [{params :params}]
  (let [table "cmt"]
    (build-form-save params table)))

(defn cmt-delete [{params :params}]
  (let [table "cmt"]
    (build-form-delete params table)))
