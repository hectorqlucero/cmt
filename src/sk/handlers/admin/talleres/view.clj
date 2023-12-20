(ns sk.handlers.admin.talleres.view
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
    {:id "nombre"
     :name "nombre"
     :class "easyui-textbox"
     :prompt "Nombre del taller..."
     :data-options "label:'Nombre:',
        labelPosition:'top',
        required:true,
        width:'100%'"})
   (build-field
    {:id "direccion"
     :name "direccion"
     :class "easyui-textbox"
     :prompt "Domicilio del taller..."
     :data-options "label:'Domicilio:',
        labelPosition:'top',
        required:true,
        width:'100%'"})
   (build-field
    {:id "telefono"
     :name "telefono"
     :class "easyui-textbox"
     :prompt "Telefono del taller..."
     :data-options "label:'Telefono:',
        labelPosition:'top',
        required:false,
        width:'100%'"})
   (build-field
    {:id "horarios"
     :name "horarios"
     :class "easyui-textbox"
     :prompt "Horarios del taller..."
     :data-options "label:'Horarios:',
        labelPosition:'top',
        required:false,
        width:'100%'"})
   (build-field
    {:id "sitio"
     :name "sitio"
     :class "easyui-textbox"
     :prompt "Sitio ej. enlace a facebook o pagina web..."
     :data-options "label:'Enlace Sitio:',
        labelPosition:'top',
        required:false,
        width:'100%'"})
   (build-field
    {:id "direcciones"
     :name "direcciones"
     :class "easyui-textbox"
     :prompt "Enlace a google maps..."
     :data-options "label:'Direcciones:',
        labelPosition:'top',
        required:false,
        multiline:true,
        height:120,
        width:'100%'"})
   (build-field
    {:id "historia"
     :name "historia"
     :class "easyui-textbox"
     :prompt "Historia o detalles del taller"
     :data-options "label:'Historia/Detalles:',
        labelPosition:'top',
        required:false,
        multiline:true,
        height:120,
        width:'100%'"})))

(defn talleres-view [title]
  (list
   (anti-forgery-field)
   (build-table
    title
    "/admin/talleres"
    (list
     [:th {:data-options "field:'nombre',sortable:true,width:100"} "NOMBRE"]
     [:th {:data-options "field:'direccion',sortable:true,width:100"} "DIRECCION"]
     [:th {:data-options "field:'telefono',sortable:true,width:100"} "TELEFONO"]))
   (build-toolbar)
   (build-dialog title (dialog-fields))
   (build-dialog-buttons)))

(defn talleres-scripts []
  (include-js "/js/grid.js"))
