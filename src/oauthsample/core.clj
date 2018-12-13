(ns oauthsample.core
  (:require [compojure.core :refer [defroutes routes GET POST]]
            [compojure.handler :as handler]
            [hiccup.page :refer [html5]]
            [hiccup.form :as f]
            [hiccup.table :as tab]
            [ring.middleware.params :refer [wrap-params]]
            [clojure.tools.logging :as log]
            [clj-http.client :as client]
            [clojure.pprint :as pp]
            [clojure.string]))

(def client-id (System/getenv "CLIENT_ID"))

(def client-secret (System/getenv "CLIENT_SECRET"))
(def authorization-endpoint (System/getenv "AUTHORIZATION_ENDPOINT"))
(def token-endpoint (System/getenv "TOKEN_ENDPOINT"))
(def scope (System/getenv "SCOPE"))

(log/info "Client ID: " (System/getenv "CLIENT_ID"))
(log/info "Client Secret (length): " (count (System/getenv "CLIENT_SECRET")))
(log/info "Authorization Endpoint: " (System/getenv "AUTHORIZATION_ENDPOINT"))
(log/info "Token Endpoint: " (System/getenv "TOKEN_ENDPOINT"))
(log/info "Scope: " (System/getenv "SCOPE"))

(defn front-door []
  (html5
   [:html
    [:head
     [:link {:rel "stylesheet" :href "//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"}]
     [:title "Welcome to the Oauth demo"]]
    [:body.container
     [:h1 "Welcome to the Oauth demo"]
     [:p.lead "This demo will check the authentication of the app and user with the Github oauth server."]
     [:p.lead "Please check that the Client ID and Client Secret are set correctly"]
     (if (= (count client-id) 0)
       [:p.lead "!! Client ID not set !!  Please set (via environment variable CLIENT_ID) and restart"]
       (html5  [:p.lead "Values used:"]
               (hiccup.table/to-table1d (list {:variable "Client ID" :value client-id}
                                              {:variable "Client Secret (length)" :value (count client-secret)}
                                              {:variable "Authorization Endpoint " :value authorization-endpoint}
                                              {:variable "Token Endpoint" :value token-endpoint}
                                              {:variable "Scope" :value scope})
                                        [:variable "Variable" :value "Value"])
               [:p.lead ""]
               [:a.btn.btn-primary {:href (str authorization-endpoint "?scope=" scope "&client_id=" client-id "&response_type=code")} "Check my authentication"]))]]))

(defn request-auth-token [request]
  (html5
   [:html
    [:head
     [:link {:rel "stylesheet" :href "//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"}]
     [:title "Welcome to the Oauth Demo - page 2"]]
    [:body.container
     [:h1 "Here is the code response from the Oauth server"]
     [:p.lead "Callback code: " (get-in request [:params :code])]
     [:p.lead "With this code we can get an access token for API calls"]
                                        ;  [:p.lead "Request content" request]
     [:a.btn.btn-primary {:href (str "./get-access-token?code=" (get-in request [:params :code]))} "Get the access token!!!"]]]))

(defn split-response [s]
  (reduce (fn [acc [k v]] (assoc acc (keyword k) (String. v))) {} (partition 2 (clojure.string/split s #"[=&]"))))


(defn cl-print [x] (doto x
                     (pp/pprint)))

(defn post-auth-token [req]
  (let [code  (get-in req [:params :code])]
    (client/post token-endpoint
                 {:as :json
                  :form-params
                  {:client_id client-id
                   :client_secret client-secret
                   :code code
                   :grant_type "authorization_code"}})))

(defn request-info-page [request]
  (let [access_token (get-in request [:body :access_token])]
    (pp/pprint (str  "Oauth Token response" access_token))
    (log/info "Auth Token Response: " request)
    (log/info "Auth Token: " access_token) ;remove for production
    (html5
     [:html
      [:head
       [:link {:rel "stylesheet" :href "//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"}]
       [:title "Welcome to the Oauth demo - page 3"]]
      [:body.container
       [:h1 "Here is the code response from the Oauth server"]
       [:p.lead "Request: " (pp/pprint request) " Access Token: " access_token]
       [:p.lead "Now we will use the token to get User data"]
       [:a.btn.btn-primary {:href
                                        ;git version (str "https://api.github.com/user?access_token=" request)
                            (str "https://api.github.com/user?access_token=" access_token
                                 "Get user data!!!")}]]])))

(defroutes main-routes
  (GET "/" []
       {:status  200
        :body    (front-door)})
  (GET "/get-access-token" code
       (let [body (post-auth-token code)
             access_token (get-in body [:body :access-token])]
         {:status  200
          :headers {"Content-Type" "text/html"}
          :body   (request-info-page body)}))

  (GET "/callback" request
       {:status  200
        :headers {"Content-Type" "text/html"}
        :body (request-auth-token request)}))

(def handler
  (handler/api main-routes))
