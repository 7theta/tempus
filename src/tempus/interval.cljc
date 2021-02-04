;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns tempus.interval
  (:refer-clojure :exclude [into])
  (:require [tempus.core :as t]))

(defrecord Interval [start end]
  Object
  (toString [_]
    (str "#tempus/interval [" start " - " end "]")))

#?(:clj
   (defmethod print-method Interval [^Interval p ^java.io.Writer w]
     (.write w (.toString p))))

(defn interval
  [start end]
  (Interval. start end))

(defn into
  [type i]
  (let [ms (- (t/into :long (:end i)) (t/into :long (:start i)))]
    (case type
      :weeks (/ ms 6.048e+8)
      :days (/ ms 8.64e+7)
      :hours (/ ms 3.6e+6)
      :minutes (/ ms 60000)
      :seconds (/ ms 1000)
      :milliseconds ms)))
