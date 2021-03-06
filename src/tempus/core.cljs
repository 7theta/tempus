;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns tempus.core
  (:refer-clojure :exclude [+ - < > <= >= time second format into])
  (:require ["date-fns" :as date-fns]
            ["date-fns-tz" :as date-fns-tz]))

(defrecord DateTime [date-time]
  Object
  (equals [this other]
    (date-fns/isEqual (:date-time this) (:date-time other)))
  (toString [_]
    (str "#tempus/date-time " (.toISOString date-time))))

(defn date-time
  ([year month day hour minute]
   (date-time year month day hour minute 0 0))
  ([year month day hour minute second]
   (date-time year month day hour minute second 0))
  ([year month day hour minute second millisecond]
   (DateTime. (js/Date. (js/Date.UTC year (dec month) day
                                     hour minute second millisecond)))))

(defn into
  [type ts]
  (let [to-long #(.valueOf (:date-time %))]
    (case type
      :long (to-long ts)
      :edn (to-long ts)
      :native (:date-time ts))))

(defn from
  [type value]
  (let [from-long #(DateTime. (js/Date. %))]
    (case type
      :long (from-long value)
      :edn (from-long value)
      :native (->> value (DateTime.) (into :long) (from :long)))))

(defn format
  [pattern ts]
  (date-fns-tz/format (date-fns-tz/utcToZonedTime (:date-time ts) "UTC")
                      pattern #js {:timeZone "UTC"}))

(defn now
  []
  (DateTime. (js/Date.)))

(defn year
  [t]
  (.getUTCFullYear (:date-time t)))

(defn month
  [t]
  (inc (.getUTCMonth (:date-time t))))

(defn day
  [t]
  (.getUTCDate (:date-time t)))

(defn hour
  [t]
  (.getUTCHours (:date-time t)))

(defn minute
  [t]
  (.getUTCMinutes (:date-time t)))

(defn second
  [t]
  (.getUTCSeconds (:date-time t)))

(defn millisecond
  [t]
  (.getUTCMilliseconds (:date-time t)))

(defn +
  [ts & durations]
  (reduce (fn [ts duration]
            (DateTime. ((case (:unit duration)
                          :years date-fns/addYears
                          :months date-fns/addMonths
                          :days date-fns/addDays
                          :hours date-fns/addHours
                          :minutes date-fns/addMinutes
                          :seconds date-fns/addSeconds
                          :milliseconds date-fns/addMilliseconds)
                        (:date-time ts) (:value duration)))) ts durations))

(defn -
  [ts & durations]
  (reduce (fn [ts duration]
            (DateTime. ((case (:unit duration)
                          :years date-fns/subYears
                          :months date-fns/subMonths
                          :days date-fns/subDays
                          :hours date-fns/subHours
                          :minutes date-fns/subMinutes
                          :seconds date-fns/subSeconds
                          :milliseconds date-fns/subMilliseconds)
                        (:date-time ts) (:value duration)))) ts durations))

(defn >
  [& times]
  (->> (partition 2 1 times)
       (every? (fn [[a b]] (date-fns/isAfter (:date-time a) (:date-time b))))))

(defn <
  [& times]
  (->> (partition 2 1 times)
       (every? (fn [[a b]] (date-fns/isBefore (:date-time a) (:date-time b))))))

(defn <=
  [& times]
  (->> (partition 2 1 times)
       (every? (fn [[a b]] (or (= a b) (< a b))))))

(defn >=
  [& times]
  (->> (partition 2 1 times)
       (every? (fn [[a b]] (or (= a b) (> a b))))))
