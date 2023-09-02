(ns clojure-test-website.main
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.content-type :as content-middleware]
            [ring.middleware.defaults :as defaults]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [hiccup2.core :as h]
            [muuntaja.middleware :as muuntaja]))

(defonce server (atom nil))

(defn layout [content]
  (h/html
   [:html {:lang "en"}
    [:head
     [:link {:rel "stylesheet"
             :href "https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css"}]]
    [:body
     [:nav.navbar.navbar-expand-lg.bg-body-tertiary
      [:div.container-fluid
       [:div.navbar-header
        [:a.navbar-brand {:href "/"} "Website"]]
       [:div.collapse.navbar-collapse
        [:div.navbar-nav
         [:ul.navbar-nav
          [:li.nav-item
           [:a.nav-link {:href "#"} "First Link"]]]] ]]]
     [:div.container content]]]))

(def home-page
  (h/html
   [:h1 "Home page"]
   [:p "This is the home page of the application"]))

;; (defn html-response [raw-str]
;;   {:status 200
;;    :headers {"Content-Type" "text/html"}
;;    :body (str raw-str)})

;; (defn handler [req]
;;   (html-response (layout home-page)))

(defroutes handler
  (GET "/" [] (str (layout home-page))))

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
