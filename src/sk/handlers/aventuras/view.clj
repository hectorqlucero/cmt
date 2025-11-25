(ns sk.handlers.aventuras.view
  (:require [clojure.string :refer [upper-case]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.crud :refer [Query db]]
            [sk.handlers.aventuras.model :refer [get-aventuras-comments]]
            [sk.migrations :refer [config]]))

(defn get-imagen [row]
  (if-not (nil? (:imagen row))
    (str (:path config) (:imagen row) "?" (.toString (java.util.UUID/randomUUID)))
    (str "/images/placeholder_profile.png")))

(defn line-rr [label value]
  [:div.row
   [:div.col-xs-4.col-sm-4.col-md-3.col-lg-2.text-secondary [:strong label]]
   [:div.col-xs.8.col-sm-8.col-md-9.col-lg-10 value]])

(defn line-cc [aventuras_id]
  (map (fn [i]
         (let [label [:span [:strong (if (empty? (:nombre i)) "Anonimo" (:nombre i))]]
               valor [:span (:comments i)]]
           (line-rr label valor))) (get-aventuras-comments aventuras_id)))

(defn body-rr [row]
  (let [imagen (get-imagen row)
        the-id (row :id)]
    [:div.col-12.col-md-6.mb-4
     [:div.card.h-100.shadow {:style "border-radius: 12px; overflow: hidden; border-left: 4px solid var(--color-primary); background: rgba(var(--color-primary-rgb), 0.02);"}
      [:div.card-header {:style "border-radius: 12px 12px 0 0; background: rgba(var(--color-success-rgb), 0.08); color: var(--color-text); border-bottom: 1px solid var(--color-border);"}
       [:div.d-flex.align-items-center
        [:img.rounded-circle.me-3 {:src imagen
                                   :style "height: 50px; width: 50px; object-fit: cover; border: 2px solid white;"}]
        [:div
         [:h5.mb-0 [:i.fas.fa-mountain.me-2] (:nombre row)]
         [:small {:style "opacity: 0.9;"} (str (upper-case (:dia row)) " " (upper-case (:f_fecha row)))]]]]
      [:div.card-body.p-4
       [:div.mb-3
        [:p.card-text {:style "font-size: 0.95rem; line-height: 1.6;"} (:aventura row)]]

       (if (:enlace row)
         [:div.mb-3
          [:a {:href (:enlace row)
               :target "_blank"
               :style "display: inline-flex; align-items: center; gap: 6px; background: rgba(var(--color-primary-rgb), 0.1); color: var(--color-primary); border: 1px solid var(--color-primary); padding: 0.375rem 0.75rem; font-size: 0.875rem; font-weight: 500; border-radius: 6px; text-decoration: none; transition: all 0.2s ease;"}
           [:i.fas.fa-images] " Ver Fotos"]])

       (if (:enlacev row)
         [:div.mb-3
          [:a {:href (:enlacev row)
               :target "_blank"
               :style "display: inline-flex; align-items: center; gap: 6px; background: rgba(var(--color-primary-rgb), 0.1); color: var(--color-primary); border: 1px solid var(--color-primary); padding: 0.375rem 0.75rem; font-size: 0.875rem; font-weight: 500; border-radius: 6px; text-decoration: none; transition: all 0.2s ease;"}
           [:i.fas.fa-play-circle] " Ver Videos"]])

       (let [comments (get-aventuras-comments the-id)]
         (if (seq comments)
           [:div.mb-3
            [:h6 {:style "color: var(--color-text-muted); margin-bottom: 0.75rem;"} [:i.fas.fa-comments.me-2] "Comentarios"]
            [:div
             (for [comment comments]
               [:div.mb-2.p-2.bg-light.rounded {:style "border-left: 3px solid var(--color-primary);"}
                [:div.d-flex.justify-content-between.align-items-start.mb-1
                 [:strong {:style "font-size: 0.875rem; color: var(--color-primary);"}
                  (if (empty? (:nombre comment)) "Anónimo" (:nombre comment))]
                 [:small.text-muted {:style "font-size: 0.75rem;"} (:created_at comment)]]
                [:p.mb-0 {:style "font-size: 0.875rem;"} (:comments comment)]])]]))

       [:div.mt-4
        [:h6 {:style "color: var(--color-primary); margin-bottom: 0.75rem;"} [:i.fas.fa-plus-circle.me-2] "Agregar Comentario"]
        [:form {:style "max-width: 100%;"}
         (anti-forgery-field)
         [:div.mb-3
          [:input.form-control {:id (str "autor_" the-id)
                                :name "autor"
                                :placeholder "Tu nombre"
                                :style "border-radius: 6px;"}]]
         [:div.mb-3
          [:textarea.form-control {:id (str "comment_" the-id)
                                   :name "comment"
                                   :placeholder "Escribe tu comentario..."
                                   :rows "3"
                                   :style "border-radius: 6px; resize: vertical;"}]]
         [:button {:type "button"
                   :id "submit_comment"
                   :style "border-radius: 6px; width: 100%; background: var(--color-primary); color: white; border: 1px solid var(--color-primary); padding: 0.375rem 0.75rem; font-size: 0.875rem; font-weight: 500; transition: all 0.2s ease;"
                   :onclick (str "process_comment(" the-id ",this.form.autor.value,this.form.comment.value)")}
          [:i.fas.fa-paper-plane] "Enviar Comentario"]]]]]]))

(defn aventuras-view
  [rows crow]
  [:div.content-area
   [:div.container-fluid {:style "max-width: 1400px;"}
    [:div.row.mb-4
     [:div.col-12
      [:div.d-flex.justify-content-between.align-items-center
       [:h2 {:style "color: var(--color-primary); margin-bottom: 0;"} (:nombre crow)]
       [:div {:style "background: var(--color-primary); color: white; font-size: 0.875rem; padding: 0.5rem 1rem; border-radius: 6px;"}
        (str (count rows) " " (if (= (count rows) 1) "Aventura" "Aventuras"))]]]]

    [:div.row.g-3
     (map body-rr rows)]]])

(defn aventuras-scripts []
  [:script
   "
   function process_comment(the_id,autor,comment) {
    var the_autor = '#autor_'+the_id;
    var the_comment = '#comment_'+the_id;
    var token = $('#__anti-forgery-token').val();
    var reload_on_return = true;
    if (autor && comment) {
      $('#submit_comment').prop('disabled',true);
      $.ajax({
        type: 'POST',
        url: '/aventuras/comentarios',
        data: {'__anti-forgery-token': token, 'aventuras_id': the_id,'nombre': autor,'comments': comment}
      })
      .done(function(msg) {
        var json = JSON.parse(msg);
        if(json.error) {
          alert(json.error);
        } else {
          alert(json.success);
        }
        $(the_autor).val('');
        $(the_comment).val('');
        if (reload_on_return) {
          setTimeout(
            function() 
            {
              location.reload();
            }, 1000);    
        }
      });
      $('#submit_comment').prop('enabled',true);
   } else {
    alert('El autor o comentario no pueden estar vacios!');
   }
   }
   "])

(comment
  (aventuras-view (get-rows 4) (get-cmt-rows 387)))
