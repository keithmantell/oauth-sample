(ns oauthsample.core
  (:require [compojure.core :refer [defroutes routes GET POST]]
            [compojure.handler :as handler]
            [hiccup.page :refer [html5]]
            [hiccup.form :as f]
            [ring.middleware.params :refer [wrap-params]]
            [clojure.tools.logging :as log]
            [clj-http.client :as client]
            [clojure.pprint :as pp]
            [clojure.string]))

(def client-id (System/getenv "CLIENT_ID"))
(def client-secret (System/getenv "CLIENT_SECRET"))
(def authorization-endpoint (System/getenv "AUTHORIZATION_ENDPOINT" ))
(def token-endpoint (System/getenv "TOKEN_ENDPOINT"))
(def scope (System/getenv "SCOPE"))

(log/info "Client ID: " (System/getenv "CLIENT_ID"))

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
       (html5  [:p.lead "Client ID: " client-id ]
               [:p.lead "Client secret: xxxxxx" (count client-secret) ]
               [:p.lead "" ]
               [:a.btn.btn-primary {:href (str authorization-endpoint "?scope=" scope "&client_id=" client-id)} "Check my authentication"]))]]))

(defn request-auth-token [request]
  (html5
   [:html
    [:head
     [:link {:rel "stylesheet" :href "//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"}]
     [:title "Welcome to the Oauth Demo - page 2"]]
    [:body.container
     [:h1 "Here is the code response from the Oauth server"]
     [:p.lead "Callback code: " (get-in request [:params :code]) ]
     [:p.lead "With this code we can get an access token for API calls"]
                                        ;  [:p.lead "Request content" request]
     [:a.btn.btn-primary {:href (str "./get-access-token?code=" (get-in request [:params :code]))} "Get the access token!!!"]]]))

(defn split-response [s]
  (reduce (fn [acc [k v]] (assoc acc (keyword k) (String. v))) {} (partition 2 (clojure.string/split s #"[=&]"))))

(defn post-auth-token [req]
  (let [code  (get-in req [:params :code])]
    (println "Code is: " code)
    (client/post token-endpoint
                 {:as :clojure
                  :form-params
                  {:client_id client-id
                   :client_secret client-secret
                   :code code
                   }})))


(defn request-info-page [request]
  (let [access_token (get-in request [:params :code])]
    (html5
     [:html
      [:head
       [:link {:rel "stylesheet" :href "//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"}]
       [:title "Welcome to the Oauth demo - page 3"]]
      [:body.container
       [:h1 "Here is the code response from the Oauth server"]
       [:p.lead "Request: " request " Access Token: " access_token ]
       [:p.lead "Now we will use the token to get User data"]
       [:a.btn.btn-primary {:href (str "https://api.github.com/user?access_token=" request)} "Get user data!!!"]]])))


(defroutes main-routes
  (GET "/" []
       {:status  200
        :body    (front-door)})
  (GET "/get-access-token" code
       (let [body (str (get (post-auth-token code) :body "missing"))
             token-body (split-response body)
             access_token (get token-body :access_token)]
         {:status  200
          :headers {"Content-Type" "text/html"}
          :body    (request-info-page access_token)}))

  (GET "/callback" request
       {:status  200
        :headers {"Content-Type" "text/html"}
        :body (request-auth-token request)
        })
  )

(def handler
   (handler/api main-routes))
