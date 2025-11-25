(ns sk.handlers.fotos.view)

(defn fotos-view
  [title rows]
  [:div.content-area
   [:div.container-fluid {:style "max-width: 1400px;"}
    [:div.row.mb-4
     [:div.col-12
      [:div.d-flex.justify-content-between.align-items-center
       [:h2 {:style "color: var(--color-primary); margin-bottom: 0;"} title]
       [:div {:style "background: var(--color-primary); color: white; font-size: 0.875rem; padding: 0.5rem 1rem; border-radius: 6px;"}
        (str (count rows) " " (if (= (count rows) 1) "Foto" "Fotos"))]]]]

    [:div.row.g-3
     (for [row rows]
       [:div.col-12.col-md-6.col-lg-4
        [:div.card.h-100.shadow {:style "border-radius: 12px; overflow: hidden; border-left: 4px solid var(--color-primary); background: rgba(var(--color-primary-rgb), 0.02);"}
         [:div.card-body.p-4
          [:div.d-flex.justify-content-between.align-items-start.mb-3
           [:div
            [:h5.card-title.mb-2 {:style "color: var(--color-primary); font-weight: 600;"}
             [:i.fas.fa-calendar.me-2] (:dia row)]
            [:p.card-text.text-muted.mb-1 {:style "font-size: 0.875rem;"} (:f_fecha row)]]
           [:span {:style "background: var(--color-primary); color: white; font-size: 0.75rem; padding: 0.375rem 0.75rem; border-radius: 4px;"}
            "Foto"]]
          [:div.d-grid.gap-2
           [:a {:href (:enlace row)
                :target "_blank"
                :style "display: inline-flex; align-items: center; gap: 6px; background: var(--color-primary); color: white; border: 1px solid var(--color-primary); padding: 0.75rem; font-weight: 500; border-radius: 8px; text-decoration: none; transition: all 0.2s ease;"}
            [:i.fas.fa-images] " Ver Fotos"]]]]])]]])

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