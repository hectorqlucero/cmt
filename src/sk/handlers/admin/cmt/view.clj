(ns sk.handlers.admin.cmt.view
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.form :refer [form build-hidden-field build-field build-select build-radio build-modal-buttons build-textarea]]
            [sk.models.grid :refer [build-grid build-modal modal-script]]))

(defn cmt-view
  [title rows]
  (let [labels ["NOMBRE" "MAXIMO"]
        db-fields [:nombre :maximo]
        fields (zipmap db-fields labels)
        table-id "cmt_table"
        args {:new true :edit true :delete true}
        href "/admin/cmt"]
    (build-grid title rows table-id fields href args)))

(defn build-cmt-fields
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
   (build-textarea {:label "COMMENTARIOS"
                    :id "comments"
                    :name "comments"
                    :rows "3"
                    :placeholder "comments aqui..."
                    :required false
                    :value (:comments row)})
   (build-field {:label "MAXIMO"
                 :type "text"
                 :id "maximo"
                 :name "maximo"
                 :placeholder "maximo aqui..."
                 :required true
                 :value (:maximo row)})))

(defn build-cmt-form
  [title row]
  (let [fields (build-cmt-fields row)
        href "/admin/cmt/save"
        buttons (build-modal-buttons)]
    (form href fields buttons)))

(defn build-cmt-modal
  [title row]
  (build-modal title row (build-cmt-form title row)))

(defn cmt-edit-view
  [title row rows]
  (list
   (cmt-view "cmt Manteniento" rows)
   (build-cmt-modal title row)))

(defn cmt-add-view
  [title row rows]
  (list
   (cmt-view "cmt Mantenimiento" rows)
   (build-cmt-modal title row)))

(defn cmt-modal-script
  []
  (modal-script))
