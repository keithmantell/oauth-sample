(ns dga-pub.core
  (:require [compojure.core :refer [defroutes routes GET POST]]
            [compojure.handler :as handler]
            [hiccup.page :refer [html5]]
            [hiccup.form :as f]
            [ring.middleware.params :refer [wrap-params]]
            [clj-http.client :as client]
            [clojure.pprint :as pp]
            [clojure.string]
            ))

(def client-id (System/getenv "CLIENT_ID"))
(def client-secret (System/getenv "CLIENT_SECRET"))

(defn front-door []
  (html5
   [:html
    [:head
     [:link {:rel "stylesheet" :href "//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"}]
     [:title "Welcome to the demo SP"]]
    [:body.container
     [:h1 "Hey, you've found your local Service Provider!!!"]
     [:p.lead "Client ID: " client-id "Client secret: " client-secret ]
     [:p.lead "You can get the SAML metadata for this SP " ]
     [:a.btn.btn-primary {:href (str "https://github.com/login/oauth/authorize?scope=user:email&client_id=" client-id)} "Take me to the IdP!!!"]]]))

(defn request-auth-token [request]
  (html5
    [:html
     [:head
      [:link {:rel "stylesheet" :href "//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"}]
      [:title "Welcome to the demo SP - page 2"]]
     [:body.container
      [:h1 "Here is the code response from the Oauth server"]
      [:p.lead "Callback code: " (get-in request [:params :code]) ]
      [:a.btn.btn-primary {:href (str "./post-form?code=" (get-in request [:params :code]))} "Get the auth token!!!"]]]))

(defn split-response [s]
  (reduce (fn [acc [k v]] (assoc acc (keyword k) (String. v))) {} (partition 2 (clojure.string/split s #"[=&]"))))

(defn post-auth-token [req]
  (let [code  (get-in req [:params :code])]
    (println "Code is: " code)
    (client/post "https://github.com/login/oauth/access_token"
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
       [:title "Welcome to the demo SP - page 3"]]
      [:body.container
       [:h1 "Here is the code response from the Oauth server"]
       [:p.lead "Request: " request " Access Token: " access_token ]
       [:a.btn.btn-primary {:href (str "https://api.github.com/user?access_token=" request)} "Get user data!!!"]]])))


(defroutes main-routes
  (GET "/" []
       {:status  200
        :body    (front-door)})
  (GET "/post-form" code
       (let [body (str (get (post-auth-token code) :body "missing"))
             token-body (split-response body)
             access_token (get token-body :access_token)]
         {:status  200
          :headers {"Content-Type" "text/html"}
          :body    (request-info-page access_token)}))
  (GET "/post-user-data" request ;; what is in his request?
         {:status  200
          :headers {"Content-Type" "text/html"}
          :body    (request-info-page request)})
  (GET "/callback" request
       {:status  200
        :headers {"Content-Type" "text/html"}
        :body (request-auth-token request)
        })
 )

(def handler
  ;(wrap-params
              (handler/api main-routes)
   ;           )
  )


;(defn handler [request]
;  {:status 200
;   :headers {"Content-Type" "text/html"}
;   :body "Hello World"})
