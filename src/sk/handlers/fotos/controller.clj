(ns sk.handlers.fotos.controller
  (:require [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id]]
            [sk.handlers.fotos.model :refer [get-fotos]]
            [sk.handlers.fotos.view :refer [fotos-view
                                            fotos-scripts]]))

(defn fotos [_]
  (let [title "Fotos"
        ok (get-session-id)
        js (fotos-scripts)
        rows (get-fotos)
        content (fotos-view title rows)]
    (application title ok js content)))
