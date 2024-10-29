(ns sk.handlers.admin.fotos.controller
  (:require [sk.layout :refer [application error-404]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.models.crud :refer [build-form-save build-form-delete]]
            [sk.handlers.admin.fotos.model :refer [get-fotos get-fotos-id]]
            [sk.handlers.admin.fotos.view :refer [fotos-view fotos-edit-view fotos-add-view fotos-modal-script]]))

(defn fotos [_]
  (let [title "Fotos"
        ok (get-session-id)
        js nil
        rows (get-fotos)
        content (fotos-view title rows)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "Only <strong>los administrators </strong> can access this option!!!"))))

(defn fotos-edit
  [id]
  (let [title "Modificar fotos"
        ok (get-session-id)
        js (fotos-modal-script)
        row (get-fotos-id  id)
        rows (get-fotos)
        content (fotos-edit-view title row rows)]
    (application title ok js content)))

(defn fotos-save
  [{params :params}]
  (let [table "fotos"
        result (build-form-save params table)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/fotos")
      (error-404 "No se pudo procesar el record!" "/admin/fotos"))))

(defn fotos-add
  [_]
  (let [title "Crear nuevo fotos"
        ok (get-session-id)
        js (fotos-modal-script)
        row nil
        rows (get-fotos)
        content (fotos-add-view title row rows)]
    (application title ok js content)))

(defn fotos-delete
  [id]
  (let [table "fotos"
        result (build-form-delete table id)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/fotos")
      (error-404 "No se pudo procesar el record!" "/admin/fotos"))))
