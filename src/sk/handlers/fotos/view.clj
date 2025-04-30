(ns sk.handlers.fotos.view)

(defn fotos-view
  [title rows]
  (list
   [:div.container.table-responsive
    [:table.table.table-hover.table-bordered.table-sm.table-fixed
     [:h3.text-center.text-info title]
     [:thead.table-light
      [:tr
       [:th {:data-options "field:'enlace'" :style "text-align:center;width: 126px;"} "PROCESAR"]
       [:th {:data-options "field:'dia'" :style "text-align:center;width: fit-content;"} "DIA"]
       [:th {:data-options "field:'f_fecha'" :style "text-align:center;width: fit-content;"} "FECHA"]]]

     [:tbody.bg-white
      (let [cnt (atom 0)]
        (for [row rows]
          (let [button-id (str "button_" (swap! cnt inc))]
            [:tr
             [:td {:style "text-align:center;width: 126px;"} [:a.btn.btn-outline-success {:id button-id
                                                                                          :href (:enlace row)
                                                                                          :target "_blank"
                                                                                          :onclick (str "setColor('" button-id "','#FF851B');")} [:span.float-right "Ver Fotos"]]]
             [:td {:style "text-align:center;width: fit-content;"} (:dia row)]
             [:td {:style "text-align:center;width: fit-content;"} (:f_fecha row)]])))]]]))

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
