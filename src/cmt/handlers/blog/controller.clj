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
	  content (view/blog-index-view request data)]
    (application request title ok js content)))

(defn blog-adventure
  [request]
  (let [id (get-in request [:params :id])
	  ok (get-session-id request)]
    (if-let [adventure (model/adventure-by-id id)]
	(let [title (str (i18n/tr request :blog/adventure-label) " " (:id adventure))
		js nil
		content (view/adventure-detail-view request {:adventure adventure
								 :links (model/adventure-links id)})]
	  (application request title ok js content))
	(error-404 (i18n/tr request :blog/adventure-not-found) "/blog"))))

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
										:q q})]
    (application request title ok js content)))

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
									   :q q})]
    (application request title ok js content)))

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
									   :per-page per-page})]
    (application request title ok js content)))

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
										  :per-page per-page})]
    (application request title ok js content)))

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