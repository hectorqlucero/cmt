(ns sk.handlers.talleres.view
  (:require [sk.models.util :refer [capitalize-words]]
            [clojure.string :as str]))

(defn body-rr [row]
  [:div.col-12.col-md-6.col-lg-4.mb-4
   [:div.card.h-100.shadow {:style "border-radius: 12px; overflow: hidden; border-left: 4px solid var(--color-primary); background: rgba(var(--color-primary-rgb), 0.02);"}
    [:div.card-header {:style "border-radius: 12px 12px 0 0; background: rgba(var(--color-success-rgb), 0.08); color: var(--color-text); border-bottom: 1px solid var(--color-border);"}
     [:h5.mb-0 [:i.fas.fa-tools.me-2] (capitalize-words (:nombre row))]]
    [:div.card-body.p-4
     [:div.mb-3
      [:strong {:style "color: var(--color-text-muted); font-size: 0.875rem;"} "Sitio:"]
      [:div.mt-1
       (if-not (str/blank? (:sitio row))
         [:a {:href (str (:sitio row))
              :target "_blank"
              :style "display: inline-flex; align-items: center; gap: 6px; background: rgba(var(--color-primary-rgb), 0.1); color: var(--color-primary); border: 1px solid var(--color-primary); padding: 0.375rem 0.75rem; font-size: 0.875rem; font-weight: 500; border-radius: 6px; text-decoration: none; transition: all 0.2s ease;"}
          [:i.fas.fa-external-link-alt] "Visitar Sitio"]
         [:span {:style "font-size: 0.875rem; color: var(--color-text-muted);"} "No disponible"])]]
     [:div.mb-3
      [:strong {:style "color: var(--color-text-muted); font-size: 0.875rem;"} "Dirección:"]
      [:div.mt-1 {:style "font-size: 0.875rem;"} (:direccion row)]]
     [:div.mb-3
      [:strong {:style "color: var(--color-text-muted); font-size: 0.875rem;"} "Horarios:"]
      [:div.mt-1 {:style "font-size: 0.875rem;"} (:horarios row)]]
     [:div.mb-3
      [:strong {:style "color: var(--color-text-muted); font-size: 0.875rem;"} "Teléfono:"]
      [:div.mt-1 {:style "font-size: 0.875rem;"} (:telefono row)]]
     [:div
      [:strong {:style "color: var(--color-text-muted); font-size: 0.875rem;"} "Mapa:"]
      [:div.mt-1
       [:a {:href (str (:direcciones row))
            :target "_blank"
            :style "display: inline-flex; align-items: center; gap: 6px; background: var(--color-primary); color: white; border: 1px solid var(--color-primary); padding: 0.375rem 0.75rem; font-size: 0.875rem; font-weight: 500; border-radius: 6px; text-decoration: none; transition: all 0.2s ease;"}
        [:i.fas.fa-map-marker-alt] "Cómo Llegar"]]]]]])

(defn reporte-view [_ rows]
  [:div.content-area
   [:div.container-fluid {:style "max-width: 1400px;"}
    [:div.row.mb-4
     [:div.col-12
      [:div.d-flex.justify-content-between.align-items-center
       [:h2 {:style "color: var(--color-primary); margin-bottom: 0;"} "Talleres en Mexicali BC Mexico"]
       [:div {:style "background: var(--color-primary); color: white; font-size: 0.875rem; padding: 0.5rem 1rem; border-radius: 6px;"}
        (str (count rows) " " (if (= (count rows) 1) "Taller" "Talleres"))]]]]

    [:div.row.g-3
     (map body-rr rows)]]])

(defn reporte-scripts [])
