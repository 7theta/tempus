(ns tempus.transit
  (:require [tempus.core #?@(:cljs [:refer [DateTime]]) :as t]
            [tempus.duration #?@(:cljs [:refer [Duration]]) :as td]
            [cognitect.transit :as transit])
  #?(:clj (:import [tempus.core DateTime]
                   [tempus.duration Duration])))

(def handlers {:read {"tempus/date-time" (transit/read-handler (partial t/from :edn))
                      "tempus/duration" (transit/read-handler (partial td/from :edn))}
               :write {DateTime (transit/write-handler "tempus/date-time" (partial t/into :edn))
                       Duration (transit/write-handler "tempus/duration" (partial td/into :edn))}})
