(def +lib-version+ "0.58.1")
(def +version+ (str +lib-version+ "-0"))

(set-env!
 :resource-paths #{"resources"}
 :dependencies '[[cljsjs/boot-cljsjs "0.10.5" :scope "test"]
                 [cljsjs/slate "0.58.1-0"]])

(require '[cljsjs.boot-cljsjs.packaging :refer :all])

(task-options!
 pom  {:project     'cljsjs/slate-history
       :version     +version+
       :description "An operation-based history implementation for Slate editors."
       :url         "http://slatejs.org"
       :scm         {:url "https://github.com/ianstormtaylor/slate"}
       :license     {"MIT" "http://opensource.org/licenses/MIT"}})

(deftask package []
  (comp
   (download :url (format "https://unpkg.com/slate-history@%s/dist/slate-history.js" +lib-version+)
             :target "cljsjs/slate-history/development/slate-history.inc.js")
   (download :url (format "https://unpkg.com/slate-history@%s/dist/slate-history.min.js" +lib-version+)
             :target "cljsjs/slate-history/production/slate-history.min.inc.js")
   (sift :include #{#"^cljsjs"})
   (deps-cljs :foreign-libs [{:file           #"slate-history\.inc\.js"
                              :file-min       #"slate-history\.min\.inc\.js"
                              :requires       ["cljsjs.slate"]
                              :provides       ["slate-history" "cljsjs.slate-history"]
                              :global-exports '{slate-history SlateHistory}}]
              :externs [#"slate-history\.ext\.js"])

   (pom)
   (jar)
   (validate)))
