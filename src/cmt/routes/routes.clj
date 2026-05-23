(ns cmt.routes.routes
  (:require
   [compojure.core :refer [defroutes GET POST]]
  [cmt.handlers.home.controller :as home-controller]
  [cmt.handlers.blog.controller :as blog-controller]))

(defroutes open-routes
  (GET "/" params [] (blog-controller/blog-index params))
  (GET "/blog" params [] (blog-controller/blog-index params))
  (GET "/blog/stories" params [] (blog-controller/blog-stories params))
  (GET "/blog/videos" params [] (blog-controller/blog-videos params))
  (GET "/blog/photos" params [] (blog-controller/blog-photos params))
  (GET "/blog/workshops" params [] (blog-controller/blog-workshops params))
  (GET "/blog/adventure/:id" [id :as params] (blog-controller/blog-adventure (assoc-in params [:params :id] id)))
  (POST "/blog/adventure/:id/comment" [id :as params] (blog-controller/blog-add-comment (assoc-in params [:params :id] id)))
  (GET "/home/login" params [] (home-controller/login params))
  (POST "/home/login" params [] (home-controller/login-user params))
  (GET "/home/logoff" params [] (home-controller/logoff-user params)))

(defroutes password-routes
  (GET "/change/password" params [] (home-controller/change-password params))
  (POST "/change/password" params [] (home-controller/process-password params)))
