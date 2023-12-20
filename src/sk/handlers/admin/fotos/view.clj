(ns sk.handlers.admin.fotos.view
  (:require
   [hiccup.page :refer [include-js]]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [sk.models.util :refer
    [build-dialog build-dialog-buttons build-field build-table build-toolbar]]))

(defn dialog-fields []
  (list
   (build-field
    {:id "id"
     :name "id"
     :type "hidden"})
   (build-field
    {:id "fecha"
     :name "fecha"
     :class "easyui-datebox"
     :prompt "mm/dd/aaaa ex. 02/07/1957 es: Febreo 2 de 1957"
     :data-options "label:'Fecha:',
        labelPosition:'top',
        required:true,
        width:'100%'"})
   (build-field
    {:id "enlace"
     :name "enlace"
     :class "easyui-textbox"
     :prompt "Enlace a las fotos..."
     :data-options "label:'Enlace:',
        labelPosition:'top',
        required:true,
        width:'100%'"})))

(defn fotos-view [title]
  (list
   (anti-forgery-field)
   (build-table
    title
    "/admin/fotos"
    (list
     [:th {:data-options "field:'fecha_formatted',sortable:true,width:100"} "FECHA"]
     [:th {:data-options "field:'enlace',sortable:true,width:100"} "ENLACE"]))
   (build-toolbar)
   (build-dialog title (dialog-fields))
   (build-dialog-buttons)))

(defn fotos-scripts []
  (include-js "/js/grid.js"))
