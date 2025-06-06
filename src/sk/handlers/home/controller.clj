(ns sk.handlers.home.controller
  (:require [sk.layout :refer [application error-404]]
            [sk.handlers.home.view :refer [carousel-view
                                           main-view
                                           change-password-view]]
            [noir.session :as session]
            [noir.util.crypt :as crypt]
            [sk.handlers.home.model :refer [get-user
                                            get-users
                                            update-password]]
            [sk.models.util :refer [get-session-id]]))

(defn main
  [_]
  (let [title "Cicloturismo"
        ok (get-session-id)
        js nil
        content (carousel-view)]
    (application title ok js content)))

(defn login
  [_]
  (let [title "Entrar al sitio"
        ok (get-session-id)
        js nil
        content (main-view title)]
    (application title ok js content)))

(defn login-user
  [{params :params}]
  (let [username (:username params)
        password (:password params)
        row (first (get-user username))
        active (:active row)]
    (if (= active "T")
      (if (crypt/compare password (:password row))
        (do
          (session/put! :user_id (:id row))
          (error-404 "Logged in correctly!" "/"))
        (error-404 "Incorrect Username and or Password!" "/"))
      (error-404 "User is not active!" "/"))))

(defn change-password
  [_]
  (let [title "Cambiar Contraseña"
        ok (get-session-id)
        js nil
        content (change-password-view title)]
    (application title ok js content)))

(defn process-password
  [{params :params}]
  (let [username (:email params)
        where-clause ["username = ?" username]
        password (crypt/encrypt (:password params))
        row (first (get-user username))
        active (:active row)]
    (if (= active "T")
      (if (seq (update-password username password))
        (error-404 "Su contraseña se cambio correctamente!" "/home/login")
        (error-404 "No se pudo cambiar su contraseña" "/home/login"))
      (error-404 "No se pudo cambiar su contraseña!" "/home/login"))))

(defn logoff-user
  [_]
  (session/clear!)
  (error-404 "Logoff successfully" "/"))

(comment
  (:username (first (get-users))))
