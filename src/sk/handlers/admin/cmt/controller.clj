(ns sk.handlers.admin.cmt.controller
  (:require [sk.layout :refer [application error-404]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.models.crud :refer [build-form-save build-form-delete]]
            [sk.handlers.admin.cmt.model :refer [get-cmt get-cmt-id]]
            [sk.handlers.admin.cmt.view :refer [cmt-view cmt-edit-view cmt-add-view cmt-modal-script]]))

(defn cmt [_]
  (let [title "Cmt"
        ok (get-session-id)
        js nil
        rows (get-cmt)
        content (cmt-view title rows)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "Only <strong>los administrators </strong> can access this option!!!"))))

(defn cmt-edit
  [id]
  (let [title "Modificar cmt"
        ok (get-session-id)
        js (cmt-modal-script)
        row (get-cmt-id  id)
        rows (get-cmt)
        content (cmt-edit-view title row rows)]
    (application title ok js content)))

(defn cmt-save
  [{params :params}]
  (let [table "cmt"
        result (build-form-save params table)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/cmt")
      (error-404 "No se pudo procesar el record!" "/admin/cmt"))))

(defn cmt-add
  [_]
  (let [title "Crear nuevo cmt"
        ok (get-session-id)
        js (cmt-modal-script)
        row nil
        rows (get-cmt)
        content (cmt-add-view title row rows)]
    (application title ok js content)))

(defn cmt-delete
  [id]
  (let [table "cmt"
        result (build-form-delete table id)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/cmt")
      (error-404 "No se pudo procesar el record!" "/admin/cmt"))))
