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
    [:div.container.border.border-dark.rounded {:style " margin-bottom:10px;"}
     [:h4.card-title [:img.rounded-circle {:id (str "img" (:id row))
                                           :style "height:65px;width:65px;margin-top:5px;margin-right:16px"
                                           :src imagen
                                           :width 95
                                           :height 71}] (:nombre row)]
     (line-rr "Fecha:" [:strong.text-warning (str (upper-case (:dia row)) (upper-case (:f_fecha row)))])
     (when (:enlace row)
       (line-rr "Fotos:" [:a.btn.btn-secondary
                          {:role "button"
                           :style "margin:1px;"
                           :href (:enlace row)
                           :target "_blank"}  "Clic aqui para ver fotos!"]))
     (when (:enlacev row)
       (line-rr "Videos:" [:a.btn.btn-secondary
                           {:href (:enlacev row)
                            :style "margin:1px;"
                            :target "_blank"}  "Clic aqui para ver videos!"]))
     (line-rr "Aventura:" (:aventura row)) [:br]
     (line-cc the-id) [:br]
     [:div
      (anti-forgery-field)
      [:div.card
       [:div.row
        [:div.col
         [:form {:style "width:55"}

          [:input.form-control {:id (str "autor_" the-id)
                                :name "autor"
                                :placeholder "autor"
                                :size "26"}]
          [:textarea.form-control {:id (str "comment_" the-id)
                                   :name "comment"
                                   :placeholder "comentario"
                                   :rows "4"
                                   :cols "26"
                                   :size "26"}]
          [:button.btn.btn-secondary {:type "button"
                                      :id "submit_comment"
                                      :style "margin:3px;"
                                      :onclick (str "process_comment(" the-id ",this.form.autor.value,this.form.comment.value)")} "Enviar Comentario"]]]]]]]))

(defn aventuras-view
  [rows crow]
  [:div.container
   [:div.card-header
    [:a.link-underline-light.link-underline-opacity-0.text-secondary
     {:href "#"
      :data-toggle "tooltip"
      :title (:comments crow)} [:span {:style "font-size:1.5em;"} (:nombre crow)]]]
   (map body-rr rows)])

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
