(ns cmt.handlers.travel-hub.view
  (:require
   [clojure.string :as str]
   [cmt.i18n.core :as i18n]))

(defn- clip [s n]
  (let [t (str/trim (str (or s "")))]
    (if (> (count t) n)
      (str (subs t 0 n) "...")
      t)))

(defn- parse-epoch-ms [fecha]
  (cond
    (number? fecha) fecha
    (string? fecha) (try
                      (Long/parseLong (str/trim fecha))
                      (catch Exception _ nil))
    :else nil))

(defn- group-item [request {:keys [id nombre comments aventuras_count enlaces_count]}]
  [:div.list-group-item {:key (str "g-" id)}
   [:div.d-flex.justify-content-between.align-items-start.gap-2
    [:div
     [:p.fw-semibold.mb-1 (or nombre (str (i18n/tr request :travel-hub/group-label) " #" id))]
     [:p.text-muted.small.mb-1 (clip comments 80)]
     [:p.small.mb-0
      (str (i18n/tr request :travel-hub/adventures-count) ": " aventuras_count " | "
           (i18n/tr request :travel-hub/links-count) ": " enlaces_count)]]
    [:div.text-nowrap
     [:a.btn.btn-sm.btn-outline-primary.me-2
      {:href (str "/admin/cmt?id=" id)}
      (i18n/tr request :travel-hub/open-group-board)]
     [:a.btn.btn-sm.btn-primary
      {:href (str "/admin/aventuras/add-form/" id "?parent_entity=cmt")
       :class "travelhub-modal-add"
       :data-modal-title (i18n/tr request :travel-hub/new-adventure)}
      (i18n/tr request :travel-hub/new-adventure)]]]])

(defn- adventure-item [request {:keys [id fecha aventura cmt_nombre cmt_id enlaces_count]}]
  [:div.list-group-item {:key (str "a-" id)}
   [:div.d-flex.justify-content-between.align-items-start.gap-2
    [:div
     [:p.fw-semibold.mb-1 (clip aventura 90)]
     [:p.small.text-muted.mb-1
      (or
       (when fecha
         (let [ts (parse-epoch-ms fecha)]
           (when (and ts (>= ts 1000000000000))
             (let [instant (java.time.Instant/ofEpochMilli ts)
                   zdt (-> instant (.atZone (java.time.ZoneId/of "UTC")))
                   formatter (java.time.format.DateTimeFormatter/ofPattern "d 'de' MMMM 'de' yyyy" (java.util.Locale. "es"))]
               (.format formatter zdt)))))
       "-")
      " | "
      (or cmt_nombre (str (i18n/tr request :travel-hub/group-label) " #" cmt_id))]
     [:p.small.mb-0 (str (i18n/tr request :travel-hub/links-count) ": " enlaces_count)]]
    [:div.text-nowrap
     [:a.btn.btn-sm.btn-outline-primary.me-2
      {:href (str "/admin/aventuras?id=" id)}
      (i18n/tr request :travel-hub/open-adventure)]
     [:a.btn.btn-sm.btn-primary
      {:href (str "/admin/aventuras_link/add-form/" id "?parent_entity=aventuras")
       :class "travelhub-modal-add"
       :data-modal-title (i18n/tr request :travel-hub/new-link)}
      (i18n/tr request :travel-hub/new-link)]]]])

(defn hub-view [request {:keys [groups adventures]}]
  (list
   [:section.mb-4
    [:div.rounded-4.p-4.shadow-sm
     {:style "background: linear-gradient(110deg,#0e4d79 0%,#2b79a8 60%,#66a9ce 100%); color: #eaf6ff;"}
     [:p.text-uppercase.mb-2.fw-bold {:style "letter-spacing:.08em;"}
      (i18n/tr request :travel-hub/kicker)]
     [:h1.h3.mb-2 (i18n/tr request :travel-hub/title)]
     [:p.mb-0.opacity-75 (i18n/tr request :travel-hub/subtitle)]]]

   [:section.row.g-4
    [:div.col-12.col-lg-6
     [:div.card.border-0.shadow-sm.h-100
      [:div.card-header.bg-white.fw-bold (i18n/tr request :travel-hub/groups-section)]
      [:div.card-body
       (if (seq groups)
          [:div.list-group (map (partial group-item request) groups)]
         [:p.text-muted.mb-0 (i18n/tr request :travel-hub/no-groups)])]]]

    [:div.col-12.col-lg-6
     [:div.card.border-0.shadow-sm.h-100
      [:div.card-header.bg-white.fw-bold (i18n/tr request :travel-hub/adventures-section)]
      [:div.card-body
       (if (seq adventures)
          [:div.list-group (map (partial adventure-item request) adventures)]
               [:p.text-muted.mb-0 (i18n/tr request :travel-hub/no-adventures)])]]]]

             [:script
              "(function(){\n"
              "  if (window.__travelHubModalBound) return;\n"
              "  window.__travelHubModalBound = true;\n"
              "  document.addEventListener('click', function(e){\n"
              "    var link = e.target.closest('a.travelhub-modal-add');\n"
              "    if (!link) return;\n"
              "    e.preventDefault();\n"
              "    var modalEl = document.getElementById('exampleModal');\n"
              "    if (!modalEl) return;\n"
              "    var modalBody = modalEl.querySelector('.modal-body');\n"
              "    var modalTitle = document.getElementById('exampleModalLabel');\n"
              "    if (modalTitle) modalTitle.textContent = link.getAttribute('data-modal-title') || 'Nuevo';\n"
              "    if (modalBody) modalBody.innerHTML = '<div class=\"p-4 text-center text-muted\">Cargando...</div>';\n"
              "    fetch(link.href, {credentials: 'same-origin'})\n"
              "      .then(function(r){ return r.text(); })\n"
              "      .then(function(html){\n"
              "        if (modalBody) modalBody.innerHTML = html;\n"
              "        var modal = bootstrap.Modal.getOrCreateInstance(modalEl);\n"
              "        modal.show();\n"
              "      })\n"
              "      .catch(function(){\n"
              "        if (modalBody) modalBody.innerHTML = '<div class=\"p-4 text-danger\">No se pudo cargar el formulario.</div>';\n"
              "        var modal = bootstrap.Modal.getOrCreateInstance(modalEl);\n"
              "        modal.show();\n"
              "      });\n"
              "  });\n"
               "})();"]))