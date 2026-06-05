(ns cmt.hooks.users
  (:require [clojure.string :as str]
            [cmt.models.util :refer [image-link]]
            [buddy.hashers :as hashers]
            [cmt.models.email :as email]))

(defn before-load [params]
  params)

(defn after-load [rows _params]
  (map #(-> %
            (assoc :imagen (image-link (:imagen %)))) rows))

(defn- new-record? [params]
  (let [id (:id params)]
    (or (nil? id)
        (and (string? id) (str/blank? (str/trim id)))
        (= id "0")
        (= id 0))))

(defn before-save [params]
  (let [params (if-let [file-data (:imagen params)]
                (if (and (map? file-data) (:tempfile file-data))
                  (-> params
                      (assoc :file file-data :file-column :imagen)
                      (dissoc :imagen))
                  params)
                params)]
    (if (new-record? params)
      (let [chars "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789"
            temp-password (apply str (repeatedly 10 #(rand-nth chars)))]
        (-> params
            (assoc :password (hashers/derive temp-password)
                   :temp-password-plaintext temp-password)))
      params)))

(defn after-save [entity-id _params]
  (when-let [temp-password (:temp-password-plaintext entity-id)]
    (println "========== NEW USER CREATED ==========")
    (println "  Username:    " (:username entity-id))
    (println "  Email:       " (:email entity-id))
    (println "  Temp password: " temp-password)
    (println "======================================")
    (email/send-welcome-email entity-id))
  {:success true})

(defn before-delete [_entity-id]
  {:success true})

(defn after-delete [_entity-id]
  {:success true})
