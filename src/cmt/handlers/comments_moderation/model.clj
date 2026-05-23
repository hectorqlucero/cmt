(ns cmt.handlers.comments-moderation.model
	(:require
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

(defn pending-comments
	[]
	(try
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
						 ORDER BY COALESCE(al.approved, 0) ASC, al.id DESC
						 LIMIT 150"])
		(catch Exception _ [])))

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