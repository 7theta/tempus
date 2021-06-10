;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns tempus.transit
  (:require [tempus.core #?@(:cljs [:refer [DateTime]]) :as t]
            [tempus.duration #?@(:cljs [:refer [Duration]]) :as td]
            [cognitect.transit :as transit])
  #?(:clj (:import [tempus.core DateTime]
                   [tempus.duration Duration])))

(def handlers {:read {"tempus/date-time" (transit/read-handler (partial t/from :edn))
                      "tempus/duration" (transit/read-handler (partial td/from :edn))}
               :write {DateTime (transit/write-handler (constantly "tempus/date-time") (partial t/into :edn))
                       Duration (transit/write-handler (constantly "tempus/duration") (partial td/into :edn))}})
