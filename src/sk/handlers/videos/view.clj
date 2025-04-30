(ns sk.handlers.videos.view)

(defn videos-view
  [title rows]
  (list
   [:div.container
    [:table.table.table-hover.table-bordered
     [:h3.text-center.text-info title]
     [:thead.table-light
      [:tr
       [:th {:data-options "field:'enlace'" :style "text-align:center;width:140px;"} "PROCESAR"]
       [:th {:data-options "field:'titulo'" :style "text-align:center;"} "TITULO"]
       [:th {:data-options "field:'dia'"} "DIA"]
       [:th {:data-options "field:'f_fecha'"} "FECHA"]]]

     [:tbody.bg-white
      (let [cnt (atom 0)]
        (for [row rows]
          (let [button-id (str "button_" (swap! cnt inc))]
            [:tr
             [:td {:style "text-align:center;width: 140px;"} [:a.btn.btn-outline-success {:id button-id
                                                                                          :href (:enlace row)
                                                                                          :target "_blank"
                                                                                          :onclick (str "setColor('" button-id "','#FF851B');")} [:span.float-right "Ver Videos"]]]
             [:td (:titulo row)]
             [:td (:dia row)]
             [:td (:f_fecha row)]])))]]]))

(defn videos-scripts []
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
