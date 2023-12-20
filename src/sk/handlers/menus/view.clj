(ns sk.handlers.menus.view
  (:require [sk.handlers.menus.model :refer [build-open-menus-rows
                                             build-aventuras-menus-rows
                                             build-private-admin-admin-menus-rows
                                             build-private-admin-system-menus-rows build-private-menus-rows
                                             build-private-user-admin-menus-rows]]))

;; Start build-aventuras-menus-view
(defn build-aventuras-menus-view []
  (let [rows (build-aventuras-menus-rows)]
    (list
     (map (fn [row]
            (list
             [:a.dropdown-item {:href (str "/aventuras/" (:id row))} (:nombre row)])) rows))))
;; End build-aventuras-menus-view

;; Start build-open-menus-view
(defn build-open-menus [row]
  (list
   [:li.nav-item [:a.nav-link {:href (:link row)} (:description row)]]))

(defn build-open-menus-view []
  (let [rows (build-open-menus-rows)]
    (map build-open-menus rows)))
;; End build-open-menus-view

;; Start build-private-menus-view
(defn build-private-menus [row]
  (list
   [:li.nav-item [:a.nav-link {:href (:link row)} (:description row)]]))

(defn build-private-menus-view []
  (let [rows (build-private-menus-rows)]
    (map build-private-menus rows)))
;; End build-private-menus-view

;; Start build-private-user-admin-menus-view
(defn build-private-user-admin-menus [row]
  (list
   [:a.dropdown-item {:href (:link row)} (:description row)]))

(defn build-private-user-admin-menus-view []
  (let [rows (build-private-user-admin-menus-rows)]
    (map build-private-user-admin-menus rows)))
;; ENd build-private-user-admin-menus-view

;; Start build-private-admin-admin-menus-view
(defn build-private-admin-admin-menus [row]
  (list
   [:a.dropdown-item {:href (:link row)} (:description row)]))

(defn build-private-admin-admin-menus-view []
  (let [rows (build-private-admin-admin-menus-rows)]
    (map build-private-admin-admin-menus rows)))
;; End build-private-admin-admin-menus-view

;; Start build-private-admin-system-menus-view
(defn build-private-admin-system-menus [row]
  (list
   [:a.dropdown-item {:href (:link row)} (:description row)]))

(defn build-private-admin-system-menus-view []
  (let [rows (build-private-admin-system-menus-rows)]
    (map build-private-admin-system-menus rows)))
;; End build-private-admin-system-menus-view

(comment
  (build-private-admin-system-menus-view)
  (build-private-admin-admin-menus-view)
  (build-private-user-admin-menus-view)
  (build-private-menus-view)
  (build-open-menus-view))
