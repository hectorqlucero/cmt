(ns cmt.handlers.blog.controller
  (:require
 	[clojure.string :as str]
   [cmt.handlers.blog.model :as model]
   [cmt.handlers.blog.view :as view]
 	[cmt.models.email :as email]
 	[cmt.models.crud :as crud]
 	[cmt.i18n.core :as i18n]
   [cmt.layout :refer [application error-404]]
 	[cmt.models.util :refer [get-session-id]]
 	[ring.util.response :refer [redirect]]))

(defn- site-url []
  (let [u (or (:public-url crud/config) (:base-url crud/config) "http://localhost:3000/")]
    (if (.endsWith u "/") (subs u 0 (dec (count u))) u)))

(defn- feed-locale [request]
  (i18n/get-locale-from-session (:session request)))

(defn- seo-for-page [request {:keys [title description og-image og-type canonical]}]
  (let [base (site-url)]
    (merge
     {:description (or description (i18n/tr request :blog/hero-subtitle))
      :og-image (str base "/images/logo.png")
      :og-type (or og-type "website")}
     (when canonical {:canonical (str base canonical)})
     (when og-image {:og-image og-image})
     (when title {:og-title title}))))

(defn- parse-page [request]
  (let [raw (or (get-in request [:params "page"]) (get-in request [:params :page]) "1")
		parsed (try (Integer/parseInt (str/trim (str raw))) (catch Exception _ 1))]
	 (max 1 parsed)))

