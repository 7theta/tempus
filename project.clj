;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(defproject com.7theta/tempus "0.2.0"
  :description "Clojure(script) time library"
  :url "https://github.com/7theta/tempus"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [inflections "0.13.2"]
                 [com.cognitect/transit-clj "1.0.324" :exclusions [com.fasterxml.jackson.core/jackson-core]]]
  :profiles {:dev {:global-vars {*warn-on-reflection* true}
                   :dependencies [[org.clojure/clojurescript "1.10.773"]
                                  [org.clojure/tools.namespace "1.0.0"]]
                   :source-paths ["dev"]}}
  :prep-tasks ["compile"]
  :scm {:name "git"
        :url "https://github.com/7theta/tempus"})
