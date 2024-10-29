(ns sk.handlers.admin.fotos.view
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.form :refer [form build-hidden-field build-field build-select build-radio build-modal-buttons build-textarea]]
            [sk.models.grid :refer [build-grid build-modal modal-script]]))

(defn fotos-view
  [title rows]
  (let [labels ["FECHA" "ENLACE"]
        db-fields [:fecha :enlace]
        fields (zipmap db-fields labels)
        table-id "fotos_table"
        args {:new true :edit true :delete true}
        href "/admin/fotos"]
    (build-grid title rows table-id fields href args)))

(defn build-fotos-fields
  [row]
  (list
   (build-hidden-field {:id "id"
                        :name "id"
                        :value (:id row)})
   (build-field {:label "FECHA"
                 :type "date"
                 :id "fecha"
                 :name "fecha"
                 :required true
                 :value (:fecha row)})
   (build-textarea {:label "ENLACE"
                    :id "enlace"
                    :name "enlace"
                    :rows "1"
                    :placeholder "enlace aqui..."
                    :required true
                    :value (:enlace row)})))

(defn build-fotos-form
  [title row]
  (let [fields (build-fotos-fields row)
        href "/admin/fotos/save"
        buttons (build-modal-buttons)]
    (form href fields buttons)))

(defn build-fotos-modal
  [title row]
  (build-modal title row (build-fotos-form title row)))

(defn fotos-edit-view
  [title row rows]
  (list
   (fotos-view "fotos Manteniento" rows)
   (build-fotos-modal title row)))

(defn fotos-add-view
  [title row rows]
  (list
   (fotos-view "fotos Mantenimiento" rows)
   (build-fotos-modal title row)))

(defn fotos-modal-script
  []
  (modal-script))