(defn- parse-per-page [request]
	(let [raw (or (get-in request [:params "per-page"]) (get-in request [:params :per-page]) "12")
		parsed (try (Integer/parseInt (str/trim (str raw))) (catch Exception _ 12))
		allowed #{12 24 48}]
		(if (allowed parsed) parsed 12)))

(defn- parse-q [request]
	(let [raw (or (get-in request [:params "q"]) (get-in request [:params :q]) "")
		q (str/trim (str raw))]
		(when-not (str/blank? q) q)))

(defn blog-index
  [request]
  (let [title (i18n/tr request :blog/title)
	  ok (get-session-id request)
	  js nil
	  data {:adventures (model/latest-adventures)
		  :videos (model/latest-videos)
		  :photos (model/latest-photos)
		  :workshops (model/featured-workshops)}
	  content (view/blog-index-view request data)
	  seo (seo-for-page request {:title title
                               :description (i18n/tr request :blog/hero-subtitle)
                               :og-type "website"})]
    (application request title ok js seo content)))

(defn blog-adventure
  [request]
  (let [id (get-in request [:params :id])
	  ok (get-session-id request)]
    (if-let [adventure (model/adventure-by-id id)]
	(let [title (str (i18n/tr request :blog/adventure-label) " " (:id adventure))
		js nil
		content (view/adventure-detail-view request {:adventure adventure
								 :links (model/adventure-links id)})
		seo (seo-for-page request {:title (str (:aventura adventure) " - " title)
                               :description (-> (:aventura adventure) (str/replace #"\n" " ") (subs 0 (min 200 (count (or (:aventura adventure) "")))))
                               :og-type "article"
                               :canonical (str "/blog/adventure/" id)})]
	  (application request title ok js seo content))
	(error-404 (i18n/tr request :blog/adventure-not-found) "/blog" request))))

(defn blog-stories
  [request]
  (let [title (i18n/tr request :blog/stories-page-title)
	  ok (get-session-id request)
	  js nil
	  per-page (parse-per-page request)
	  q (parse-q request)
	  total (model/adventures-count q)
	  total-pages (max 1 (int (Math/ceil (/ (double total) per-page))))
	  page (min (parse-page request) total-pages)
	  offset (* (dec page) per-page)
	  content (view/stories-list-view request {:adventures (model/paged-adventures per-page offset q)
										:page page
										:total total
										:total-pages total-pages
										:per-page per-page
										:q q})
	  seo (seo-for-page request {:title title
                               :description (i18n/tr request :blog/stories-page-subtitle)
                               :og-type "website"})]
    (application request title ok js seo content)))

(defn blog-videos
  [request]
  (let [title (i18n/tr request :blog/videos-page-title)
	  ok (get-session-id request)
	  js nil
	  per-page (parse-per-page request)
	  q (parse-q request)
	  total (model/videos-count q)
	  total-pages (max 1 (int (Math/ceil (/ (double total) per-page))))
	  page (min (parse-page request) total-pages)
	  offset (* (dec page) per-page)
	  content (view/videos-list-view request {:videos (model/paged-videos per-page offset q)
									   :page page
									   :total total
									   :total-pages total-pages
									   :per-page per-page
									   :q q})
	  seo (seo-for-page request {:title title
                               :description (i18n/tr request :blog/videos-page-subtitle)
                               :og-type "website"})]
    (application request title ok js seo content)))

(defn blog-photos
  [request]
  (let [title (i18n/tr request :blog/photos-page-title)
	  ok (get-session-id request)
	  js nil
	  per-page (parse-per-page request)
	  total (model/photos-count)
	  total-pages (max 1 (int (Math/ceil (/ (double total) per-page))))
	  page (min (parse-page request) total-pages)
	  offset (* (dec page) per-page)
	  content (view/photos-list-view request {:photos (model/paged-photos per-page offset)
									   :page page
									   :total total
									   :total-pages total-pages
									   :per-page per-page})
	  seo (seo-for-page request {:title title
                               :description (i18n/tr request :blog/photos-page-subtitle)
                               :og-type "website"})]
    (application request title ok js seo content)))

(defn blog-workshops
  [request]
  (let [title (i18n/tr request :blog/workshops-page-title)
	  ok (get-session-id request)
	  js nil
	  per-page (parse-per-page request)
	  total (model/workshops-count)
	  total-pages (max 1 (int (Math/ceil (/ (double total) per-page))))
	  page (min (parse-page request) total-pages)
	  offset (* (dec page) per-page)
	  content (view/workshops-list-view request {:workshops (model/paged-workshops per-page offset)
										  :page page
										  :total total
										  :total-pages total-pages
										  :per-page per-page})
	  seo (seo-for-page request {:title title
                               :description (i18n/tr request :blog/workshops-page-subtitle)
                               :og-type "website"})]
    (application request title ok js seo content)))

(defn blog-add-comment
  [request]
  (let [id (get-in request [:params :id])
		params (:params request)
		author (str/trim (str (or (get params :author) (get params "author") "")))
		email-from (str/trim (str (or (get params :email) (get params "email") "")))
		comment (str/trim (str (or (get params :comment) (get params "comment") "")))
			ok? (and (not (str/blank? author)) (not (str/blank? comment)))]
    (if-not ok?
	  (-> (redirect (str "/blog/adventure/" id "?msg=invalid"))
		  (assoc :flash {:blog-comment-status :invalid}))
	  (let [saved? (model/add-adventure-comment! {:adventure-id id :author author :comment comment :email email-from})]
		(when saved?
		  (email/send-blog-comment-notification
		   {:adventure-id id
			:author author
			:email email-from
			:comment comment
			:link (str (or (:public-url crud/config)
					   (:base-url crud/config)
					   "http://localhost:3000/")
				   "blog/adventure/" id)}))
		(-> (redirect (str "/blog/adventure/" id (if saved? "?msg=ok" "?msg=error")))
			(assoc :flash {:blog-comment-status (if saved? :ok :error)}))))))

(defn blog-rss
  [request]
  (let [adventures (model/latest-adventures 50)
        base (site-url)
        locale (feed-locale request)
        now-str (fn [] (.format java.time.format.DateTimeFormatter/ISO_INSTANT (java.time.Instant/now)))
        escape-xml (fn [s] (-> (str s)
                               (str/replace "&" "&amp;")
                               (str/replace "<" "&lt;")
                               (str/replace ">" "&gt;")
                               (str/replace "'" "&apos;")
                               (str/replace "\"" "&quot;")))]
    {:status 200
     :headers {"Content-Type" "application/atom+xml; charset=utf-8"}
     :body (str "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                "<feed xmlns=\"http://www.w3.org/2005/Atom\">"
                "<title>" (escape-xml (:site-name crud/config)) "</title>"
                "<link href=\"" base "/feed.xml\" rel=\"self\"/>"
                "<link href=\"" base "/\" rel=\"alternate\"/>"
                "<id>" base "/</id>"
                "<updated>" (now-str) "</updated>"
                (apply str
                       (for [{:keys [id aventura fecha leader_email]} adventures]
                         (let [entry-id (str base "/blog/adventure/" id)
                               summary (if aventura (subs (str/replace aventura #"\n" " ") 0 (min 500 (count aventura))) "")]
                            (str "<entry>"
                                 "<id>" entry-id "</id>"
                                 "<title>" (escape-xml (i18n/t :entity/aventuras locale)) " #" id "</title>"
                                 "<link href=\"" entry-id "\"/>"
                                 "<summary>" (escape-xml summary) "</summary>"
                                 "<author><name>" (escape-xml (or leader_email (i18n/t :blog/rss-author locale))) "</name></author>"
                                 "</entry>"))))
                "</feed>")}))

(defn sitemap
  [_request]
  (let [adventures (model/all-adventures)
        base (site-url)]
    {:status 200
     :headers {"Content-Type" "application/xml; charset=utf-8"}
     :body (str "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">"
                "<url><loc>" base "/</loc><priority>1.0</priority></url>"
                "<url><loc>" base "/blog</loc><priority>0.9</priority></url>"
                "<url><loc>" base "/blog/stories</loc><priority>0.8</priority></url>"
                "<url><loc>" base "/blog/videos</loc><priority>0.6</priority></url>"
                "<url><loc>" base "/blog/photos</loc><priority>0.6</priority></url>"
                "<url><loc>" base "/blog/workshops</loc><priority>0.6</priority></url>"
                (apply str
                       (for [{:keys [id]} adventures]
                         (str "<url><loc>" base "/blog/adventure/" id "</loc><priority>0.7</priority></url>")))
                "</urlset>")}))