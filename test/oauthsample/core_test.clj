

(ns oauthsample.core-test
  (:require [clojure.test :refer :all]
            [oauthsample.core :refer :all]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest]))

(stest/check `request-auth-token)

(deftest request
  (testing "check request")
  (stest/check `request-auth-token))
