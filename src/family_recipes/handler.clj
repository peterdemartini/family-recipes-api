(ns family-recipes.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.format-params :refer [wrap-restful-params]]
            [ring.middleware.format-response :refer [wrap-restful-response]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

; [monger.core :as mg]
; (let [uri "mongodb://127.0.0.1/family-recipes"
;   {:keys [conn db]} (mg/connect-via-uri uri)])

(defn index [request]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Family Recipes"})

(defn recipe-routes [family-id recipe-id]
  (routes
    (GET "/" [] {:status 200
     :headers {"Content-Type" "application/json"}
     :body {:foo "bar"}})
    (POST "/" []{:status 201})
    (PUT "/" [] {:status 204})
    (DELETE "/" [] {:status 204})))

(defn family-routes [family-id]
 (routes
   (GET "/recipes", [] {:status 200
    :headers {"Content-Type" "application/json"}
    :body [{:foo "bar"}]})
   (context ["/recipe/:recipe-id" :recipe-id #".*"] [recipe-id]
     (recipe-routes family-id recipe-id))))

(defroutes all-routes
  (ANY "/" [] index)
  (context ["/family/:family-id" :family-id #".*"] [family-id]
    (family-routes family-id))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults
    (-> all-routes
      (wrap-restful-params)
      (wrap-restful-response))
    site-defaults))
