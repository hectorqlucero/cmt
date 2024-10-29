(ns sk.handlers.admin.videos.view
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.form :refer [form build-hidden-field build-field build-select build-radio build-modal-buttons build-textarea]]
            [sk.models.grid :refer [build-grid build-modal modal-script]]))

(defn videos-view
  [title rows]
  (let [labels ["FECHA" "TITULO" "ENLACE"]
        db-fields [:fecha :titulo :enlace]
        fields (zipmap db-fields labels)
        table-id "videos_table"
        args {:new true :edit true :delete true}
        href "/admin/videos"]
    (build-grid title rows table-id fields href args)))

(defn build-videos-fields
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
   (build-field {:label "TITULO"
                 :type "text"
                 :id "titulo"
                 :name "titulo"
                 :placeholder "titulo aqui..."
                 :required false
                 :value (:titulo row)})
   (build-textarea {:label "ENLACE"
                    :id "enlace"
                    :name "enlace"
                    :rows "1"
                    :placeholder "enlace aqui..."
                    :required true
                    :value (:enlace row)})))

(defn build-videos-form
  [title row]
  (let [fields (build-videos-fields row)
        href "/admin/videos/save"
        buttons (build-modal-buttons)]
    (form href fields buttons)))

(defn build-videos-modal
  [title row]
  (build-modal title row (build-videos-form title row)))

(defn videos-edit-view
  [title row rows]
  (list
   (videos-view "videos Manteniento" rows)
   (build-videos-modal title row)))

(defn videos-add-view
  [title row rows]
  (list
   (videos-view "videos Mantenimiento" rows)
   (build-videos-modal title row)))

(defn videos-modal-script
  []
  (modal-script))
