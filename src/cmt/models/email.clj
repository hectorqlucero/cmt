(ns cmt.models.email
  (:require [clojure.string :as str]
            [postal.core :refer [send-message]]
            [cmt.models.crud :refer [config]]
            [cmt.config.loader :as cfg-loader]))

;;(send-message {:host "email-host"
;;               :user "email-user"
;;               :pass "email-password"
;;               :ssl  true}
;;              {:from    "me@draines.com"
;;               :to      "foo@example.com"
;;               :subject "Hi!"
;;               :body    [{:type    "text/html"
;;                          :content "<b>Test!</b>"}
;;                           ;;;; supports both dispositions:
;;                         {:type    :attachment
;;                          :content (java.io.File. "/tmp/foo.txt")}
;;                         {:type         :inline
;;                          :content      (java.io.File. "/tmp/a.pdf")
;;                          :content-type "application/pdf"}]})
;;{:code 0, :error :SUCCESS, :message "message sent"}      ;Returned error messages
;;
;;{:host "mail.gmx.com"
;; :user "xxxxxxx@gmx.com"
;; :pass "xxxxxxx"
;; :tls  true}
;;
;;{:host "smtp.gmail.com"
;; :user "xxxxxxx@gmx.com"
;; :pass "xxxxxxxx"
;; :ssl  true}

(defn- runtime-config []
  ;; Use latest app-config values so email settings can be adjusted without restart.
  (merge config (or (cfg-loader/load-config "app-config") {})))

(defn- smtp-host [cfg]
  (merge
   {:host (:email-host cfg)
    :user (:email-user cfg)
    :pass (or (:email-pwd cfg) (:email-passwd cfg))}
   (cond-> {}
     (contains? cfg :email-port) (assoc :port (:email-port cfg))
     (contains? cfg :email-ssl) (assoc :ssl (:email-ssl cfg))
     (contains? cfg :email-tls) (assoc :tls (:email-tls cfg))
     (and (not (contains? cfg :email-ssl))
          (not (contains? cfg :email-tls))) (assoc :tls true))))

(defn- commenter-email [email]
  (let [mail (str/trim (str (or email "")))]
    (when-not (str/blank? mail) mail)))

(defn- send-html-email! [to subject html]
  (let [cfg (runtime-config)
        host (smtp-host cfg)]
    (when (and (:host host) (:user host) (:pass host) (not (str/blank? (str to))))
      (try
        (let [resp (send-message host {:from (or (:email-from cfg) (:email-user cfg) "no-reply@localhost")
                                       :to to
                                       :subject subject
                                       :body [{:type "text/html;charset=utf-8"
                                               :content html}]})]
          (when-not (= :SUCCESS (:error resp))
            (println "[email] send non-success:" resp))
          resp)
        (catch Exception e
          (println "[email] send failed:" (.getMessage e))
          nil)))))

(defn send-blog-comment-notification
  [{:keys [adventure-id author email comment link]}]
  (let [cfg (runtime-config)
        to (or (:email-notify-to cfg) (:email-user cfg))
        subject (str "Nuevo comentario en aventura #" adventure-id)
        body (str "<h3>Nuevo comentario en el blog</h3>"
                  "<p><b>Aventura:</b> #" adventure-id "</p>"
                  "<p><b>Autor:</b> " (or author "-") "</p>"
                  "<p><b>Email:</b> " (or email "-") "</p>"
                  "<p><b>Comentario:</b><br/>" (or comment "") "</p>"
                  "<p><a href='" (or link "") "'>Ver aventura</a></p>")]
    (send-html-email! to subject body)))

(def body
  {:from    "marcopescador@lucero-systems.cf"
   :to      "hectorqlucero@gmail.com"
   :subject "Hi!"
   :body    [{:type    "text/html;charset=utf-8"
              :content "<b>Testing</b>"}]})
(defn send-email [host body]
  (send-message host body))

(comment
  (send-email (smtp-host (runtime-config)) body))

(defn send-blog-comment-approved
  [{:keys [adventure-id author email comment link]}]
  (let [to (commenter-email email)
        subject (str "Tu comentario fue aprobado en la aventura #" adventure-id)
        body (str "<h3>Tu comentario fue aprobado</h3>"
                  "<p>Hola " (or author "-") ",</p>"
                  "<p>Tu comentario ya está visible en la aventura #" adventure-id ".</p>"
                  "<p><b>Comentario:</b><br/>" (or comment "") "</p>"
                  "<p><a href='" (or link "") "'>Ver aventura</a></p>")]
    (send-html-email! to subject body)))

(defn send-blog-comment-reply
  [{:keys [adventure-id author email reply link]}]
  (let [to (commenter-email email)
        subject (str "Respuesta a tu comentario en la aventura #" adventure-id)
        body (str "<h3>Respuesta a tu comentario</h3>"
                  "<p>Hola " (or author "-") ",</p>"
                  "<p>Te respondo sobre la aventura #" adventure-id ".</p>"
                  "<p><b>Mi respuesta:</b><br/>" (or reply "") "</p>"
                  "<p><a href='" (or link "") "'>Ver aventura</a></p>")]
    (send-html-email! to subject body)))
