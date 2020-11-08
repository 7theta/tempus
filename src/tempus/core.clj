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
  (let [to-long #(-> % :date-time (.toInstant) (.toEpochMilli))]
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
  (.format (:date-time ts) (DateTimeFormatter/ofPattern pattern)))

(defn parse
  ([s]
   (DateTime. (OffsetDateTime/parse s)))
  ([formatter s]))

(defn now
  []
  (DateTime. (OffsetDateTime/now (Clock/systemUTC))))

(defn year
  [t]
  (.getYear (:date-time t)))

(defn month
  [t]
  (.getMonthValue (:date-time t)))

(defn day
  [t]
  (.getDayOfMonth (:date-time t)))

(defn hour
  [t]
  (.getHour (:date-time t)))

(defn minute
  [t]
  (.getMinute (:date-time t)))

(defn second
  [t]
  (.getSecond (:date-time t)))

(defn millisecond
  [t]
  (quot (.getNano (:date-time t)) 1000000))

(defn +
  [ts period]
  (DateTime. (.plus (:date-time ts) (:value period)
                    (case (:unit period)
                      :years ChronoUnit/YEARS
                      :months ChronoUnit/MONTHS
                      :days ChronoUnit/DAYS
                      :hours ChronoUnit/HOURS
                      :minutes ChronoUnit/MINUTES
                      :seconds ChronoUnit/SECONDS
                      :milliseconds ChronoUnit/MILLIS))))

(defn -
  [ts period]
  (DateTime. (.minus (:date-time ts) (:value period)
                     (case (:unit period)
                       :years ChronoUnit/YEARS
                       :months ChronoUnit/MONTHS
                       :days ChronoUnit/DAYS
                       :hours ChronoUnit/HOURS
                       :minutes ChronoUnit/MINUTES
                       :seconds ChronoUnit/SECONDS
                       :milliseconds ChronoUnit/MILLIS))))

(defn >
  [a b]
  (.isAfter (:date-time a) (:date-time b)))

(defn <
  [a b]
  (.isBefore (:date-time a) (:date-time b)))

(defn <=
  [a b]
  (or (= a b) (< a b)))

(defn >=
  [a b]
  (or (= a b) (> a b)))
