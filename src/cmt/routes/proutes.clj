(ns cmt.routes.proutes
  (:require
   [compojure.core :refer [defroutes GET POST]]
   [cmt.handlers.travel-hub.controller :as travel-hub-controller]
   [cmt.handlers.comments-moderation.controller :as comments-moderation-controller]))

;; All CRUD routes now handled by parameter-driven engine
;; Add custom non-CRUD routes here if needed

(defroutes proutes
  ;; Custom routes go here
  (GET "/admin/travel-hub" request [] (travel-hub-controller/main request))
  (GET "/admin/comments/pending" request [] (comments-moderation-controller/pending request))
  (POST "/admin/comments/:id/approve" [id :as request]
        (comments-moderation-controller/approve (assoc-in request [:params :id] id)))
  (POST "/admin/comments/:id/reply" [id :as request]
        (comments-moderation-controller/reply (assoc-in request [:params :id] id))))
