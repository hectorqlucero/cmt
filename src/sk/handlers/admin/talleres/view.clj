(ns sk.handlers.admin.talleres.view
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.form :refer [form build-hidden-field build-field build-select build-radio build-modal-buttons build-textarea]]
            [sk.models.grid :refer [build-grid build-modal modal-script]]))

(defn talleres-view
  [title rows]
  (let [labels ["NOMBRE" "DIRECCION" "TELEFONO" "HORARIOS"]
        db-fields [:nombre :direccion :telefono :horarios]
        fields (zipmap db-fields labels)
        table-id "talleres_table"
        args {:new true :edit true :delete true}
        href "/admin/talleres"]
    (build-grid title rows table-id fields href args)))

(defn build-talleres-fields
  [row]
  (list
   (build-hidden-field {:id "id"
                        :name "id"
                        :value (:id row)})
   (build-field {:label "NOMBRE"
                 :type "text"
                 :id "nombre"
                 :name "nombre"
                 :placeholder "nombre aqui..."
                 :required true
                 :value (:nombre row)})
   (build-field {:label "DIRECCION"
                 :type "text"
                 :id "direccion"
                 :name "direccion"
                 :placeholder "direccion aqui..."
                 :required true
                 :value (:direccion row)})
   (build-field {:label "TELEFONO"
                 :type "text"
                 :id "telefono"
                 :name "telefono"
                 :placeholder "telefono aqui..."
                 :required true
                 :value (:telefono row)})
   (build-textarea {:label "HORARIOS"
                    :id "horarios"
                    :name "horarios"
                    :rows "3"
                    :placeholder "horarios aqui..."
                    :required false
                    :value (:horarios row)})
   (build-textarea {:label "SITIO"
                    :id "sitio"
                    :name "sitio"
                    :rows "3"
                    :placeholder "sitio aqui..."
                    :required false
                    :value (:sitio row)})
   (build-textarea {:label "DIRECCIONES"
                    :id "direcciones"
                    :name "direcciones"
                    :rows "3"
                    :placeholder "direcciones aqui..."
                    :required false
                    :value (:direcciones row)})
   (build-textarea {:label "HISTORIA"
                    :id "historia"
                    :name "historia"
                    :rows "3"
                    :placeholder "historia aqui..."
                    :required false
                    :value (:historia row)})))

(defn build-talleres-form
  [title row]
  (let [fields (build-talleres-fields row)
        href "/admin/talleres/save"
        buttons (build-modal-buttons)]
    (form href fields buttons)))

(defn build-talleres-modal
  [title row]
  (build-modal title row (build-talleres-form title row)))

(defn talleres-edit-view
  [title row rows]
  (list
   (talleres-view "talleres Manteniento" rows)
   (build-talleres-modal title row)))

(defn talleres-add-view
  [title row rows]
  (list
   (talleres-view "talleres Mantenimiento" rows)
   (build-talleres-modal title row)))

(defn talleres-modal-script
  []
  (modal-script))
