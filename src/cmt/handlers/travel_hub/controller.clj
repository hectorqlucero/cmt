(ns cmt.handlers.travel-hub.controller
	(:require
	 [cmt.handlers.travel-hub.model :as model]
	 [cmt.handlers.travel-hub.view :as view]
	 [cmt.layout :refer [application]]
	 [cmt.i18n.core :as i18n]
	 [cmt.models.util :refer [get-session-id]]))

(defn main
	[request]
	(let [title (i18n/tr request :travel-hub/title)
				ok (get-session-id request)
				js nil
				stats {:total-adventures (model/total-adventures)
				       :total-groups (model/total-groups)
				       :total-videos (model/total-videos)
				       :total-photos (model/total-photos)
				       :total-shops (model/total-shops)
				       :pending-comments (model/pending-comments-count)}
				content (view/hub-view request {:stats stats
				                                :groups (model/groups-with-counts)
																			  :adventures (model/latest-adventures)})]
		(application request title ok js content)))