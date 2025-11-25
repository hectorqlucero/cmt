(ns sk.layout
  (:require [clj-time.core :as t]
            [hiccup.page :refer [html5 include-css include-js]]
            [sk.models.util :refer [user-level
                                    user-name
                                    build-turismo
                                    build-mturismo
                                    build-ciudad]]
            [sk.migrations :refer [config]]))

(defn build-admin []
  (list
   nil
   (when (or
          (= (user-level) "A")
          (= (user-level) "S"))
     (list
      [:li [:a.dropdown-item {:href "/admin/aventuras"} "Aventuras"]]
      [:li [:a.dropdown-item {:href "/admin/cmt"} "CMT"]]
      [:li [:a.dropdown-item {:href "/admin/fotos"} "Fotos"]]
      [:li [:a.dropdown-item {:href "/admin/videos"} "Videos"]]
      [:li [:a.dropdown-item {:href "/admin/talleres"} "Talleres"]]
      (when (= (user-level) "S")
        [:li [:a.dropdown-item {:href "/admin/users"} "Usuarios"]])))))

(defn menus-private []
  (list
   [:nav.navbar.navbar-expand-lg.fixed-top {:style "background: rgba(var(--color-primary-rgb), 0.06); backdrop-filter: saturate(180%) blur(12px); border-bottom: 1px solid var(--color-border); box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04), 0 1px 2px rgba(0, 0, 0, 0.06);"}
    [:div.container-fluid
     [:a.navbar-brand {:href "#"}
      [:img {:src "/images/logo.png"
             :alt (:site-name config)
             :style "width:40px;height:40px;"}]]
     [:button.navbar-toggler {:type "button"
                              :data-bs-toggle "collapse"
                              :data-bs-target "#collapsibleNavbar"
                              :aria-controls "collapsibleNavbar"
                              :aria-expanded "false"
                              :aria-label "Toggle navigation"}
      [:span.navbar-toggler-icon]]
     [:div#collapsibleNavbar.collapse.navbar-collapse
      [:ul.navbar-nav.ms-auto
       [:li.nav-item
        [:a.nav-link.active {:aria-current "page"
                             :href "/"} [:i.fas.fa-home.me-2] "Inicio"]]
       [:li.nav-item.dropdown
        [:a.nav-link.dropdown-toggle {:href "#"
                                      :id "navdrop0"
                                      :role "button"
                                      :data-bs-toggle "dropdown"
                                      :aria-expanded "false"} [:i.fas.fa-bicycle.me-2] "Cicloturismo"]
        [:ul.dropdown-menu {:aria-labelledby "navdrop0"}
         (build-turismo)]]
       [:li.nav-item.dropdown
        [:a.nav-link.dropdown-toggle {:href "#"
                                      :id "navdrop1"
                                      :role "button"
                                      :data-bs-toggle "dropdown"
                                      :aria-expanded "false"} [:i.fas.fa-motorcycle.me-2] "Mototurismo"]
        [:ul.dropdown-menu {:aria-labelledby "navdrop1"}
         (build-mturismo)]]
       [:li.nav-item.dropdown
        [:a.nav-link.dropdown-toggle {:href "#"
                                      :id "navdrop2"
                                      :role "button"
                                      :data-bs-toggle "dropdown"
                                      :aria-expanded "false"} [:i.fas.fa-route.me-2] "Rodadas por la ciudad"]
        [:ul.dropdown-menu {:aria-labelledby "navdrop2"}
         (build-ciudad)]]
       [:li.nav-item [:a.nav-link {:href "/fotos/list"} [:i.fas.fa-camera.me-2] "Fotos"]]
       [:li.nav-item [:a.nav-link {:href "/videos/list"} [:i.fas.fa-video.me-2] "Videos"]]
       [:li.nav-item [:a.nav-link {:href "/talleres/list"} [:i.fas.fa-tools.me-2] "Talleres"]]
       (when (or (= (user-level) "U") (= (user-level) "A") (= (user-level) "S"))
         [:li.nav-item.dropdown
          [:a.nav-link.dropdown-toggle {:href "#"
                                        :id "navdrop3"
                                        :data-bs-toggle "dropdown"} [:i.fas.fa-cog.me-2] "Administrar"]
          [:ul.dropdown-menu {:aria-labelledby "navdrop3"}
           (build-admin)]])
       [:li.nav-item [:a.nav-link {:href "/home/logoff"} [:i.fas.fa-sign-out-alt.me-2] (str "Salir [" (user-name) "]")]]]]]]))

