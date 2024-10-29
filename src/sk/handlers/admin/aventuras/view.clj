(ns sk.handlers.admin.aventuras.view
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.form :refer [form build-hidden-field build-field build-select build-radio build-modal-buttons build-textarea]]
            [sk.handlers.admin.aventuras.model :refer [cmt-options]]
            [sk.models.util :refer [user-email]]
            [sk.models.grid :refer [build-grid build-modal modal-script]]))

(defn aventuras-view
  [title rows]
  (let [labels ["EMAIL" "FECHA" "AVENTURA"]
        db-fields [:leader_email :fecha :aventura]
        fields (zipmap db-fields labels)
        table-id "aventuras_table"
        args {:new true :edit true :delete true}
        href "/admin/aventuras"]
    (build-grid title rows table-id fields href args)))

(defn build-aventuras-fields
  [row]
  (list
   (build-hidden-field {:id "id"
                        :name "id"
                        :value (:id row)})
   (build-field {:label "EMAIL"
                 :type "text"
                 :id "leader_email"
                 :name "leader_email"
                 :placeholder "leader_email aqui..."
                 :required true
                 :value (or (:leader_email row) (user-email))})
   (build-field {:label "FECHA"
                 :type "date"
                 :id "fecha"
                 :name "fecha"
                 :required true
                 :value (:fecha row)})
   (build-select {:label "CMT"
                  :type "text"
                  :id "cmt_id"
                  :name "cmt_id"
                  :placeholder "cmt_id aqui..."
                  :required true
                  :options (cmt-options)
                  :value (:cmt_id row)})
   (build-textarea {:label "ENLACE FOTOS"
                    :id "enlace"
                    :name "enlace"
                    :rows "1"
                    :placeholder "enlace aqui..."
                    :required false
                    :value (:enlace row)})
   (build-textarea {:label "ENLACE VIDEOS"
                    :id "enlacev"
                    :name "enlacev"
                    :rows "1"
                    :placeholder "enlacev aqui..."
                    :required false
                    :value (:enlacev row)})
   (build-textarea {:label "AVENTURA"
                    :id "aventura"
                    :name "aventura"
                    :rows "8"
                    :placeholder "aventura aqui..."
                    :required true
                    :value (:aventura row)})))

(defn build-aventuras-form
  [title row]
  (let [fields (build-aventuras-fields row)
        href "/admin/aventuras/save"
        buttons (build-modal-buttons)]
    (form href fields buttons)))

(defn build-aventuras-modal
  [title row]
  (build-modal title row (build-aventuras-form title row)))

(defn aventuras-edit-view
  [title row rows]
  (list
   (aventuras-view "aventuras Manteniento" rows)
   (build-aventuras-modal title row)))

(defn aventuras-add-view
  [title row rows]
  (list
   (aventuras-view "aventuras Mantenimiento" rows)
   (build-aventuras-modal title row)))

(defn aventuras-modal-script
  []
  (modal-script))
