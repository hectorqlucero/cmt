(ns sk.handlers.admin.aventuras.controller
  (:require [sk.layout :refer [application error-404]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.models.crud :refer [build-form-save build-form-delete]]
            [sk.handlers.admin.aventuras.model :refer [get-aventuras get-aventuras-id]]
            [sk.handlers.admin.aventuras.view :refer [aventuras-view aventuras-edit-view aventuras-add-view aventuras-modal-script]]))

(defn aventuras [_]
  (let [title "Aventuras"
        ok (get-session-id)
        js nil
        rows (get-aventuras)
        content (aventuras-view title rows)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "Only <strong>los administrators </strong> can access this option!!!"))))

(defn aventuras-edit
  [id]
  (let [title "Modificar aventuras"
        ok (get-session-id)
        js (aventuras-modal-script)
        row (get-aventuras-id  id)
        rows (get-aventuras)
        content (aventuras-edit-view title row rows)]
    (application title ok js content)))

(defn aventuras-save
  [{params :params}]
  (let [table "aventuras"
        result (build-form-save params table)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/aventuras")
      (error-404 "No se pudo procesar el record!" "/admin/aventuras"))))

(defn aventuras-add
  [_]
  (let [title "Crear nueva aventura"
        ok (get-session-id)
        js (aventuras-modal-script)
        row nil
        rows (get-aventuras)
        content (aventuras-add-view title row rows)]
    (application title ok js content)))

(defn aventuras-delete
  [id]
  (let [table "aventuras"
        result (build-form-delete table id)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/aventuras")
      (error-404 "No se pudo procesar el record!" "/admin/aventuras"))))
