(ns sk.handlers.talleres.view
  (:require [sk.models.util :refer [capitalize-words]]))

(defn line-rr [label value]
  (list
   [:div.row
    [:div.col-xs.col-sm-4.col-md-3.col-lg-2.text-primary [:strong label]]
    [:div.col-xs.8.col-sm-8.col-md-9.col-lg-10 value]]))

(defn body-rr [row]
  (list
   [:div.container.border.border-dark.rounded {:style "margin-bottom:10px;"}
    [:h2.card-title (capitalize-words (:nombre row))]
    (line-rr "Sitio:"
             [:div.card-action
              (if-not (clojure.string/blank? (:sitio row))
                [:a.btn.btn-secondary {:href (str (:sitio row))
                                       :target "_blank"} [:strong.text-secondary "Click aqui para ir al sitio"]]
                [:strong.text-secondary "No hay facebook o pagina disponible"])])
    (line-rr "Dirección:" (:direccion row))
    (line-rr "Horarios:" (:horarios row))
    (line-rr "Telefono:" (:telefono row))
    (line-rr "Mapa:"
             [:div.card-action
              [:a.btn.btn-secondary {:href (str (:direcciones row))
                                     :target "_blank"} [:strong.text-secondary "Cómo Llegar"]]]) [:br]]))

(defn reporte-view [_ rows]
  [:div.row
   [:div.col
    [:div.card
     (map body-rr rows)]]])

(defn reporte-scripts [])
