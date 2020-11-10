;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns tempus.duration
  (:refer-clojure :exclude [into])
  (:require [inflections.core :refer [singular plural]]))

(defrecord Duration [value unit]
  Object
  (toString [_]
    (str "#tempus/duration [" value " :" (cond-> (name unit) (clojure.core/= value 1) singular) "]")))

#?(:clj
   (defmethod print-method Duration [^Duration p ^java.io.Writer w]
     (.write w (.toString p))))

(defn into
  [type d]
  (case type
    :edn [(:value d) (:unit d)]))

(defn from
  [type [value unit]]
  (case type
    :edn (Duration. value (-> unit plural keyword))))

(defn years
  [value]
  (Duration. value :years))

(defn months
  [value]
  (Duration. value :months))

(defn weeks
  [value]
  (Duration. value :weeks))

(defn days
  [value]
  (Duration. value :days))

(defn hours
  [value]
  (Duration. value :hours))

(defn minutes
  [value]
  (Duration. value :minutes))

(defn seconds
  [value]
  (Duration. value :seconds))

(defn milliseconds
  [value]
  (Duration. value :milliseconds))
