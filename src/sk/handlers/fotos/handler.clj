(ns sk.handlers.fotos.handler
  (:require [sk.layout :refer [application]]
            [sk.models.crud :refer [Query db]]
            [sk.models.util :refer [get-session-id]]))

(def fotos-sql
  "
  SELECT
  DATE_FORMAT(fecha, '%W ') as dia,
  DATE_FORMAT(fecha, '%e de %M %Y') as f_fecha,
  enlace
  FROM fotos
  ORDER BY fecha desc")

(defn get-rows []
  (Query db fotos-sql))

(defn get-fotos [title]
  [:div.container
   [:table.table.table-hover.table-bordered
    [:caption {:style "caption-side:top;"} title]
    [:thead.table-dark
     [:tr
      [:th {:data-options "field:'enlace'" :style "text-align:center;"} "PROCESAR"]
      [:th {:data-options "field:'dia'"} "DIA"]
      [:th {:data-options "field:'f_fecha'"} "FECHA"]]]
    [:tbody.bg-white
     (let [cnt (atom 0)]
       (for [row (get-rows)]
         (let [button-id (str "button_" (swap! cnt inc))]
           [:tr
            [:td [:a.btn.btn-secondary {:id button-id :href (:enlace row) :target "_blank" :onclick (str "setColor('" button-id "','#FF851B');")} [:span.float-right "Ver Fotos"]]]
            [:td (:dia row)]
            [:td (:f_fecha row)]])))]]])

(defn fotos-scripts []
  [:script
   "
   var count = 1;
   function setColor(btn, color) {
    var property = document.getElementById(btn);
    if (count == 0) {
      property.style.color = color;
      count = 1;
    } else {
      property.style.color = color;
      count = 0
    }
   }
   "])

(defn fotos [_]
  (let [title "Fotos - Ciclismo Mexicali"
        ok (get-session-id)
        js (fotos-scripts)
        content (get-fotos title)]
    (application title ok js content)))
