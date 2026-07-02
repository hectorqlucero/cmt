(ns cmt.handlers.comments-moderation.model
	(:require
   [clojure.string :as str]
	 [cmt.models.crud :refer [db Query Query!]]))

(defn comment-by-id
  [id]
  (try
    (first
     (Query db
            ["SELECT al.id,
                    al.aventuras_id,
                    al.nombre,
                    al.commenter_email,
										al.approved,
                    al.comments,
			      a.fecha,
			      a.aventura,
                    c.nombre AS cmt_nombre
             FROM aventuras_link al
             LEFT JOIN aventuras a ON a.id = al.aventuras_id
             LEFT JOIN cmt c ON c.id = a.cmt_id
             WHERE al.id = ?"
             id]))
    (catch Exception _ nil)))

(defn- build-where
  [{:keys [status q]}]
  (let [conditions (atom ["1=1"])
        params (atom [])]
    (when q
      (let [like (str "%" q "%")]
        (swap! conditions conj "(UPPER(COALESCE(al.nombre,'')) LIKE UPPER(?) OR UPPER(COALESCE(al.commenter_email,'')) LIKE UPPER(?))")
        (swap! params into [like like])))
    (when (= status "pending")
      (swap! conditions conj "(COALESCE(al.approved,0) != 1)"))
    (when (= status "approved")
      (swap! conditions conj "(al.approved = 1)"))
    [(str/join " AND " @conditions) @params]))

(defn pending-comments
  ([] (pending-comments {}))
  ([{:keys [status q page per-page]}]
   (let [page (max 1 (or page 1))
         per-page (max 10 (min 100 (or per-page 50)))
         [where-clause params] (build-where {:status status :q q})]
      (try
       (Query db
              (into [(str "SELECT al.id, al.aventuras_id, al.nombre, al.commenter_email, al.approved, al.comments, a.fecha, a.aventura, c.nombre AS cmt_nombre"
                          " FROM aventuras_link al"
                          " LEFT JOIN aventuras a ON a.id = al.aventuras_id"
                          " LEFT JOIN cmt c ON c.id = a.cmt_id"
                          " WHERE " where-clause
                          " ORDER BY COALESCE(al.approved,0) ASC, al.id DESC"
                          " LIMIT ? OFFSET ?")]
                    (into params [per-page (* (dec page) per-page)])))
        (catch Exception _ [])))))

(defn pending-comments-count
  ([] (pending-comments-count {}))
  ([{:keys [status q]}]
   (let [[where-clause params] (build-where {:status status :q q})]
      (try
        (or (some-> (first (Query db
                                   (into [(str "SELECT COUNT(*) AS total"
                                               " FROM aventuras_link al"
                                               " LEFT JOIN aventuras a ON a.id = al.aventuras_id"
                                               " WHERE " where-clause)]
                                         params)))
                    :total)
            0)
        (catch Exception _ 0)))))

(defn approve-comment!
	[id]
	(try
		(Query! db ["UPDATE aventuras_link SET approved = 1 WHERE id = ?" id])
		true
		(catch Exception _ false)))

(defn add-reply-comment!
	[{:keys [adventure-id reply-text responder-name]}]
	(let [name (or responder-name "Administrador")]
		(try
			(Query! db ["INSERT INTO aventuras_link (aventuras_id, nombre, comments, approved)
						 VALUES (?, ?, ?, 1)"
					 adventure-id name reply-text])
			true
			(catch Exception _ false))))