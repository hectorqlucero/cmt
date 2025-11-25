(ns sk.models.grid
  (:require
   [clojure.string :as st]))

;; start build-gid
(defn build-grid-head
  [href fields & args]
  (let [args (first args)
        new (:new args)]
    [:thead {:style "background: var(--color-bg-subtle); color: var(--color-text);"}
     (for [field fields]
       [:th {:data-sortable "true"
             :data-field (key field)
             :style "border-color: var(--color-border);"} (st/upper-case (val field))])
     [:th.text-center {:style "white-space:nowrap;width:128px; border-color: var(--color-border);"}
      [:a.btn {:role "button"
               :style (str "background: var(--color-success); color: var(--color-text-on-success); border-color: var(--color-success);"
                           (when (= new false) " opacity: 0.5; pointer-events: none;"))
               :href (str href "/add")}
       [:i.bi.bi-plus-lg] " Nuevo Record"]]]))

(defn build-grid-body
  [rows href fields & args]
  (let [args (first args)
        edit (:edit args)
        delete (:delete args)]
    [:tbody
     (for [row rows]
       [:tr
        (for [field fields]
          [:td.text-truncate {:style "max-width:150px;overflow:hidden;white-space:nowrap"} ((key field) row)])

        [:td.text-center {:style "white-space:nowrap;width:128px;"}
         [:div.d-inline-flex.gap-1
          [:a {:role "button"
               :class "btn"
               :style (str "background: var(--color-primary); color: var(--color-text-on-primary); border-color: var(--color-primary); margin:1px; --bs-btn-padding-y: 0.25rem; --bs-btn-padding-x: 0.5rem; --bs-btn-font-size: 0.875rem; --bs-btn-border-radius: 0.375rem;"
                           (when (= edit false) " opacity: 0.5; pointer-events: none;"))
               :href (str href "/edit/" (:id row))}
           [:i.bi.bi-pencil] " Editar"]
          [:a {:role "button"
               :class "btn confirm"
               :style (str "background: var(--color-danger); color: var(--color-text-on-danger); border-color: var(--color-danger); margin:1px; --bs-btn-padding-y: 0.25rem; --bs-btn-padding-x: 0.5rem; --bs-btn-font-size: 0.875rem; --bs-btn-border-radius: 0.375rem;"
                           (when (= delete false) " opacity: 0.5; pointer-events: none;"))
               :href (str href "/delete/" (:id row))}
           [:i.bi.bi-trash] " Borrar"]]]])]))

(defn build-grid
  [title rows table-id fields href & args]
  [:div.container-fluid
   [:div.row.justify-content-center
    [:div.col-12
     [:div.card.shadow-sm.mb-4 {:style "border-radius: 12px; border: none; background: var(--color-bg-card);"}
      [:div.card-header {:style "background: var(--color-bg-subtle); border-bottom: 1px solid var(--color-border); border-radius: 12px 12px 0 0;"}
       [:h3.text-center.mb-0 {:style "color: var(--color-text);"} title]]
      [:div.card-body
       [:div.table-responsive
        [:table.table.table-sm.w-100 {:id table-id
                                      :data-locale "es-MX"
                                      :data-show-fullscreen "true"
                                      :data-toggle "table"
                                      :data-show-columns "true"
                                      :data-show-toggle "true"
                                      :data-show-print "false"
                                      :data-search "true"
                                      :data-pagination "true"
                                      :data-page-size "5"
                                      :data-page-list "[5,10,25,50,100]"
                                      :data-key-events "true"
                                      :style "color: var(--color-text);"}
         (if (seq args)
           (build-grid-head href fields (first args))
           (build-grid-head href fields))
         (if (seq args)
           (build-grid-body rows href fields (first args))
           (build-grid-body rows href fields))]]]]]]])
;; End build-grid

;; start build-dashboard
(defn build-dashboard-head
  [fields]
  [:thead {:style "background: var(--color-bg-subtle); color: var(--color-text);"}
   [:tr
    [:div.d-inline-flex.gap-1
     (for [field fields]
       [:th {:data-sortable "true"
             :data-field (key field)
             :style "white-space:nowrap;"} (st/upper-case (val field))])]]])

(defn build-dashboard-body
  [rows fields]
  [:tbody
   (for [row rows]
     [:tr
      [:div.d-inline-flex.gap-1
       (for [field fields]
         [:td {:style "white-space:nowrap"} ((key field) row)])]])])

(defn build-dashboard
  [title rows table-id fields]
  [:div.table-responsive
   [:h3 {:style "text-align: center; color: var(--color-primary);"} title]
   [:table.table.table-sm {:style "table-layout:auto;width:100%;"
                           :id table-id
                           :data-virtual-scroll "true"
                           :data-show-export "true"
                           :data-show-fullscreen "true"
                           :data-locale "es-MX"
                           :data-toggle "table"
                           :data-show-columns "true"
                           :data-show-toggle "true"
                           :data-show-print "true"
                           :data-search "true"
                           :data-pagination "true"
                           :data-page-size "5"
                           :data-page-list "[5,10,25,50,100]"
                           :data-key-events "true"}
    (build-dashboard-head fields)
    (build-dashboard-body rows fields)]])
;; End build-dashboard

;; Start build-modal
(defn build-modal
  [title _ form]
  (list
   [:div.modal.fade {:id "exampleModal"
                     :data-bs-backdrop "static"
                     :data-bs-keyboard "false"
                     :tabindex "-1"
                     :aria-labelledby "exampleModalLabel"
                     :aria-hidden "true"}
    [:div.modal-dialog
     [:div.modal-content
      [:div.modal-header
       [:h1.modal-title.fs-5 {:id "exampleModalLabel"} title]
       [:button.btn-close {:type "button"
                           :data-bs-dismiss "modal"
                           :aria-label "Close"}]]

      [:div.modal-body
       form]]]]))
;; End build-modal

(defn modal-script
  []
  [:script
   "
   const myModal = new bootstrap.Modal(document.getElementById('exampleModal'), {
    keyboard: false
   })

   myModal.show();
   "])
;; End build-modal
