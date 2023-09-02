(ns clojure-test-website.main
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :as defaults]
            [hiccup2.core :as h]
            [muuntaja.middleware :as muuntaja]))

(defonce server (atom nil))

(def home-page
  (h/html
   [:html
    [:body
     [:h1 "Hello World!"]
     [:p "Welcome to my amazing website!"]]]))

(defn handler [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str home-page)})

(defn start-jetty! []
  (reset!
   server
   (jetty/run-jetty (-> #'handler
                        (defaults/wrap-defaults defaults/site-defaults)
                        (content-middleware/wrap-content-type "text/html"))
    {:join? false
     :port 8000})))

(defn stop-jetty! []
  (.stop @server)
  (reset! server nil))

(defn restart-jetty! []
  (stop-jetty!)
  (start-jetty!))

(comment
  (restart-jetty!))
