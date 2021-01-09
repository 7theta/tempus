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
  (:refer-clojure :exclude [+ - < > <= >= time second into format])
  (:import [java.time OffsetDateTime Clock ZoneOffset Instant]
           [java.time.temporal ChronoUnit]
           [java.time.format DateTimeFormatter]))

(defrecord DateTime [^OffsetDateTime date-time]
  Object
  (toString [_]
    (str "#tempus/date-time \"" (.toString date-time) "\"")))

(defmethod print-method DateTime [^DateTime ts ^java.io.Writer w]
  (.write w (.toString ts)))

(defmethod print-dup DateTime [^DateTime ts ^java.io.Writer w]
  (.write w (.toString ts)))

(defmethod print-dup OffsetDateTime [^OffsetDateTime ts ^java.io.Writer w]
  (.write w (str "\"" (.toString ts) "\"")))

(defn date-time
  ([year month day hour minute]
   (date-time year month day hour minute 0 0))
  ([year month day hour minute second]
   (date-time year month day hour minute second 0))
  ([year month day hour minute second millisecond]
   (DateTime. (OffsetDateTime/of year month day
                                 hour minute second (* 1000000 millisecond)
                                 ZoneOffset/UTC))))

(defn into
  [type ts]
  (let [to-long #(let [^OffsetDateTime dt (:date-time %)]
                   (-> dt (.toInstant) (.toEpochMilli)))]
    (case type
      :long (to-long ts)
      :edn (to-long ts)
      :native (:date-time ts))))

(defn from
  [type value]
  (let [from-long #(DateTime. (-> (Instant/ofEpochMilli %)
                                  (OffsetDateTime/ofInstant ZoneOffset/UTC)))]
    (case type
      :long (from-long value)
      :edn (from-long value)
      :native (->> value (DateTime.) (into :long) (from :long)))))

(defn format
  [pattern ts]
  (.format ^OffsetDateTime (:date-time ts) (DateTimeFormatter/ofPattern pattern)))

(defn parse
  ([s]
   (DateTime. (OffsetDateTime/parse s)))
  ([formatter s]))

(defn now
  []
  (DateTime. (OffsetDateTime/now (Clock/systemUTC))))

(defn year
  [t]
  (.getYear ^OffsetDateTime (:date-time t)))

(defn month
  [t]
  (.getMonthValue ^OffsetDateTime (:date-time t)))

(defn day
  [t]
  (.getDayOfMonth ^OffsetDateTime (:date-time t)))

(defn hour
  [t]
  (.getHour ^OffsetDateTime (:date-time t)))

(defn minute
  [t]
  (.getMinute ^OffsetDateTime (:date-time t)))

(defn second
  [t]
  (.getSecond ^OffsetDateTime (:date-time t)))

(defn millisecond
  [t]
  (quot (.getNano ^OffsetDateTime (:date-time t)) 1000000))

(defn- ^ChronoUnit native-unit
  [unit]
  (case unit
    :years ChronoUnit/YEARS
    :months ChronoUnit/MONTHS
    :days ChronoUnit/DAYS
    :hours ChronoUnit/HOURS
    :minutes ChronoUnit/MINUTES
    :seconds ChronoUnit/SECONDS
    :milliseconds ChronoUnit/MILLIS))

(defn +
  [ts & durations]
  (reduce (fn [ts duration]
            (DateTime. (.plus ^OffsetDateTime (:date-time ts)
                              ^long (:value duration)
                              (native-unit (:unit duration))))) ts durations))

(defn -
  [ts & durations]
  (reduce (fn [ts duration]
            (DateTime. (.minus ^OffsetDateTime (:date-time ts)
                               ^long (:value duration)
                               (native-unit (:unit duration))))) ts durations))

(defn >
  [& times]
  (->> (partition 2 1 times)
       (every? (fn [[a b]]
                 (.isAfter ^OffsetDateTime (:date-time a)
                           ^OffsetDateTime (:date-time b))))))

(defn <
  [& times]
  (->> (partition 2 1 times)
       (every? (fn [[a b]]
                 (.isBefore ^OffsetDateTime (:date-time a)
                            ^OffsetDateTime (:date-time b))))))

(defn <=
  [& times]
  (->> (partition 2 1 times)
       (every? (fn [[a b]] (or (= a b) (< a b))))))

(defn >=
  [& times]
  (->> (partition 2 1 times)
       (every? (fn [[a b]] (or (= a b) (> a b))))))
