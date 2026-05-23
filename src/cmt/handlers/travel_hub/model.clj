(ns cmt.handlers.travel-hub.model
  (:require
   [cmt.models.crud :refer [db Query]]))

(defn groups-with-counts
  []
  (Query db
	  ["SELECT c.id,
		    c.nombre,
		    c.comments,
		    COUNT(DISTINCT a.id) AS aventuras_count,
		    COUNT(al.id) AS enlaces_count
	    FROM cmt c
	    LEFT JOIN aventuras a ON a.cmt_id = c.id
	    LEFT JOIN aventuras_link al ON al.aventuras_id = a.id
	    GROUP BY c.id, c.nombre, c.comments
	    ORDER BY c.id DESC"]))

(defn latest-adventures
  ([] (latest-adventures 20))
  ([limit]
   (Query db
	   ["SELECT a.id,
		     a.fecha,
		     a.aventura,
		     a.cmt_id,
		     c.nombre AS cmt_nombre,
		     COUNT(al.id) AS enlaces_count
	     FROM aventuras a
	     LEFT JOIN cmt c ON c.id = a.cmt_id
	     LEFT JOIN aventuras_link al ON al.aventuras_id = a.id
	     GROUP BY a.id, a.fecha, a.aventura, a.cmt_id, c.nombre
	     ORDER BY a.fecha DESC, a.id DESC
	     LIMIT ?" limit])))