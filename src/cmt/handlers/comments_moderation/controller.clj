(ns cmt.handlers.comments-moderation.controller
  (:require
   [clojure.string :as str]
   [cmt.handlers.comments-moderation.model :as model]
   [cmt.handlers.comments-moderation.view :as view]
   [cmt.layout :refer [application]]
   [cmt.i18n.core :as i18n]
   [cmt.models.crud :as crud]
   [cmt.models.email :as email]
   [cmt.models.util :refer [get-session-id]]
   [ring.util.response :refer [redirect]]))

(defn- comment-link [id]
  (str (or (:public-url crud/config)
       (:base-url crud/config)
       "http://localhost:3000/")
    "blog/adventure/" id))

(defn- commenter-email [comment]
  (let [mail (str/trim (str (or (:commenter_email comment) "")))]
    (when-not (str/blank? mail) mail)))

(defn- notify-commenter-approved! [comment]
  (when-let [to (commenter-email comment)]
    (email/send-blog-comment-approved
     {:adventure-id (:aventuras_id comment)
      :author (:nombre comment)
      :email to
      :comment (:comments comment)
      :link (comment-link (:aventuras_id comment))})))

(defn- notify-commenter-reply! [comment reply]
  (when-let [to (commenter-email comment)]
    (email/send-blog-comment-reply
     {:adventure-id (:aventuras_id comment)
      :author (:nombre comment)
      :email to
      :reply reply
      :link (comment-link (:aventuras_id comment))})))

(defn pending
	[request]
	(let [title (i18n/tr request :comments/moderation-title)
				ok (get-session-id request)
				js nil
				content (view/moderation-view request (model/pending-comments))]
		(application request title ok js content)))

(defn approve
  [request]
  (let [id (get-in request [:params :id])
        comment (model/comment-by-id id)
        was-pending? (not= 1 (or (:approved comment) 0))
        approved? (model/approve-comment! id)]
    (when (and approved? was-pending? comment)
      (notify-commenter-approved! comment))
    (redirect "/admin/comments/pending")))

(defn reply
  [request]
  (let [id (get-in request [:params :id])
        reply (str/trim (str (or (get-in request [:params :reply])
                                 (get-in request [:params "reply"]) "")))
        comment (model/comment-by-id id)]
    (when (and comment (not (str/blank? reply)))
      (when (model/add-reply-comment! {:adventure-id (:aventuras_id comment)
                                       :reply-text reply
                                       :responder-name "Administrador"})
        (notify-commenter-reply! comment reply)))
    (redirect "/admin/comments/pending")))
