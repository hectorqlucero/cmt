(ns sk.handlers.admin.videos.handler
  (:require [sk.models.crud :refer [build-form-row build-form-save build-form-delete]]
            [sk.models.grid :refer [build-grid]]
            [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.handlers.admin.videos.view :refer [videos-view videos-scripts]]))

(defn videos [_]
  (let [title "Videos"
        ok (get-session-id)
        js (videos-scripts)
        content (videos-view title)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "solo <strong>los administradores </strong> pueden accessar esta opción!!!"))))

(defn videos-grid
  "builds grid. parameters: params table & args args: {:join 'other-table' :search-extra name='pedro' :sort-extra 'name,lastname'}"
  [{params :params}]
  (let [table "videos"
        args {:sort-extra "fecha desc"}]
    (build-grid params table args)))

(defn videos-form [id]
  (let [table "videos"]
    (build-form-row table id)))

(defn videos-save [{params :params}]
  (let [table "videos"]
    (build-form-save params table)))

(defn videos-delete [{params :params}]
  (let [table "videos"]
    (build-form-delete params table)))
