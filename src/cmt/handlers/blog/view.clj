(ns cmt.handlers.blog.view
	(:require
	 [clojure.string :as str]
	 [cmt.i18n.core :as i18n]
	 [hiccup.util :refer [raw-string]]
	 [ring.util.anti-forgery :refer [anti-forgery-field]]))

(defn- format-fecha
	"Convert Unix timestamp (ms) to readable date string in Spanish."
	[timestamp-str]
	(if-let [ms (try (Long/parseLong (str/trim (str timestamp-str))) (catch Exception _ nil))]
		(try
			(let [instant (java.time.Instant/ofEpochMilli ms)
				  zdt (-> instant (.atZone (java.time.ZoneId/of "UTC")))
				  date (.toLocalDate zdt)
				  formatter (java.time.format.DateTimeFormatter/ofPattern "d 'de' MMMM 'de' yyyy" (java.util.Locale. "es"))]
				(.format date formatter))
			(catch Exception _ "N/A"))
		"N/A"))

(defn- shorten
	[text max-len]
	(let [s (str/trim (str (or text "")))]
		(if (<= (count s) max-len)
			s
			(str (subs s 0 max-len) "..."))))

(defn- selected-cover
	[]
	(let [day (.getDayOfYear (java.time.LocalDate/now))]
		(if (even? day)
			{:url "/images/blog-cover-cinematic.svg"}
			{:url "/images/blog-cover-creative.svg"})))

(defn- stat-chip [label value href]
		[:a.blog-stat {:href href}
		 [:span.blog-stat-label label]
		 [:span.blog-stat-value value]
		 [:span.blog-stat-arrow "→"]])

(defn- card-title
	[request {:keys [id aventura]}]
	(let [raw (str/trim (str (or aventura "")))]
		(if (str/blank? raw)
			(str (i18n/tr request :blog/ride-label) " #" id)
			(shorten raw 58))))

