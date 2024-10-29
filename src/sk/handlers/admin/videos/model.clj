(ns sk.handlers.admin.videos.model
  (:require [sk.models.crud :refer [Query db]]
            [clojure.string :as st]))

(def get-videos-sql
  (str
   "
SELECT *
FROM videos
ORDER BY fecha desc
"))

(defn get-videos
  []
  (Query db get-videos-sql))

(def get-videos-id-sql
  (str
   "
SELECT *
FROM videos
WHERE id = ?
"))

(defn get-videos-id
  [id]
  (first (Query db [get-videos-id-sql id])))
