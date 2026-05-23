(ns cmt.handlers.comments-moderation.view
	(:require
	 [clojure.string :as str]
	 [cmt.i18n.core :as i18n]
	 [hiccup.util :refer [raw-string]]
	 [ring.util.anti-forgery :refer [anti-forgery-field]]))

(defn- clip [s n]
	(let [t (str/trim (str (or s "")))]
		(if (> (count t) n)
			(str (subs t 0 n) "...")
			t)))

(defn- human-date [v]
  (let [s (str/trim (str (or v "")))]
    (if (str/blank? s)
      "-"
      (try
        (let [n (Long/parseLong s)
            inst (cond
               (>= n 1000000000000) (java.time.Instant/ofEpochMilli n)
               (>= n 1000000000) (java.time.Instant/ofEpochSecond n)
               :else nil)]
          (if inst
            (str (.toLocalDate (java.time.ZonedDateTime/ofInstant inst (java.time.ZoneId/systemDefault))))
            s))
        (catch Exception _ s)))))

(defn- pending-card
  [request {:keys [id aventuras_id nombre commenter_email approved comments fecha aventura cmt_nombre]}]
  [:article.card.shadow-sm.border-0 {:key (str "pc-" id)}
   [:div.card-body
    [:div.d-flex.flex-wrap.align-items-center.justify-content-between.gap-2.mb-2.comments-meta
     [:p.text-muted.small.mb-0.me-2
      (str "#" id " | " (human-date fecha) " | " (or cmt_nombre "-"))]
     (if (= 1 (or approved 0))
       [:span.badge.text-bg-success (i18n/tr request :comments/status-approved)]
       [:span.badge.text-bg-warning (i18n/tr request :comments/status-pending)])]
    [:h2.h6.mb-1 (str (i18n/tr request :blog/adventure-label) " #" aventuras_id)]
    [:p.small.fw-semibold.mb-1 (or nombre "-")]
    [:p.small.text-muted.mb-2 (or commenter_email "-")]
    [:p.mb-3 (clip comments 260)]
    [:p.small.text-muted.mb-3 (clip aventura 180)]
    [:div.row.g-2.mb-3.comments-actions
     [:div.col-12.col-md-auto
      [:a.btn.btn-sm.btn-outline-primary.w-100
       {:href (str "/blog/adventure/" aventuras_id)
        :target "_blank"}
       (i18n/tr request :comments/open-adventure)]]
     (when (not= 1 (or approved 0))
       [:div.col-12.col-md-auto
        [:form {:method "post" :action (str "/admin/comments/" id "/approve")}
         (raw-string (anti-forgery-field))
         [:button.btn.btn-sm.btn-success.w-100 {:type "submit"}
          (i18n/tr request :comments/approve)]]])]
    (when (not (str/blank? (or commenter_email "")))
      [:form.mt-2.comments-reply-form {:method "post" :action (str "/admin/comments/" id "/reply")}
       (raw-string (anti-forgery-field))
       [:textarea.form-control.form-control-sm.mb-2
        {:name "reply"
         :rows 4
         :placeholder (i18n/tr request :comments/reply-placeholder)}]
       [:div.d-grid.d-sm-flex.justify-content-sm-end
        [:button.btn.btn-primary.w-100.w-sm-auto {:type "submit"}
         (i18n/tr request :comments/reply)]]])]])

(defn moderation-view
	[request rows]
  [:section
    [:style ".comments-moderation-shell{max-width:100%;overflow-x:hidden;}
.comments-moderation-shell .card{border-radius:12px;}
  .comments-actions .btn,.comments-reply-form .btn{min-height:42px;}
@media (max-width:576px){
    .comments-header{display:grid;grid-template-columns:1fr;gap:.55rem;}
    .comments-header .btn{width:100%;}
    .comments-meta{display:grid;grid-template-columns:1fr;gap:.3rem;}
    .comments-actions .btn,.comments-reply-form .btn{font-size:.95rem;}
  .comments-moderation-shell .card-body{padding:.9rem;}
}"]
   [:div.comments-moderation-shell
  	 [:div.comments-header.d-flex.justify-content-between.align-items-center.mb-3
	  [:h1.h4.mb-0 (i18n/tr request :comments/moderation-title)]
	  [:a.btn.btn-outline-secondary.btn-sm {:href "/admin/travel-hub"}
	   (i18n/tr request :common/back)]]

	 (if (seq rows)
	   [:div.d-grid.gap-3
		(map (partial pending-card request) rows)]
     [:div.alert.alert-info.mb-0 (i18n/tr request :comments/no-pending)])]])