(defn- list-page-shell
	([request title subtitle body]
	 (list-page-shell request title subtitle body (i18n/tr request :blog/stories-label)))
	([request title subtitle body breadcrumb-label]
	 (list
	  [:style
		"@import url('https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@400;500;700&family=Fraunces:opsz,wght@9..144,600;9..144,700&display=swap');
.blog-list-shell{font-family:'Space Grotesk',sans-serif;background:linear-gradient(180deg,#f6f9fc 0%,#eef3f8 100%);border-radius:24px;padding:16px;}
.blog-list-canvas{background:#fff;border-radius:18px;padding:22px;box-shadow:0 14px 30px rgba(14,32,52,.08);}
.blog-list-head{margin-bottom:1rem;padding:1rem 1.1rem;border-radius:14px;background:linear-gradient(120deg,#0a2f4a 0%,#0c5f86 60%,#25a5a0 100%);color:#e9f8ff;}
.blog-list-title{font-family:'Fraunces',serif;font-size:clamp(1.7rem,3.2vw,2.4rem);line-height:1.08;margin:.2rem 0 .4rem 0;color:#f6fdff;}
.blog-list-subtitle{margin:0;max-width:64ch;color:#d0ebf8;}
.blog-list-back{display:inline-flex;align-items:center;gap:.35rem;text-decoration:none;font-weight:700;color:#0d5679;margin-bottom:1rem;}
.blog-item-card{border-radius:14px;border:1px solid #e3edf5;background:#fff;box-shadow:0 8px 18px rgba(16,40,62,.07);}
.blog-item-card .card-body{padding:1rem 1.05rem;}
.blog-item-meta{font-size:.75rem;letter-spacing:.07em;text-transform:uppercase;font-weight:700;color:#58718a;margin-bottom:.35rem;}
.blog-item-title{font-size:1.06rem;font-weight:700;color:#152c40;margin-bottom:.4rem;}
.blog-item-copy{color:#42586a;line-height:1.62;margin-bottom:.85rem;}
.blog-item-actions{display:flex;flex-wrap:wrap;gap:.45rem;}
.blog-empty{padding:1rem 1.1rem;border:1px dashed #c6d8e7;border-radius:12px;background:#f9fcff;color:#5f7688;}
@media (max-width:576px){.blog-list-shell{padding:10px;}.blog-list-canvas{padding:14px;}.blog-list-head{padding:.85rem .9rem;}}"]
	 [:section.blog-list-shell
		[:div.blog-list-canvas
		 [:nav {:aria-label "breadcrumb"}
			[:ol.breadcrumb.mb-3.bg-transparent
				[:li.breadcrumb-item [:a {:href "/blog"} (i18n/tr request :blog/blog-label)]]
				[:li.breadcrumb-item.active breadcrumb-label]]]
		 [:div.blog-list-head
			[:h1.blog-list-title title]
			[:p.blog-list-subtitle subtitle]]
		 body]])))

(defn- enc [v]
	(java.net.URLEncoder/encode (str v) "UTF-8"))

(defn- query-string [params]
	(->> params
		 (remove (fn [[_ v]] (or (nil? v) (and (string? v) (str/blank? v)))))
		 (map (fn [[k v]] (str (enc (name k)) "=" (enc v))))
		 (str/join "&")))

(defn- with-query [base-url params]
	(let [qs (query-string params)]
		(if (str/blank? qs)
			base-url
			(str base-url "?" qs))))

(defn- list-controls
	[request {:keys [base-url per-page q search?]}]
	[:div.d-flex.flex-wrap.justify-content-between.align-items-center.gap-2.mb-3
	 [:div.d-flex.flex-wrap.align-items-center.gap-2
	  (when search?
		[:form.d-flex.gap-2 {:method "get" :action base-url}
		 [:input.form-control.form-control-sm
		  {:type "text"
		   :name "q"
		   :value (or q "")
		   :placeholder (i18n/tr request :blog/search-placeholder)}]
		 [:input {:type "hidden" :name "per-page" :value (str per-page)}]
		 [:button.btn.btn-sm.btn-dark {:type "submit"}
		  (i18n/tr request :common/search)]])]
	 [:form.d-flex.align-items-center.gap-2 {:method "get" :action base-url}
	  (when (and search? q)
		[:input {:type "hidden" :name "q" :value q}])
	  [:label.small.text-muted.mb-0 {:for (str "per-page-" (subs base-url 6))}
	   (i18n/tr request :grid/per-page)]
	  [:select.form-select.form-select-sm
	   {:id (str "per-page-" (subs base-url 6))
		:name "per-page"
		:onchange "this.form.submit()"}
	   (for [opt [12 24 48]]
		 [:option {:key (str "pp-" opt) :value opt :selected (= opt per-page)} opt])]]])

(defn- pager
	[request base-url page total total-pages label {:keys [per-page q]}]
	[:div.d-flex.flex-wrap.justify-content-between.align-items-center.gap-2.mb-3
	 [:p.mb-0.blog-dim
	  (str (i18n/tr request :blog/page-label) " " page " " (i18n/tr request :grid/of) " " total-pages
		   " • " total " " label)]
	 [:div.d-flex.gap-2
	  (when (> page 1)
		[:a.btn.btn-sm.btn-outline-dark.rounded-pill.px-3
		 {:href (with-query base-url {:page (dec page) :per-page per-page :q q})}
		 (i18n/tr request :common/previous)])
	  (when (< page total-pages)
		[:a.btn.btn-sm.btn-dark.rounded-pill.px-3
		 {:href (with-query base-url {:page (inc page) :per-page per-page :q q})}
		 (i18n/tr request :common/next)])]])

(defn blog-index-view
	[request {:keys [adventures videos photos workshops]}]
	(let [cover (selected-cover)]
				(list
				 [:style
					"@import url('https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@400;500;700&family=Fraunces:opsz,wght@9..144,600;9..144,700&display=swap');
.blog-home-shell{font-family:'Space Grotesk',sans-serif;background:linear-gradient(180deg,#f4f8fc 0%,#eef3f8 100%);border-radius:28px;padding:16px;}
.blog-home-canvas{background:#ffffff;border-radius:24px;padding:22px;box-shadow:0 22px 44px rgba(9,26,47,.08);}
.blog-home-hero{border-radius:24px;overflow:hidden;background:linear-gradient(120deg,#08283f 0%,#0b4f73 54%,#20a0a2 100%);color:#eef9fc;position:relative;isolation:isolate;}
.blog-home-hero:before{content:'';position:absolute;inset:auto -120px -140px auto;width:320px;height:320px;border-radius:50%;background:rgba(255,255,255,.08);}
.blog-home-copy{padding:30px 28px 30px 30px;display:flex;flex-direction:column;justify-content:center;min-height:320px;}
.blog-home-kicker{font-size:.72rem;letter-spacing:.14em;text-transform:uppercase;font-weight:700;opacity:.84;margin-bottom:.4rem;}
.blog-home-title{font-family:'Fraunces',serif;font-size:clamp(2.2rem,4vw,3.6rem);line-height:1.02;margin:0 0 .7rem 0;color:#f7fdff;max-width:12ch;}
.blog-home-subtitle{max-width:56ch;font-size:1.02rem;line-height:1.65;color:#d5edf7;margin:0 0 1rem 0;}
.blog-home-actions{display:flex;flex-wrap:wrap;gap:.6rem;}
.blog-home-visual{padding:18px 18px 18px 0;display:flex;align-items:stretch;height:100%;}
.blog-home-frame{width:100%;min-height:320px;border-radius:22px;overflow:hidden;box-shadow:0 20px 38px rgba(5,20,34,.3);background:#0a2031;}
.blog-home-image{width:100%;height:100%;min-height:320px;object-fit:cover;display:block;}
.blog-stat{display:flex;flex-direction:column;gap:.18rem;min-width:0;text-decoration:none;padding:.92rem .95rem .85rem;border-radius:16px;border:1px solid rgba(255,255,255,.14);background:rgba(255,255,255,.1);backdrop-filter:blur(8px);color:#f5fbfe;box-shadow:0 10px 20px rgba(2,18,31,.12);transition:transform .18s ease,background .18s ease,box-shadow .18s ease;flex:1 1 12rem;}
.blog-stat:hover{transform:translateY(-2px);background:rgba(255,255,255,.16);box-shadow:0 14px 24px rgba(2,18,31,.16);color:#fff;}
.blog-stat-label{font-size:.7rem;letter-spacing:.12em;text-transform:uppercase;font-weight:700;opacity:.82;}
.blog-stat-value{font-size:1.12rem;font-weight:700;line-height:1.1;}
.blog-stat-arrow{margin-top:.08rem;font-size:1rem;opacity:.9;}
@media (max-width:991.98px){.blog-home-copy{padding:26px 22px 0 22px;min-height:auto;}.blog-home-visual{padding:0 22px 22px 22px;}.blog-home-frame,.blog-home-image{min-height:240px;}.blog-stat{flex-basis:calc(50% - .3rem);}}
@media (max-width:575.98px){.blog-home-shell{padding:10px;}.blog-home-canvas{padding:14px;}.blog-home-copy{padding:18px 16px 0 16px;}.blog-home-visual{padding:0 16px 16px 16px;}.blog-home-title{max-width:none;}.blog-stat{flex-basis:100%;}.blog-home-frame,.blog-home-image{min-height:200px;}}"]
				 [:section.blog-home-shell.mb-5
				  [:div.blog-home-canvas
				   [:div.blog-home-hero
				    [:div.row.g-0.align-items-stretch
				     [:div.col-lg-7
				      [:div.blog-home-copy
				       [:p.blog-home-kicker (i18n/tr request :blog/hero-kicker)]
				       [:h1.blog-home-title (i18n/tr request :blog/hero-title)]
				       [:p.blog-home-subtitle (i18n/tr request :blog/hero-subtitle)]
				       [:div.blog-home-actions
				        (stat-chip (i18n/tr request :blog/latest-ride-stories) (count adventures) "/blog/stories")
				        (stat-chip (i18n/tr request :blog/recent-videos) (count videos) "/blog/videos")
				        (stat-chip (i18n/tr request :blog/photo-links) (count photos) "/blog/photos")
				        (stat-chip (i18n/tr request :blog/trusted-workshops) (count workshops) "/blog/workshops")]]]
				     [:div.col-lg-5
				      [:div.blog-home-visual
				       [:div.blog-home-frame
				        [:img.blog-home-image {:src (:url cover) :alt ""}]]]]]]]])))
(defn stories-list-view
	[request {:keys [adventures page total total-pages per-page q]}]
	(list-page-shell
	 request
	 (i18n/tr request :blog/stories-page-title)
	 (i18n/tr request :blog/stories-page-subtitle)
	 (if (seq adventures)
		 [:div
			(list-controls request {:base-url "/blog/stories" :per-page per-page :q q :search? true})
			(pager request "/blog/stories" page total total-pages (i18n/tr request :blog/latest-ride-stories)
					 {:per-page per-page :q q})
			[:div.row.row-cols-1.row-cols-md-2.g-3
			 (for [{:keys [id aventura fecha cmt_nombre enlace enlacev leader_email]} adventures]
				[:div.col {:key (str "story-" id)}
				 [:article.blog-item-card.card.h-100.border-0
					[:div.card-body
					 [:p.blog-item-meta
						(str (format-fecha fecha)
							 "  •  "
							 (or cmt_nombre (i18n/tr request :blog/open-route)))]
					 [:h2.blog-item-title (card-title request {:id id :aventura aventura})]
					 (when (and leader_email (not (str/blank? leader_email)))
						[:p.blog-item-meta (str (i18n/tr request :blog/author-label) ": " leader_email)])
					 [:p.blog-item-copy (shorten aventura 260)]
					 [:div.blog-item-actions
						[:a.btn.btn-sm.btn-dark.rounded-pill.px-3 {:href (str "/blog/adventure/" id)}
						 (i18n/tr request :blog/read-full-story)]
						(when (and enlace (not (str/blank? enlace)))
							[:a.btn.btn-sm.btn-outline-secondary.rounded-pill.px-3
							 {:href enlace :target "_blank" :rel "noopener noreferrer"}
							 (i18n/tr request :blog/open-photo-link)])
						(when (and enlacev (not (str/blank? enlacev)))
							[:a.btn.btn-sm.btn-outline-secondary.rounded-pill.px-3
							 {:href enlacev :target "_blank" :rel "noopener noreferrer"}
							 (i18n/tr request :blog/open-video)])]]]])]]
		 [:div.blog-empty (i18n/tr request :blog/no-story-text)])))

(defn videos-list-view
	[request {:keys [videos page total total-pages per-page q]}]
	(list-page-shell
	 request
	 (i18n/tr request :blog/videos-page-title)
	 (i18n/tr request :blog/videos-page-subtitle)
	 (if (seq videos)
		[:div
		 (list-controls request {:base-url "/blog/videos" :per-page per-page :q q :search? true})
		 (pager request "/blog/videos" page total total-pages (i18n/tr request :blog/recent-videos)
				{:per-page per-page :q q})
		 [:div.d-grid.gap-2
		  (for [{:keys [id fecha titulo enlace]} videos]
			[:article.blog-item-card.card.border-0 {:key (str "video-" id)}
			 [:div.card-body
				[:p.blog-item-meta (format-fecha fecha)]
				[:h2.blog-item-title (or titulo (i18n/tr request :blog/video-label))]
				[:div.blog-item-actions
				 [:a.btn.btn-sm.btn-dark.rounded-pill.px-3
				  {:href (or enlace "#") :target "_blank" :rel "noopener noreferrer"}
				  (i18n/tr request :blog/open-video)]]]])]]
		[:div.blog-empty (i18n/tr request :blog/no-videos)])
	 (i18n/tr request :blog/videos-label)))

(defn photos-list-view
	[request {:keys [photos page total total-pages per-page]}]
	(list-page-shell
	 request
	 (i18n/tr request :blog/photos-page-title)
	 (i18n/tr request :blog/photos-page-subtitle)
	 (if (seq photos)
		[:div
		 (list-controls request {:base-url "/blog/photos" :per-page per-page :search? false})
		 (pager request "/blog/photos" page total total-pages (i18n/tr request :blog/photo-links)
				{:per-page per-page})
		 [:div.row.row-cols-1.row-cols-md-2.row-cols-lg-3.g-3
		  (for [{:keys [id fecha enlace]} photos]
			[:div.col {:key (str "photo-" id)}
			 [:article.blog-item-card.card.h-100.border-0
				[:div.card-body
				 [:p.blog-item-meta (format-fecha fecha)]
				 [:h2.blog-item-title (str (i18n/tr request :blog/photo-label) " #" id)]
				 [:div.blog-item-actions
					[:a.btn.btn-sm.btn-dark.rounded-pill.px-3
					 {:href (or enlace "#") :target "_blank" :rel "noopener noreferrer"}
					 (i18n/tr request :blog/open-photo-link)]]]]])]]
		[:div.blog-empty (i18n/tr request :blog/no-photos)])
	 (i18n/tr request :blog/photos-label)))

(defn workshops-list-view
	[request {:keys [workshops page total total-pages per-page]}]
	(list-page-shell
	 request
	 (i18n/tr request :blog/workshops-page-title)
	 (i18n/tr request :blog/workshops-page-subtitle)
	 (if (seq workshops)
		[:div
		 (list-controls request {:base-url "/blog/workshops" :per-page per-page :search? false})
		 (pager request "/blog/workshops" page total total-pages (i18n/tr request :blog/trusted-workshops)
				{:per-page per-page})
		 [:div.d-grid.gap-2
		  (for [{:keys [id nombre direccion telefono historia sitio]} workshops]
			[:article.blog-item-card.card.border-0 {:key (str "workshop-" id)}
			 [:div.card-body
				[:p.blog-item-meta (str (i18n/tr request :blog/workshop-label) " #" id)]
				[:h2.blog-item-title (or nombre (i18n/tr request :blog/workshop-label))]
				[:p.blog-item-copy (str (or direccion (i18n/tr request :blog/address-unavailable))
										 " | "
										 (or telefono (i18n/tr request :blog/no-phone)))]
				[:p.blog-item-copy (shorten historia 220)]
				(when (and sitio (not (str/blank? sitio)))
					[:div.blog-item-actions
					 [:a.btn.btn-sm.btn-dark.rounded-pill.px-3
					  {:href sitio :target "_blank" :rel "noopener noreferrer"}
					  (i18n/tr request :blog/visit-site)]])]])]]
		[:div.blog-empty (i18n/tr request :blog/no-workshops)])
	 (i18n/tr request :blog/workshops-label)))

(defn adventure-detail-view
	[request {:keys [adventure links]}]
	(let [{:keys [id aventura fecha cmt_nombre leader_email enlace enlacev]} adventure
			msg (or (get-in request [:params :msg]) (name (or (get-in request [:flash :blog-comment-status]) "")))]
		(list
		 [:style
			"@import url('https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@400;500;700&family=Fraunces:opsz,wght@9..144,600;9..144,700&display=swap');
.ride-shell{font-family:'Space Grotesk',sans-serif;background:linear-gradient(180deg,#f4f8fc 0%,#f9fbfd 100%);border-radius:24px;padding:16px;}
.ride-canvas{background:#fff;border-radius:20px;padding:24px;box-shadow:0 18px 36px rgba(10,30,50,.09);}
.ride-banner{border-radius:16px;background:linear-gradient(120deg,#08283f 0%,#0f5a7f 52%,#27a6a0 100%);padding:28px;color:#e8faff;}
.ride-title{font-family:'Fraunces',serif;line-height:1.1;font-size:clamp(1.9rem,3vw,2.8rem);color:#f4fcff;margin-bottom:.45rem;}
.ride-meta{font-size:.9rem;color:#cae8f4;}
.ride-story-card{border-radius:14px;background:#fbfdff;box-shadow:0 10px 20px rgba(19,40,60,.08);}
.ride-story-copy{white-space:pre-wrap;line-height:1.8;color:#2d4253;}
.ride-link{font-weight:700;text-decoration:none;color:#0b5e82;}
.ride-link:hover{color:#084866;}
.ride-list .list-group-item{border-color:#e7eef5;padding:1rem 1.1rem;}
.ride-list-title{font-weight:700;color:#14314a;}
.ride-list-copy{margin:0;color:#5a6f80;line-height:1.6;}
@media (max-width:576px){.ride-shell{padding:10px;}.ride-canvas{padding:15px;}.ride-banner{padding:22px 16px;}}"]

		 [:nav {:aria-label "breadcrumb"}
			[:ol.breadcrumb.mb-4.bg-transparent
				[:li.breadcrumb-item [:a {:href "/blog"} (i18n/tr request :blog/blog-label)]]
				[:li.breadcrumb-item [:a {:href "/blog/stories"} (i18n/tr request :blog/stories-label)]]
				[:li.breadcrumb-item.active (str (i18n/tr request :blog/adventure-label) " #" id)]]]

		 [:article.ride-shell
			[:div.ride-canvas
			 [:div.ride-banner.mb-4
				[:p.text-uppercase.mb-2.fw-bold {:style "letter-spacing:.1em;font-size:.73rem;"}
				 (i18n/tr request :blog/ride-story)]
				[:h1.ride-title (str (i18n/tr request :blog/adventure-label) " #" id)]
				[:p.ride-meta.mb-0
				 (str (i18n/tr request :blog/date-label) ": " (format-fecha fecha)
						"  |  "
						(i18n/tr request :blog/group-label) ": " (or cmt_nombre (i18n/tr request :blog/open-route)))]]

			 [:div.ride-story-card.card.border-0.mb-4
				[:div.card-body.p-4
				 [:p.ride-story-copy.mb-0
					(or aventura (i18n/tr request :blog/no-story-text))]
				 [:div.d-flex.flex-wrap.gap-3.mt-3
					(when (and enlace (not (str/blank? enlace)))
						[:a.ride-link {:href enlace :target "_blank" :rel "noopener noreferrer"}
						 (i18n/tr request :blog/primary-link)])
					(when (and enlacev (not (str/blank? enlacev)))
						[:a.ride-link {:href enlacev :target "_blank" :rel "noopener noreferrer"}
						 (i18n/tr request :blog/video-extra-link)])
					(when (and leader_email (not (str/blank? leader_email)))
						[:span.badge.rounded-pill.text-bg-light.px-3.py-2
						 (str (i18n/tr request :blog/leader-label) ": " leader_email)])]]]

			 [:section
				[:h2.h5.fw-bold.mb-3 (i18n/tr request :blog/ride-notes-links)]
				(if (seq links)
					[:div.ride-list.list-group.shadow-sm
					 (for [{:keys [id nombre comments]} links]
						[:div.list-group-item {:key (str "lnk-" id)}
						 [:p.ride-list-title.mb-1 (or nombre (i18n/tr request :blog/reference-label))]
						 [:p.ride-list-copy (or comments "")]])]
					[:p.text-muted (i18n/tr request :blog/no-related-notes)])]

			[:section.mt-4
				[:h2.h5.fw-bold.mb-3 (i18n/tr request :blog/comments-title)]
				(when (= msg "ok")
					[:div.alert.alert-success (i18n/tr request :blog/comment-sent)])
				(when (or (= msg "invalid") (= msg "error"))
					[:div.alert.alert-warning (i18n/tr request :blog/comment-invalid)])
				[:form.row.g-2 {:method "post" :action (str "/blog/adventure/" id "/comment")}
				 (raw-string (anti-forgery-field))
				 [:div.col-12.col-md-4
					[:input.form-control {:type "text"
											:name "author"
											:placeholder (i18n/tr request :blog/comment-author-placeholder)
											:required true}]]
				 [:div.col-12.col-md-4
					[:input.form-control {:type "email"
											:name "email"
											:placeholder (i18n/tr request :blog/comment-email-placeholder)}]]
				 [:div.col-12
					[:textarea.form-control {:name "comment"
												:rows 4
												:placeholder (i18n/tr request :blog/comment-placeholder)
												:required true}]]
				 [:div.col-12
					[:button.btn.btn-dark.rounded-pill.px-4 {:type "submit"}
					 (i18n/tr request :blog/comment-submit)]]]]]

			 [:nav {:aria-label "breadcrumb"}
				[:ol.breadcrumb.mt-4.bg-transparent
					[:li.breadcrumb-item [:a {:href "/blog"} (i18n/tr request :blog/blog-label)]]
					[:li.breadcrumb-item [:a {:href "/blog/stories"} (i18n/tr request :blog/stories-label)]]
				[:li.breadcrumb-item.active (str (i18n/tr request :blog/adventure-label) " #" id)]]]])))