(ns cmt.layout
  (:require
   [clj-time.core :as t]

   [clojure.string :as str]
   [hiccup.page :refer [html5]]
   [cmt.models.crud :refer [config]]
   [cmt.models.util :refer [user-level user-name]]
   [cmt.menu :as menu]
   [cmt.i18n.core :as i18n]))

(defn generate-data-id [href]
  (-> href
      (str/replace #"^/" "")
      (str/replace #"/" "-")
      (str/replace #"[^a-zA-Z0-9\-]" "")
      (str/replace #"^$" "home")))


(defn build-link [request href label & [icon]]
  (let [uri (:uri request)
        data-id (generate-data-id href)
        is-active (= uri href)]
    [:li.nav-item
     [:a.nav-link.fw-semibold.px-3.py-2.rounded.transition
      {:href href
       :data-id data-id
       :class (str (when is-active "active bg-gradient text-primary-emphasis shadow-sm"))
       :aria-current (when is-active "page")
       :onclick "localStorage.setItem('active-link', this.dataset.id)"}
      (when icon [:i.me-2 {:class icon}])
      label]]))


(defn build-dropdown-link [request href label & [icon]]
  (let [uri (:uri request)
        is-active (= uri href)]
    [:li
     [:a.dropdown-item.fw-semibold
      {:href href
       :class (str (when is-active "active bg-primary text-white"))
       :aria-current (when is-active "page")
       :data-id (generate-data-id href)
       :onclick "localStorage.setItem('active-link', this.dataset.id)"}
      (when icon [:i.me-2 {:class icon}])
      label]]))

(defn build-menu [request items]
  (when (some #{(user-level request)} ["A" "S" "U"])
    (for [{:keys [href label role icon]} items
          :when (or (nil? role)
                    (= (user-level request) role)
                    (some #{(user-level request)} ["A" "S"]))]
      (build-dropdown-link request href label icon))))

;; HELPER FUNCTIONS
(defn menu-item->map [[href label & rest]]
  (let [[role-or-icon maybe-icon] rest
        role (when (and role-or-icon (not (clojure.string/starts-with? (str role-or-icon) "bi ")))
               role-or-icon)
        icon (cond
               (and maybe-icon (string? maybe-icon)) maybe-icon
               (and role-or-icon (string? role-or-icon) (clojure.string/starts-with? role-or-icon "bi ")) role-or-icon
               :else nil)]
    (cond-> {:href href :label label}
      role (assoc :role role)
      icon (assoc :icon icon))))

(defn build-dropdown [request dropdown-id data-id label items & [icon]]
  (let [display-label (if (keyword? label)
                        (i18n/tr request label)
                        label)]
    (when (some #{(user-level request)} ["A" "S" "U"])
      [:li.nav-item.dropdown
       [:a.nav-link.dropdown-toggle.fw-semibold.px-3.py-2.rounded.transition
        {:href "#"
         :id dropdown-id
         :data-id data-id
         :onclick "localStorage.setItem('active-link', this.dataset.id)"
         :role "button"
         :data-bs-toggle "dropdown"
         :aria-expanded "false"}
        (when icon [:i.me-2 {:class icon}])
        display-label]
       [:ul.dropdown-menu.shadow-lg.border-0.rounded.mt-2
        {:aria-labelledby dropdown-id
         :style "max-height: 60vh; overflow-y: auto;"}
        (build-menu request items)]])))

(defn create-dropdown [request {:keys [id data-id label items icon]}]
  (let [menu-items (map menu-item->map items)]
    (build-dropdown request id data-id label menu-items icon)))

(defn create-nav-links [request nav-links]
  (map (fn [item]
         (let [{:keys [href label icon]} (menu-item->map item)]
           (build-link request href label icon)))
       nav-links))

(defn brand-logo []
  [:a.navbar-brand.fw-bold.fs-4.d-flex.align-items-center.gap-2 {:href "/"}
   [:img {:src "/images/logo.png"
          :alt (:site-name config)
          :style "width: 44px; height: 44px; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.10);"}]
   [:span.d-none.d-md-inline (:site-name config)]])

(defn logout-button [request]
  [:li.nav-item.dropdown.ms-3
   [:a.btn.btn-primary.btn-sm.px-3.rounded-pill.fw-semibold.dropdown-toggle
    {:href "#"
     :id "userDropdown"
     :role "button"
     :data-bs-toggle "dropdown"
     :aria-expanded "false"}
    [:i.bi.bi-person-circle.me-1]
    (user-name request)]
   [:ul.dropdown-menu.dropdown-menu-end.shadow.border-0
    {:aria-labelledby "userDropdown"}
    [:li
     [:a.dropdown-item.fw-semibold
      {:href "/change/password"}
      [:i.bi.bi-key.me-2]
      (i18n/tr request :auth/change-password)]]
    [:li [:hr.dropdown-divider]]
    [:li
     [:a.dropdown-item.fw-semibold.text-danger
      {:href "/home/logoff"
       :onclick "localStorage.removeItem('active-link')"}
      [:i.bi.bi-box-arrow-right.me-2]
      (i18n/tr request :auth/logout)]]]])

;; THEME SWITCHER
(def theme-options
  [["cerulean" "Cerulean"]
   ["cosmo" "Cosmo"]
   ["cyborg" "Cyborg"]
   ["darkly" "Darkly"]
   ["journal" "Journal"]
   ["litera" "Litera"]
   ["lumen" "Lumen"]
   ["lux" "Lux"]
   ["materia" "Materia"]
   ["minty" "Minty"]
   ["morph" "Morph"]
   ["pulse" "Pulse"]
   ["quartz" "Quartz"]
   ["sandstone" "Sandstone"]
   ["simplex" "Simplex"]
   ["sketchy" "Sketchy"]
   ["slate" "Slate"]
   ["solar" "Solar"]
   ["spacelab" "Spacelab"]
   ["united" "United"]
   ["vapor" "Vapor"]
   ["zephyr" "Zephyr"]
   ["default" "Default"]])

(defn language-selector
  [request]
  (let [current-locale (if-not (nil? request)
                         (i18n/get-locale-from-session (:session request))
                         (i18n/get-locale-from-session request))
        current-flag (if current-locale (i18n/get-locale-flag current-locale) "")]
    [:li.nav-item.dropdown
     [:a.nav-link.dropdown-toggle.fw-semibold.px-3.py-2.rounded.transition
      {:href "#"
       :id "languageDropdown"
       :role "button"
       :data-bs-toggle "dropdown"
       :aria-expanded "false"}
      [:span.me-2 current-flag]
      (i18n/get-locale-name current-locale)]
     [:ul.dropdown-menu.dropdown-menu-end.shadow.border-0
      {:aria-labelledby "languageDropdown"}
      (doall
       (for [[locale info] i18n/supported-locales]
         [:li {:key (name locale)}
          [:a.dropdown-item.d-flex.align-items-center.gap-2
           {:href (str "/set-language/" (name locale))
            :class (when (= locale current-locale) "active")}
           [:span (:flag info)]
           (:name info)]]))]]))

(defn theme-switcher [request]
  [:li.nav-item.dropdown.ms-2
   [:a.nav-link.dropdown-toggle.fw-semibold.px-3.py-2.rounded.transition
    {:href "#"
     :id "themeSwitcher"
     :data-id "theme"
     :role "button"
     :data-bs-toggle "dropdown"
     :aria-expanded "false"}
    [:i.bi.bi-palette-fill.me-1]
    [:span#currentThemeLabel (i18n/tr request :common/theme)]]
   [:ul.dropdown-menu.dropdown-menu-end.shadow-lg.border-0.rounded.mt-2
    {:aria-labelledby "themeSwitcher"}
    (for [[value label] theme-options]
      [:li
       [:a.dropdown-item.theme-option
        {:href "#" :data-theme value :class "fw-semibold"}
        (if (= value "default")
          (i18n/tr request :common/default)
          label)]])]])

;; MENU FUNCTIONS
(defn menus-private [request]
  (let [{:keys [nav-links dropdowns]} (cmt.menu/get-menu-config request)]
    [:nav.navbar.navbar-expand-lg.navbar-dark.bg-gradient.bg-primary.shadow-lg.fixed-top
     {:translate "no"}
     [:div.container-fluid
      (brand-logo)
      [:button.navbar-toggler
       {:type "button"
        :data-bs-toggle "collapse"
        :data-bs-target "#mainNavbar"
        :aria-controls "mainNavbar"
        :aria-expanded "false"
       :aria-label (i18n/tr request :common/toggle-navigation)}
        [:span.navbar-toggler-icon]]
      [:div#mainNavbar.collapse.navbar-collapse
       [:ul.navbar-nav.ms-auto.align-items-lg-center.gap-2
        (create-nav-links request nav-links)
        (doall (for [[_category dropdown] dropdowns]
                 (create-dropdown request dropdown)))
        (theme-switcher request)
        (language-selector request)
        (logout-button request)]]]]))
 
(defn menus-public [request]
  [:nav.navbar.navbar-expand-lg.navbar-dark.bg-primary.shadow.fixed-top
   {:translate "no"}
   [:div.container-fluid
    (brand-logo)
    [:button.navbar-toggler
     {:type "button"
      :data-bs-toggle "collapse"
      :data-bs-target "#mainNavbar"
      :aria-controls "mainNavbar"
      :aria-expanded "false"
      :aria-label (i18n/tr request :common/toggle-navigation)}
     [:span.navbar-toggler-icon]]
    [:div#mainNavbar.collapse.navbar-collapse
     [:ul.navbar-nav.ms-auto.align-items-lg-center.gap-2
      (build-link request "/" (i18n/tr request :menu/home) "bi bi-house")
      (language-selector request)
      (let [base (str/replace (or (:public-url config) (:base-url config) "http://localhost:3000/") #"/$" "")
            page-url (str base (:uri request))
            encoded-url (java.net.URLEncoder/encode page-url "UTF-8")]
        [:li.nav-item.ms-2
         [:a.nav-link.fw-semibold.px-2.py-2.rounded.translate-link
          {:href (str "https://translate.google.com/translate?hl=en&sl=es&tl=en&u=" encoded-url)
           :target "_blank"
           :rel "noopener"
           :title (i18n/tr request :blog/translate-title)}
          [:i.bi.bi-translate.me-1]
          (i18n/tr request :blog/translate)]])
      (theme-switcher request)
      [:li.nav-item.ms-3
       [:a.btn.btn-primary.btn-sm.px-3.rounded-pill.fw-semibold
        {:href "/home/login"}
        [:i.bi.bi-box-arrow-in-right.me-1 {:style "font-size: 0.9rem;"}]
        (i18n/tr request :auth/login)]]]]]])
 
(defn menus-none []
  [:nav.navbar.navbar-expand-lg.navbar-light.bg-white.shadow.fixed-top
   [:div.container-fluid
    (brand-logo)]])

;; ASSETS (CDN)
;; Add themes.css to the CSS includes
(defn app-css []
  (list
   [:link {:rel "stylesheet" :href "/vendor/bootstrap-icons.css"}]
   [:link {:rel "stylesheet" :href "/vendor/dropdown-scroll-fix.css"}]
   [:link {:rel "stylesheet" :href "/css/tabgrid.css?v=5"}]
   [:style ".dropdown-menu .active, .dropdown-menu .active:focus, .dropdown-menu .active:hover { background-color: var(--bs-primary, #0d6efd) !important; color: #fff !important; }
.theme-quartz .dropdown-menu,
.theme-superhero .dropdown-menu,
.theme-darkly .dropdown-menu { background-color: #23272b !important; color: #f8f9fa !important; }
.theme-quartz .dropdown-menu .dropdown-item,
.theme-superhero .dropdown-menu .dropdown-item,
.theme-darkly .dropdown-menu .dropdown-item { color: #f8f9fa !important; }
.theme-quartz .dropdown-menu .dropdown-item:hover,
.theme-superhero .dropdown-menu .dropdown-item:hover,
.theme-darkly .dropdown-menu .dropdown-item:hover { background-color: var(--bs-primary, #0d6efd) !important; color: #fff !important; }
.theme-cyborg .dropdown-menu { background-color: #222 !important; color: #f6f6f6 !important; }
.theme-cyborg .dropdown-menu .dropdown-item { color: #f6f6f6 !important; }
.theme-cyborg .dropdown-menu .dropdown-item:hover { background-color: #0d6efd !important; color: #fff !important; }
.logout-btn { background-color: var(--bs-danger, #dc3545) !important; color: #fff !important; border: none !important; transition: background 0.2s, color 0.2s; }
.logout-btn:hover { background-color: var(--bs-primary, #0d6efd) !important; color: #fff !important; }
.theme-quartz .logout-btn, .theme-superhero .logout-btn, .theme-darkly .logout-btn { background-color: #23272b !important; color: #f8f9fa !important; border-color: #f8f9fa !important; }
.theme-cyborg .logout-btn { background-color: #222 !important; color: #f6f6f6 !important; border-color: #f6f6f6 !important; }
.theme-quartz .logout-btn:hover, .theme-superhero .logout-btn:hover, .theme-darkly .logout-btn:hover, .theme-cyborg .logout-btn:hover { background-color: var(--bs-primary, #0d6efd) !important; color: #fff !important; border-color: var(--bs-primary, #0d6efd) !important; }"]))

(defn theme-js
  "Inline theme.js: ~50 lines of vanilla JS for theme switching, nav highlight, and responsive tables."
  []
  [:script
   (str
    "(function(){
"
    ;; Theme dropdown click handlers — defer until DOM is ready
    "var d=function(f){document.addEventListener('DOMContentLoaded',f);};
"
    "d(function(){
"
    ;; Theme option clicks
    "  document.addEventListener('click',function(e){
"
    "    var t=e.target.closest('.theme-option');
"
    "    if(t){e.preventDefault();localStorage.setItem('theme',t.dataset.theme);location.reload();}
"
    "  });
"
    ;; Active nav link highlight
    "  var a=localStorage.getItem('active-link');
"
    "  if(a){var e=document.querySelector('[data-id=\"'+a+'\"]');
"
    "    if(e){e.classList.add('active');}}
"
    ;; Language selector — highlight current locale
    "  var loc=document.querySelector('#languageDropdown + .dropdown-menu .active');
"
    "  if(!loc){var flag=document.querySelector('#languageDropdown span');
"
    "    if(flag){var txt=flag.textContent.trim();
"
    "      document.querySelectorAll('#languageDropdown + .dropdown-menu a').forEach(function(a){
"
    "        if(a.textContent.trim()===txt){a.classList.add('active');}
"
    "      });}}
"
    "});})()")])


(defn app-scripts [request]
  (list
   [:script {:src "/vendor/bootstrap.bundle.min.js"}]
   (theme-js)
   [:script {:src "/js/fk-dependent.js?v=6"}]
   [:script {:src "/js/md-editor.js?v=1"}]))


;; LAYOUT FUNCTIONS

;; Add theme class to <body> using (:theme config)
(defn application
  ([request title ok js content]
   (application request title ok js {} content))
  ([request title ok js seo content]
   (let [site-url (or (:public-url config) (:base-url config) "http://localhost:3000/")
          site-name (:site-name config)
          seo-desc (:description seo (i18n/tr request :common/seo-default {:site site-name}))
          og-title (:og-title seo title)
          og-desc (:og-description seo seo-desc)
          og-image (:og-image seo (str site-url "/images/logo.png"))
          og-type (:og-type seo "website")
          canonical (:canonical seo (str site-url (:uri request)))
          page-lang (name (i18n/get-locale-from-session (:session request)))]
      (html5
       {:lang page-lang}
      [:head
       [:style ".preload { visibility: hidden; }"]
       [:script
        "document.addEventListener('DOMContentLoaded',function(){"
        "var theme=localStorage.getItem('theme')||'sketchy';"
        "document.body.className = 'preload theme-' + theme;"
        "var themeMap={default:'/vendor/bootstrap.min.css',flatly:'/vendor/bootswatch-flatly.min.css',superhero:'/vendor/bootswatch-superhero.min.css',yeti:'/vendor/bootswatch-yeti.min.css',cerulean:'/vendor/bootswatch-cerulean.min.css',cosmo:'/vendor/bootswatch-cosmo.min.css',cyborg:'/vendor/bootswatch-cyborg.min.css',darkly:'/vendor/bootswatch-darkly.min.css',journal:'/vendor/bootswatch-journal.min.css',litera:'/vendor/bootswatch-litera.min.css',lumen:'/vendor/bootswatch-lumen.min.css',lux:'/vendor/bootswatch-lux.min.css',materia:'/vendor/bootswatch-materia.min.css',minty:'/vendor/bootswatch-minty.min.css',morph:'/vendor/bootswatch-morph.min.css',pulse:'/vendor/bootswatch-pulse.min.css',quartz:'/vendor/bootswatch-quartz.min.css',sandstone:'/vendor/bootswatch-sandstone.min.css',simplex:'/vendor/bootswatch-simplex.min.css',sketchy:'/vendor/bootswatch-sketchy.min.css',slate:'/vendor/bootswatch-slate.min.css',solar:'/vendor/bootswatch-solar.min.css',spacelab:'/vendor/bootswatch-spacelab.min.css',united:'/vendor/bootswatch-united.min.css',vapor:'/vendor/bootswatch-vapor.min.css',zephyr:'/vendor/bootswatch-zephyr.min.css'};"
        "var href=themeMap[theme]||themeMap['default'];"
        "var link=document.getElementById('bootswatch-theme');"
        "if(!link){"
        "  link=document.createElement('link');"
        "  link.rel='stylesheet';"
        "  link.id='bootswatch-theme';"
        "  var firstStyle=document.querySelector('head link[rel=stylesheet], head style');"
        "  if(firstStyle){document.head.insertBefore(link,firstStyle);}else{document.head.appendChild(link);}"
        "}"
        "link.href=href;"
        "link.onload=function(){document.body.classList.remove('preload');};"
        "});"]
       [:meta {:charset "utf-8"}]
       [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
       [:meta {:name "description" :content seo-desc}]
       [:meta {:property "og:title" :content og-title}]
       [:meta {:property "og:description" :content og-desc}]
       [:meta {:property "og:image" :content og-image}]
       [:meta {:property "og:type" :content og-type}]
       [:meta {:property "og:url" :content canonical}]
       [:meta {:property "og:site_name" :content site-name}]
       [:meta {:name "twitter:card" :content "summary_large_image"}]
       [:meta {:name "twitter:title" :content og-title}]
       [:meta {:name "twitter:description" :content og-desc}]
       [:meta {:name "twitter:image" :content og-image}]
       [:link {:rel "canonical" :href canonical}]
       (app-css)
       [:title (str title " | " site-name)]]
      [:body.preload.theme-sketchy
       {:style "display:flex;flex-direction:column;min-height:100vh;overflow-x:hidden;"}
       [:div {:style "flex-shrink:0;height:70px;"}]
       [:div.container-fluid.pt-3
        {:style "flex:1;display:flex;flex-direction:column;min-height:0;overflow:hidden;"}
         (cond
           (= ok -1) (menus-none)
           (= ok 0) (menus-public request)
           (> ok 0) (menus-private request))
        [:div.container-fluid.px-4
         {:style "flex:1;min-height:0;max-height:calc(100vh - 200px);overflow-y:auto;padding-bottom:80px;"}
         (doall content)]]
       (app-scripts request)
       js
       [:footer.bg-light.text-center.fixed-bottom.py-2.shadow-sm
        [:span (str (i18n/tr request :common/copyright) " "
                    (t/year (t/now)) " " (:company-name config) ". "
                    (i18n/tr request :common/all-rights-reserved) ".")]]]))))

(defn error-404
  ([msg] (error-404 msg nil nil))
  ([msg redirect-url] (error-404 msg redirect-url nil))
  ([msg redirect-url request]
   (let [locale (i18n/get-locale-from-session (:session request))
         error-title (i18n/t :common/error-404 locale)
         go-back (i18n/t :common/go-back locale)]
     {:status 404
      :headers {"Content-Type" "text/html; charset=utf-8"}
      :body (html5 {:lang (name locale)}
                   [:div
                    [:h1 error-title]
                    [:p msg]
                    (when redirect-url
                      [:a {:href redirect-url} go-back])])})))
