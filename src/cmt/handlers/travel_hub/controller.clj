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
				content (view/hub-view request {:groups (model/groups-with-counts)
																				:adventures (model/latest-adventures)})]
		(application request title ok js content)))