(defn menus-public []
  (list
   [:nav.navbar.navbar-expand-lg.fixed-top {:style "background: rgba(var(--color-primary-rgb), 0.06); backdrop-filter: saturate(180%) blur(12px); border-bottom: 1px solid var(--color-border); box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04), 0 1px 2px rgba(0, 0, 0, 0.06);"}
    [:div.container-fluid
     [:a.navbar-brand {:href "#"}
      [:img {:src "/images/logo.png"
             :alt (:site-name config)
             :style "width:40px;height:40px;"}]]
     [:button.navbar-toggler {:type "button"
                              :data-bs-toggle "collapse"
                              :data-bs-target "#collapsibleNavbar"
                              :aria-controls "collapsibleNavbar"
                              :aria-expanded "false"
                              :aria-label "Toggle navigation"}
      [:span.navbar-toggler-icon]]
     [:div#collapsibleNavbar.collapse.navbar-collapse
      [:ul.navbar-nav.ms-auto
       [:li.nav-item
        [:a.nav-link.active {:aria-current "page"
                             :href "/"} [:i.fas.fa-home.me-2] "Inicio"]]
       [:li.nav-item.dropdown
        [:a.nav-link.dropdown-toggle {:href "#"
                                      :id "navdrop0"
                                      :role "button"
                                      :data-bs-toggle "dropdown"
                                      :aria-expanded "false"} [:i.fas.fa-bicycle.me-2] "Cicloturismo"]
        [:ul.dropdown-menu {:aria-labelledby "navdrop0"}
         (build-turismo)]]
       [:li.nav-item.dropdown
        [:a.nav-link.dropdown-toggle {:href "#"
                                      :id "navdrop1"
                                      :role "button"
                                      :data-bs-toggle "dropdown"
                                      :aria-expanded "false"} [:i.fas.fa-motorcycle.me-2] "Mototurismo"]
        [:ul.dropdown-menu {:aria-labelledby "navdrop1"}
         (build-mturismo)]]
       [:li.nav-item.dropdown
        [:a.nav-link.dropdown-toggle {:href "#"
                                      :id "navdrop2"
                                      :role "button"
                                      :data-bs-toggle "dropdown"
                                      :aria-expanded "false"} [:i.fas.fa-route.me-2] "Rodadas por la ciudad"]
        [:ul.dropdown-menu {:aria-labelledby "navdrop2"}
         (build-ciudad)]]
       [:li.nav-item [:a.nav-link {:href "/fotos/list"} [:i.fas.fa-camera.me-2] "Fotos"]]
       [:li.nav-item [:a.nav-link {:href "/videos/list"} [:i.fas.fa-video.me-2] "Videos"]]
       [:li.nav-item [:a.nav-link {:href "/talleres/list"} [:i.fas.fa-tools.me-2] "Talleres"]]
       [:li.nav-item [:a.nav-link {:href "/home/login"
                                   :aria-current "page"} [:i.fas.fa-sign-in-alt.me-2] "Entrar al sitio"]]
       [:li.nav-item [:button#theme-toggle.btn.btn-outline-secondary {:type "button"} [:i.bi.bi-moon-fill] " Tema"]]]]]]))

(defn menus-none []
  (list
   [:nav.navbar.navbar-expand-lg.fixed-top {:style "background: rgba(var(--color-primary-rgb), 0.06); backdrop-filter: saturate(180%) blur(12px); border-bottom: 1px solid var(--color-border); box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04), 0 1px 2px rgba(0, 0, 0, 0.06);"}
    [:a.navbar-brand {:href "#"}
     [:img.rounded-circle {:src "/images/logo.png"
                           :alt (:site-name config)
                           :style "width:40px;"}]]
    [:button.navbar-toggler {:type "button"
                             :data-bs-toggle "collapse"
                             :data-bs-target "#collapsibleNavbar"
                             :aria-expanded "false"
                             :aria-label "Toggle navigation"}
     [:span.navbar-toggler-icon]]
    [:div#collapsibleNavbar.collapse.navbar-collapse]]))

(defn app-css []
  (list
   (include-css "/bootstrap5/css/bootstrap.min.css")
   (include-css "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css")
   (include-css "/bootstrap-icons/font/bootstrap-icons.css")
   (include-css "/bootstrap-table-master/dist/bootstrap-table.min.css")
   (include-css "/css/extra.css")))

(defn app-js []
  (list
   (include-js "/js/jquery.min.js")
   (include-js "/bootstrap5/js/bootstrap.bundle.min.js")
   (include-js "/bootstrap-table-master/dist/extensions/export/tableExport.min.js")
   (include-js "/bootstrap-table-master/dist/extensions/export/jspdf.umd.min.js")
   (include-js "/bootstrap-table-master/dist/extensions/export/bootstrap-table.min.js")
   (include-js "/bootstrap-table-master/dist/extensions/export/bootstrap-table-export.min.js")
   (include-js "/bootstrap-table-master/dist/extensions/export/bootstrap-table-print.min.js")
   (include-js "/bootstrap-table-master/dist/locale/bootstrap-table-es-MX.min.js")
   (include-js "/js/extra.js")))

(defn application [title ok js & content]
  (html5 {:ng-app (:site-name config) :lang "en"}
         [:head
          [:title (if title
                    title
                    (:site-name config))]
          [:meta {:charset "UTF-8"}]
          [:meta {:name "viewport"
                  :content "width=device-width, initial-scale=1"}]
          (app-css)
          [:link {:rel "shortcut icon"
                  :type "image/x-icon"
                  :href "data:image/x-icon;,"}]]
         [:body
          [:div.container.flex-nowrap.overflow-auto.margin-top {:style "margin-top:75px;margin-bottom:25px;"}
           (cond
             (= ok -1) (menus-none)
             (= ok 0) (menus-public)
             (> ok 0) (menus-private))
           [:div {:style "padding-left:14px;"} content]]
          (app-js)
          js
          [:footer {:style "background: var(--color-surface); border-top: 1px solid var(--color-border); padding: 6px 0; box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.04); font-size: 0.75rem; letter-spacing: .5px; color: var(--color-text-muted); text-align: center; position: fixed; bottom: 0; width: 100%;"}
           [:span  "Copyright &copy;" (t/year (t/now)) " " (:company-name config) " - All Rights Reserved"]]]))

(defn error-404 [content return-url]
  (html5 {:ng-app (:site-name config) :lang "es"}
         [:head
          [:title "Mesaje"]
          [:meta {:charset "UTF-8"}]
          [:meta {:name "viewport"
                  :content "width=device-width, initial-scale=1"}]
          (app-css)
          [:link {:rel "shortcut iconcompojure"
                  :type "image/x-icon"
                  :href "data:image/x-icon;,"}]]
         [:body
          [:div.container.flex-nowrap.overflow-auto.margin-top {:style "margin-top:75px;margin-bottom:25px;"}
           (menus-none)
           [:div {:style "padding-left:14px;"}
            [:div
             [:p [:h3 [:b "Mensaje: "]] [:h3 content]]
             [:p [:h3 [:a {:href return-url} "Clic aqui para " [:strong "Continuar"]]]]]]]

          (app-js)
          nil
          [:footer {:style "background: var(--color-surface); border-top: 1px solid var(--color-border); padding: 6px 0; box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.04); font-size: 0.75rem; letter-spacing: .5px; color: var(--color-text-muted); text-align: center; position: fixed; bottom: 0; width: 100%;"}
           [:span  "Copyright &copy;" (t/year (t/now)) " " (:company-name config) " - All Rights Reserved"]]]))
