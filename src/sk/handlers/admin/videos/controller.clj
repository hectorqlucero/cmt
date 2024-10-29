(ns sk.handlers.admin.videos.controller
  (:require [sk.layout :refer [application error-404]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.models.crud :refer [build-form-save build-form-delete]]
            [sk.handlers.admin.videos.model :refer [get-videos get-videos-id]]
            [sk.handlers.admin.videos.view :refer [videos-view videos-edit-view videos-add-view videos-modal-script]]))

(defn videos [_]
  (let [title "Videos"
        ok (get-session-id)
        js nil
        rows (get-videos)
        content (videos-view title rows)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "Only <strong>los administrators </strong> can access this option!!!"))))

(defn videos-edit
  [id]
  (let [title "Modificar videos"
        ok (get-session-id)
        js (videos-modal-script)
        row (get-videos-id  id)
        rows (get-videos)
        content (videos-edit-view title row rows)]
    (application title ok js content)))

(defn videos-save
  [{params :params}]
  (let [table "videos"
        result (build-form-save params table)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/videos")
      (error-404 "No se pudo procesar el record!" "/admin/videos"))))

(defn videos-add
  [_]
  (let [title "Crear nuevo videos"
        ok (get-session-id)
        js (videos-modal-script)
        row nil
        rows (get-videos)
        content (videos-add-view title row rows)]
    (application title ok js content)))

(defn videos-delete
  [id]
  (let [table "videos"
        result (build-form-delete table id)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/videos")
      (error-404 "No se pudo procesar el record!" "/admin/videos"))))
