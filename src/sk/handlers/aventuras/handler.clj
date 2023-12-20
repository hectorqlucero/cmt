(ns sk.handlers.aventuras.handler
  (:require [sk.handlers.aventuras.view :refer [aventuras-scripts aventuras-view]]
            [sk.models.crud :refer [build-form-save]]
            [sk.layout :refer [application]]
            [sk.models.email :refer [host send-email]]
            [sk.migrations :refer [config]]
            [sk.handlers.aventuras.model :refer [get-cmt-rows get-rows get-row get-cmt-row]]
            [sk.models.util :refer [get-session-id parse-int]]))

(defn aventuras [id]
  (let [title "Cicloturismo"
        ok (get-session-id)
        js (aventuras-scripts)
        rows (get-rows (parse-int id))
        crow (first (get-cmt-rows (parse-int id)))
        content (aventuras-view rows crow)]
    (application title ok js content)))

(defn email-body [nombre comments aventuras-id]
  (let [row (get-row aventuras-id)
        blog (get-cmt-row (row :cmt_id))
        content (str "<strong>Blog: </strong>" (blog :nombre) "<br>"
                     "<strong>id: </strong>" aventuras-id "<br>"
                     "<strong>Nombre: </strong>" (row :leader_email) "<br>"
                     "<strong>Fecha: </strong>" (row :fecha) "<br>"
                     "<strong>Aventura: </strong>" (row :aventura) "<br><br>"
                     "<strong>Comentarios: </strong><br>"
                     "<strong>Autor: </strong>"
                     nombre
                     "<br>"
                     "<strong>Commentario: </strong>" comments)
        body {:from (config :email-user)
              :to "hectorqlucero@gmail.com"
              :subject "Comentario en aventuras"
              :body [{:type "text/html;charset=utf-8"
                      :content content}]}]
    body))

(defn comentarios [{params :params}]
  (let [aventuras-id (params :aventuras_id)
        nombre (params :nombre)
        comments (params :comments)
        email-body (email-body nombre comments aventuras-id)
        table "aventuras_link"]
    (if (future (send-email host email-body))
      (build-form-save params table))))

(comment
  (email-body "Hector Lucero" "Puto comments" 386)
  (get-row 386)
  (get-cmt-row 4)
  (get-rows 1))
