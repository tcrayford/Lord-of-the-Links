(ns lord-of-the-links.core-test
  (:use [lord-of-the-links.core] :reload-all)
  (:use [clojure.test]))

(deftest add-post-test
         (do (add-post {:link "http://google.com"})
                       (is (some #{{:link "http://google.com"}} @all-posts))))

(deftest post-to-xml-outputs-single-entry-with-one-link
         (is (= [:entry [:link {:href "http://google.com"}]]
                (post-to-xml {:link "http://google.com"})))
         (is (= [:entry [:link {:href "http://hoogle.com"}]]
                (post-to-xml {:link "http://hoogle.com"}))))

(deftest whole-site-xml-outputs-all-entries
         (is (= [:feed {:xmlns "http://www.w3.org/2005/Atom"}
                 [:title "Eden Links"]
                 [:entry [:link {:href "http://google.com"}]]]
                (site-feed [{:link "http://google.com"}]))))
