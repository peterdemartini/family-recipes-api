(ns family-recipes.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [family-recipes.handler :refer :all]))

(deftest test-app
  (testing "get recipe route"
    (let [response (app (mock/request :get "/recipe/1"))]
      (is (= (:status response) 200))
      (is (= (:body response) "{\"foo\":\"bar\"}"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
