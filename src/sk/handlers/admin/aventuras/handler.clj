(ns sk.handlers.admin.aventuras.handler
  (:require [sk.models.crud :refer [build-form-row build-form-save build-form-delete]]
            [sk.models.grid :refer [build-grid]]
            [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id user-email user-level]]
            [sk.handlers.admin.aventuras.view :refer [aventuras-view aventuras-scripts]]))

(defn aventuras [_]
  (let [title "Aventuras"
        ok (get-session-id)
        js (aventuras-scripts)
        content (aventuras-view title (user-email))]
    (application title ok js content)))

(defn aventuras-filter []
  (if (= (user-level) "U")
    {:sort-extra "fecha desc"
     :search-extra (str "leader_email = '" (user-email) "'")}
    {:sort-extra "fecha desc"}))

(defn aventuras-grid
  "builds grid. parameters: params table & args args: {:join 'other-table' :search-extra name='pedro' :sort-extra 'name,lastname'}"
  [{params :params}]
  (let [table "aventuras"
        args (aventuras-filter)]
    (build-grid params table args)))

(defn aventuras-form [id]
  (let [table "aventuras"]
    (build-form-row table id)))

(defn aventuras-save [{params :params}]
  (let [table "aventuras"]
    (build-form-save params table)))

(defn aventuras-delete [{params :params}]
  (let [table "aventuras"]
    (build-form-delete params table)))
