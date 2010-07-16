(ns lord-of-the-links.core
  (:use compojure.core
     (ring.adapter jetty)
     (ring.middleware reload stacktrace cookies session))
  (:use clojure.contrib.prxml))

(defn xml [data]
  (with-out-str
    (prxml data)))

(defonce all-posts 
  (ref [{:link "http://google.com"}]))

(defn add-post [post]
  (dosync
    (alter all-posts conj post)))

(defn post-to-xml [post]
  [:entry [:link {:href (:link post)}]])

(defn site-feed [posts]
  (vec (concat [:feed {:xmlns "http://www.w3.org/2005/Atom"}
                [:title "Eden Links"]]
               (map post-to-xml posts))))

(defroutes site-xml
           (GET "/atom.xml" [] (xml (site-feed @all-posts))))

(def app
  (-> (var site-xml)
    (wrap-reload '(lord-of-the-links.core))
    (wrap-stacktrace)
    (wrap-session)))

(defn run-properly []
  (run-jetty #'site-xml {:join? false :port 8090}))

(defn run []
  (run-jetty #'app {:join? false :port 8080}))
