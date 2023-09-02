(ns clojure-test-website.main
  (:require [ring.adapter.jetty :as jetty]
            [hiccup2.core]))

(defonce server (atom nil))

(defn handler [req]
  {:status 200
   :body "Hello World!"})

(defn start-jetty! []
  (reset!
   server
   (jetty/run-jetty handler {:join? false
                             :port 8000})))

(defn stop-jetty! []
  (.stop @server)
  (reset! server nil))
