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
