(ns sk.handlers.videos.controller
  (:require [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id]]
            [sk.handlers.videos.model :refer [get-videos]]
            [sk.handlers.videos.view :refer [videos-view
                                             videos-scripts]]))

(defn videos [_]
  (let [title "Videos"
        ok (get-session-id)
        js (videos-scripts)
        rows (get-videos)
        content (videos-view title rows)]
    (application title ok js content)))
