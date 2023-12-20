(ns sk.routes.proutes
(:require 
[compojure.core :refer [defroutes GET POST]]
[sk.handlers.admin.menus.handler :as menus]
[sk.handlers.admin.users.handler :as users]
[sk.handlers.admin.pincludes.handler :as pincludes]
[sk.handlers.admin.proutes.handler :as proutes]
[sk.handlers.admin.rincludes.handler :as rincludes]
[sk.handlers.admin.routes.handler :as routes]
[sk.handlers.admin.cmt.handler :as cmt]
[sk.handlers.admin.aventuras.handler :as aventuras]
[sk.handlers.admin.fotos.handler :as fotos]
[sk.handlers.admin.talleres.handler :as talleres]
[sk.handlers.admin.videos.handler :as videos]
))

(defroutes proutes
(GET "/admin/menus" req [] (menus/menus req))
(POST "/admin/menus" req [] (menus/menus-grid req))
(GET "/admin/menus/edit/:id" [id] (menus/menus-form id))
(POST "/admin/menus/save" req [] (menus/menus-save req))
(POST "/admin/menus/delete" req [] (menus/menus-delete req))
(GET "/admin/users" req [] (users/users req))
(POST "/admin/users" req [] (users/users-grid req))
(GET "/admin/users/edit/:id" [id] (users/users-form id))
(POST "/admin/users/save" req [] (users/users-save req))
(POST "/admin/users/delete" req [] (users/users-delete req))
(GET "/admin/pincludes" req [] (pincludes/pincludes req))
(POST "/admin/pincludes" req [] (pincludes/pincludes-grid req))
(GET "/admin/pincludes/edit/:id" [id] (pincludes/pincludes-form id))
(POST "/admin/pincludes/save" req [] (pincludes/pincludes-save req))
(POST "/admin/pincludes/delete" req [] (pincludes/pincludes-delete req))
(GET "/admin/proutes" req [] (proutes/proutes req))
(POST "/admin/proutes" req [] (proutes/proutes-grid req))
(GET "/admin/proutes/edit/:id" [id] (proutes/proutes-form id))
(POST "/admin/proutes/save" req [] (proutes/proutes-save req))
(POST "/admin/proutes/delete" req [] (proutes/proutes-delete req))
(GET "/admin/rincludes" req [] (rincludes/rincludes req))
(POST "/admin/rincludes" req [] (rincludes/rincludes-grid req))
(GET "/admin/rincludes/edit/:id" [id] (rincludes/rincludes-form id))
(POST "/admin/rincludes/save" req [] (rincludes/rincludes-save req))
(POST "/admin/rincludes/delete" req [] (rincludes/rincludes-delete req))
(GET "/admin/routes" req [] (routes/routes req))
(POST "/admin/routes" req [] (routes/routes-grid req))
(GET "/admin/routes/edit/:id" [id] (routes/routes-form id))
(POST "/admin/routes/save" req [] (routes/routes-save req))
(POST "/admin/routes/delete" req [] (routes/routes-delete req))
(GET "/admin/cmt" req [] (cmt/cmt req))
(POST "/admin/cmt" req [] (cmt/cmt-grid req))
(GET "/admin/cmt/edit/:id" [id] (cmt/cmt-form id))
(POST "/admin/cmt/save" req [] (cmt/cmt-save req))
(POST "/admin/cmt/delete" req [] (cmt/cmt-delete req))
(GET "/admin/aventuras" req [] (aventuras/aventuras req))
(POST "/admin/aventuras" req [] (aventuras/aventuras-grid req))
(GET "/admin/aventuras/edit/:id" [id] (aventuras/aventuras-form id))
(POST "/admin/aventuras/save" req [] (aventuras/aventuras-save req))
(POST "/admin/aventuras/delete" req [] (aventuras/aventuras-delete req))
(GET "/admin/fotos" req [] (fotos/fotos req))
(POST "/admin/fotos" req [] (fotos/fotos-grid req))
(GET "/admin/fotos/edit/:id" [id] (fotos/fotos-form id))
(POST "/admin/fotos/save" req [] (fotos/fotos-save req))
(POST "/admin/fotos/delete" req [] (fotos/fotos-delete req))
(GET "/admin/talleres" req [] (talleres/talleres req))
(POST "/admin/talleres" req [] (talleres/talleres-grid req))
(GET "/admin/talleres/edit/:id" [id] (talleres/talleres-form id))
(POST "/admin/talleres/save" req [] (talleres/talleres-save req))
(POST "/admin/talleres/delete" req [] (talleres/talleres-delete req))
(GET "/admin/videos" req [] (videos/videos req))
(POST "/admin/videos" req [] (videos/videos-grid req))
(GET "/admin/videos/edit/:id" [id] (videos/videos-form id))
(POST "/admin/videos/save" req [] (videos/videos-save req))
(POST "/admin/videos/delete" req [] (videos/videos-delete req))
(GET "/talleres" req [] (talleres/talleres req))
)