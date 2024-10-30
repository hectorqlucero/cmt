(ns sk.handlers.talleres.view
  (:require [sk.models.util :refer [capitalize-words]]))

(defn line-rr [label value]
  (list
   [:div.row
    [:div.col-xs.col-sm-4.col-md-3.col-lg-2 [:strong.text-secondary label]]
    [:div.col-xs.8.col-sm-8.col-md-9.col-lg-10 value]]))

(defn body-rr [row]
  (list
   ; [:div.container.border.border-dark.rounded {:style "margin-bottom:10px;"}]
   [:div.card
    [:h5.card-header (capitalize-words (:nombre row))]
    (line-rr "Sitio:"
             [:div.card-action
              (if-not (clojure.string/blank? (:sitio row))
                [:a.text-secondary {:role "button"
                                    :href (str (:sitio row))
                                    :target "_blank"} [:strong "Click aqui para ir al sitio"]]
                [:strong.text-secondary "No hay facebook o pagina disponible"])])
    (line-rr "Dirección:" (:direccion row))
    (line-rr "Horarios:" (:horarios row))
    (line-rr "Telefono:" (:telefono row))
    (line-rr "Mapa:"
             [:div.card-action
              [:a.text-secondary {:role "button"
                                  :href (str (:direcciones row))
                                  :target "_blank"} [:strong "Cómo Llegar"]]])] [:br]))

(defn reporte-view [_ rows]
  (map body-rr rows))

(defn reporte-scripts [])
