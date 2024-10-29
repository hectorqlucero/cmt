(ns sk.routes.routes
  (:require [compojure.core :refer [defroutes GET POST]]
            [sk.handlers.home.controller :as home-controller]
            [sk.handlers.fotos.controller :as fotos-dashboard]
            [sk.handlers.videos.controller :as videos-dashboard]
            [sk.handlers.talleres.controller :as talleres-dashboard]
            [sk.handlers.aventuras.controller :as aventuras-dashboard]))

(defroutes open-routes
  (GET "/" params home-controller/main)
  (GET "/home/login" params home-controller/login)
  (POST "/home/login" params home-controller/login-user)
  (GET "/home/logoff" params home-controller/logoff-user)
  (GET "/change/password" params home-controller/change-password)
  (POST "/change/password" req [] (home-controller/process-password req))

  (GET "/fotos/list" params [] (fotos-dashboard/fotos params))
  (GET "/videos/list" params [] (videos-dashboard/videos params))
  (GET "/talleres/list" params [] (talleres-dashboard/reporte params))
  (GET "/aventuras/:id" [id] (aventuras-dashboard/aventuras id))
  (POST "/aventuras/comentarios" params [] (aventuras-dashboard/comentarios params)))
