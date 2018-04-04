(ns family-recipes.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [family-recipes.handler :refer :all]))

(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "Family Recipes"))))

  (testing "list recipes route"
    (let [response (app (mock/request :get "/family/1/recipes"))]
      (is (= (:status response) 200))
      (is (= (:body response) "{\"foo\":\"bar\"}"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
