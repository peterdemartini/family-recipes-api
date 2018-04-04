(ns family-recipes.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [ring.util.http-status :as http-status]
            [schema.core :as s]))

;;
;; Schemas
;;

(s/defschema Recipe
  {:id s/Int
   :name s/Str
   (s/optional-key :description) s/Str
   :size (s/enum :L :M :S)
   :origin {:country (s/enum :FI :PO)
            :city s/Str}})

(s/defschema NewRecipe (dissoc Recipe :id))
(s/defschema UpdatedRecipe NewRecipe)

;;
;; Database
;;

(def recipes (atom {}))

(let [ids (atom 0)]
  (defn update-recipe! [maybe-id maybe-recipe]
    (let [id (or maybe-id (swap! ids inc))]
      (if maybe-recipe
        (swap! recipes assoc id (assoc maybe-recipe :id id))
        (swap! recipes dissoc id))
      (@recipes id))))

;;
;; Application
;;

(def app
  (api
    {:swagger
     {:ui "/"
      :spec "/swagger.json"
      :data {:info {:title "Family Recipes API"
                    :description "API documentation for Family Recipes"
                    :contact {:url "https://github.com/peterdemartini/family-recipes-api"}}
             :tags [{:name "recipe", :description "recipes"}]}}}

    (context "/recipe/" []
      (resource
        {:tags ["recipe"]
         :get {:summary "get recipes"
               :description "get all recipes!"
               :responses {http-status/ok {:schema [Recipe]}}
               :handler (fn [_] (ok (vals @recipes)))}
         :post {:summary "add's a recipe"
                :parameters {:body-params NewRecipe}
                :responses {http-status/created {:schema Recipe
                                                 :description "the created recipe"
                                                 :headers {"Location" s/Str}}}
                :handler (fn [{body :body-params}]
                           (let [{:keys [id] :as recipe} (update-recipe! nil body)]
                             (created (path-for ::recipe {:id id}) recipe)))}}))

    (context "/recipe/:id" []
      :path-params [id :- s/Int]

      (resource
        {:tags ["recipe"]
         :get {:x-name ::recipe
               :summary "gets a recipe"
               :responses {http-status/ok {:schema Recipe}}
               :handler (fn [_]
                          (if-let [recipe (@recipes id)]
                            (ok recipe)
                            (not-found)))}
         :put {:summary "updates a recipe"
               :parameters {:body-params UpdatedRecipe}
               :responses {http-status/ok {:schema Recipe}}
               :handler (fn [{body :body-params}]
                          (if (@recipes id)
                            (ok (update-recipe! id body))
                            (not-found)))}
         :delete {:summary "deletes a recipe"
                  :handler (fn [_]
                             (update-recipe! id nil)
                             (no-content))}}))))
