(ns sk.handlers.admin.videos.model
(:require [sk.models.crud :refer [Query db]]))

(defn get-rows [tabla]
(Query db [(str "select * from " tabla)]))

(comment
(get-rows "videos"))