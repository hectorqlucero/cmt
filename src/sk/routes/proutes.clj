(ns sk.routes.proutes
  (:require
   [compojure.core :refer [defroutes GET POST]]
   [sk.handlers.admin.users.controller :as users-controller]
   [sk.handlers.admin.aventuras.controller :as aventuras-controller]
   [sk.handlers.admin.cmt.controller :as cmt-controller]
   [sk.handlers.admin.fotos.controller :as fotos-controller]
   [sk.handlers.admin.videos.controller :as videos-controller]
   [sk.handlers.admin.talleres.controller :as talleres-controller]
   [sk.handlers.users.controller :as users-dashboard]))

(defroutes proutes
  (GET "/admin/users" params users-controller/users)
  (GET "/admin/users/edit/:id" [id] (users-controller/users-edit id))
  (POST "/admin/users/save" params [] (users-controller/users-save params))
  (GET "/admin/users/add" params [] (users-controller/users-add params))
  (GET "/admin/users/delete/:id" [id] (users-controller/users-delete id))

  (GET "/admin/aventuras" params aventuras-controller/aventuras)
  (GET "/admin/aventuras/edit/:id" [id] (aventuras-controller/aventuras-edit id))
  (POST "/admin/aventuras/save" params [] (aventuras-controller/aventuras-save params))
  (GET "/admin/aventuras/add" params [] (aventuras-controller/aventuras-add params))
  (GET "/admin/aventuras/delete/:id" [id] (aventuras-controller/aventuras-delete id))

  (GET "/admin/cmt" params cmt-controller/cmt)
  (GET "/admin/cmt/edit/:id" [id] (cmt-controller/cmt-edit id))
  (POST "/admin/cmt/save" params [] (cmt-controller/cmt-save params))
  (GET "/admin/cmt/add" params [] (cmt-controller/cmt-add params))
  (GET "/admin/cmt/delete/:id" [id] (cmt-controller/cmt-delete id))

  (GET "/admin/fotos" params fotos-controller/fotos)
  (GET "/admin/fotos/edit/:id" [id] (fotos-controller/fotos-edit id))
  (POST "/admin/fotos/save" params [] (fotos-controller/fotos-save params))
  (GET "/admin/fotos/add" params [] (fotos-controller/fotos-add params))
  (GET "/admin/fotos/delete/:id" [id] (fotos-controller/fotos-delete id))

  (GET "/admin/videos" params videos-controller/videos)
  (GET "/admin/videos/edit/:id" [id] (videos-controller/videos-edit id))
  (POST "/admin/videos/save" params [] (videos-controller/videos-save params))
  (GET "/admin/videos/add" params [] (videos-controller/videos-add params))
  (GET "/admin/videos/delete/:id" [id] (videos-controller/videos-delete id))

  (GET "/admin/talleres" params talleres-controller/talleres)
  (GET "/admin/talleres/edit/:id" [id] (talleres-controller/talleres-edit id))
  (POST "/admin/talleres/save" params [] (talleres-controller/talleres-save params))
  (GET "/admin/talleres/add" params [] (talleres-controller/talleres-add params))
  (GET "/admin/talleres/delete/:id" [id] (talleres-controller/talleres-delete id))

  (GET "/users" params [] (users-dashboard/users params)))
