(ns sk.handlers.talleres.controller
  (:require [sk.handlers.talleres.view :refer [reporte-scripts
                                               reporte-view]]
            [sk.handlers.talleres.model :refer [get-talleres]]
            [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id]]))

(defn reporte
  [_]
  (let [title "Talleres de bicicletas"
        ok (get-session-id)
        js (reporte-scripts)
        rows (get-talleres)
        content (reporte-view title rows)]
    (application title ok js content)))
