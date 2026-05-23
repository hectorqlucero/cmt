(ns cmt.handlers.blog.model
	(:require
	 [clojure.string :as str]
	 [cmt.models.crud :refer [db Query Query!]]))

(defn- parse-id [value]
	(try
		(Long/parseLong (str value))
		(catch Exception _ nil)))

(defn- safe-query
	"Runs a query and returns [] on failure so public pages keep rendering."
	[sql]
	(try
		(Query db sql)
		(catch Exception _ [])))

(defn latest-adventures
	([] (latest-adventures 9))
	([limit]
	 (safe-query
		["SELECT a.id,
						 a.aventura,
						 a.fecha,
						 a.leader_email,
						 a.enlace,
						 a.enlacev,
						 c.nombre AS cmt_nombre
			FROM aventuras a
			LEFT JOIN cmt c ON c.id = a.cmt_id
			ORDER BY a.fecha DESC, a.id DESC
			LIMIT ?" limit])))

(defn all-adventures
	[]
	(safe-query
	 ["SELECT a.id,
						 a.aventura,
						 a.fecha,
						 a.leader_email,
						 a.enlace,
						 a.enlacev,
						 c.nombre AS cmt_nombre
		FROM aventuras a
		LEFT JOIN cmt c ON c.id = a.cmt_id
		ORDER BY a.fecha DESC, a.id DESC"]))

(defn adventures-count
	([ ] (adventures-count nil))
	([q]
	 (let [q* (some-> q str str/trim)]
		 (or
		  (some->
		   (first
			(if (and q* (not (str/blank? q*)))
			  (safe-query
			   ["SELECT COUNT(*) AS total
				 FROM aventuras
				 WHERE UPPER(COALESCE(aventura,'')) LIKE UPPER(?)"
				(str "%" q* "%")])
			  (safe-query ["SELECT COUNT(*) AS total FROM aventuras"])))
		   :total)
		 0))))

(defn paged-adventures
	([limit offset] (paged-adventures limit offset nil))
	([limit offset q]
	 (let [q* (some-> q str str/trim)]
		 (if (and q* (not (str/blank? q*)))
			(safe-query
			 ["SELECT a.id,
							 a.aventura,
							 a.fecha,
							 a.leader_email,
							 a.enlace,
							 a.enlacev,
							 c.nombre AS cmt_nombre
				FROM aventuras a
				LEFT JOIN cmt c ON c.id = a.cmt_id
				WHERE UPPER(COALESCE(a.aventura,'')) LIKE UPPER(?)
				ORDER BY a.fecha DESC, a.id DESC
				LIMIT ? OFFSET ?" (str "%" q* "%") limit offset])
			(safe-query
			 ["SELECT a.id,
							 a.aventura,
							 a.fecha,
							 a.leader_email,
							 a.enlace,
							 a.enlacev,
							 c.nombre AS cmt_nombre
				FROM aventuras a
				LEFT JOIN cmt c ON c.id = a.cmt_id
				ORDER BY a.fecha DESC, a.id DESC
				LIMIT ? OFFSET ?" limit offset])))))

(defn adventure-by-id [id]
	(let [parsed-id (parse-id id)]
		(when parsed-id
			(first
			 (safe-query
				["SELECT a.id,
								 a.aventura,
								 a.fecha,
								 a.leader_email,
								 a.enlace,
								 a.enlacev,
								 c.nombre AS cmt_nombre
					FROM aventuras a
					LEFT JOIN cmt c ON c.id = a.cmt_id
					WHERE a.id = ?" parsed-id])))))

(defn adventure-links [id]
	(let [parsed-id (parse-id id)]
		(if parsed-id
			(or
			 (try
				(Query db
					   ["SELECT id, nombre, comments, commenter_email
						  FROM aventuras_link
						  WHERE aventuras_id = ?
						  AND COALESCE(approved, 1) = 1
						  ORDER BY id DESC" parsed-id])
				(catch Exception _ nil))
			 (safe-query
			  ["SELECT id, nombre, comments, NULL AS commenter_email
				  FROM aventuras_link
				  WHERE aventuras_id = ?
				  ORDER BY id DESC" parsed-id]))
			[])))

(defn add-adventure-comment!
	[{:keys [adventure-id author comment email]}]
	(let [aid (parse-id adventure-id)
			name (str/trim (str (or author "")))
			mail (str/trim (str (or email "")))
			text (str/trim (str (or comment "")))]
		(when (and aid (not (str/blank? name)) (not (str/blank? text)))
			(try
				(Query! db ["INSERT INTO aventuras_link (aventuras_id, nombre, comments, commenter_email, approved) VALUES (?, ?, ?, ?, ?)"
							 aid name text (when-not (str/blank? mail) mail) false])
				true
				(catch Exception _
					(try
						(Query! db ["INSERT INTO aventuras_link (aventuras_id, nombre, comments) VALUES (?, ?, ?)"
								 aid name text])
						true
						(catch Exception _ false)))))))

(defn latest-videos
	([] (latest-videos 6))
	([limit]
	 (safe-query
		["SELECT id, fecha, titulo, enlace
			FROM videos
			ORDER BY fecha DESC, id DESC
			LIMIT ?" limit])))

(defn all-videos
	[]
	(safe-query
	 ["SELECT id, fecha, titulo, enlace
		FROM videos
		ORDER BY fecha DESC, id DESC"]))

(defn videos-count
	([ ] (videos-count nil))
	([q]
	 (let [q* (some-> q str str/trim)]
		 (or
		  (some->
		   (first
			(if (and q* (not (str/blank? q*)))
			  (safe-query
			   ["SELECT COUNT(*) AS total
				 FROM videos
				 WHERE UPPER(COALESCE(titulo,'')) LIKE UPPER(?)"
				(str "%" q* "%")])
			  (safe-query ["SELECT COUNT(*) AS total FROM videos"])))
		   :total)
		 0))))

(defn paged-videos
	([limit offset] (paged-videos limit offset nil))
	([limit offset q]
	 (let [q* (some-> q str str/trim)]
		 (if (and q* (not (str/blank? q*)))
			(safe-query
			 ["SELECT id, fecha, titulo, enlace
				FROM videos
				WHERE UPPER(COALESCE(titulo,'')) LIKE UPPER(?)
				ORDER BY fecha DESC, id DESC
				LIMIT ? OFFSET ?" (str "%" q* "%") limit offset])
			(safe-query
			 ["SELECT id, fecha, titulo, enlace
				FROM videos
				ORDER BY fecha DESC, id DESC
				LIMIT ? OFFSET ?" limit offset])))))

(defn latest-photos
	([] (latest-photos 10))
	([limit]
	 (safe-query
		["SELECT id, fecha, enlace
			FROM fotos
			ORDER BY fecha DESC, id DESC
			LIMIT ?" limit])))

(defn all-photos
	[]
	(safe-query
	 ["SELECT id, fecha, enlace
		FROM fotos
		ORDER BY fecha DESC, id DESC"]))

(defn photos-count
	[]
	(or (some-> (first (safe-query ["SELECT COUNT(*) AS total FROM fotos"])) :total)
		0))

(defn paged-photos
	[limit offset]
	(safe-query
	 ["SELECT id, fecha, enlace
		FROM fotos
		ORDER BY fecha DESC, id DESC
		LIMIT ? OFFSET ?" limit offset]))

(defn featured-workshops
	([] (featured-workshops 4))
	([limit]
	 (safe-query
		["SELECT id, nombre, direccion, telefono, sitio, historia
			FROM talleres
			ORDER BY id DESC
			LIMIT ?" limit])))

(defn all-workshops
	[]
	(safe-query
	 ["SELECT id, nombre, direccion, telefono, sitio, historia
		FROM talleres
		ORDER BY id DESC"]))

(defn workshops-count
	[]
	(or (some-> (first (safe-query ["SELECT COUNT(*) AS total FROM talleres"])) :total)
		0))

(defn paged-workshops
	[limit offset]
	(safe-query
	 ["SELECT id, nombre, direccion, telefono, sitio, historia
		FROM talleres
		ORDER BY id DESC
		LIMIT ? OFFSET ?" limit offset]))