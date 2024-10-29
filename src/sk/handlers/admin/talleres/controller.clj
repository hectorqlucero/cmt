(ns sk.handlers.admin.talleres.controller
  (:require [sk.layout :refer [application error-404]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.models.crud :refer [build-form-save build-form-delete]]
            [sk.handlers.admin.talleres.model :refer [get-talleres get-talleres-id]]
            [sk.handlers.admin.talleres.view :refer [talleres-view talleres-edit-view talleres-add-view talleres-modal-script]]))

(defn talleres [_]
  (let [title "Talleres"
        ok (get-session-id)
        js nil
        rows (get-talleres)
        content (talleres-view title rows)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "Only <strong>los administrators </strong> can access this option!!!"))))

(defn talleres-edit
  [id]
  (let [title "Modificar talleres"
        ok (get-session-id)
        js (talleres-modal-script)
        row (get-talleres-id  id)
        rows (get-talleres)
        content (talleres-edit-view title row rows)]
    (application title ok js content)))

(defn talleres-save
  [{params :params}]
  (let [table "talleres"
        result (build-form-save params table)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/talleres")
      (error-404 "No se pudo procesar el record!" "/admin/talleres"))))

(defn talleres-add
  [_]
  (let [title "Crear nuevo talleres"
        ok (get-session-id)
        js (talleres-modal-script)
        row nil
        rows (get-talleres)
        content (talleres-add-view title row rows)]
    (application title ok js content)))

(defn talleres-delete
  [id]
  (let [table "talleres"
        result (build-form-delete table id)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/talleres")
      (error-404 "No se pudo procesar el record!" "/admin/talleres"))))
