#! /bin/bash
clojure -X:test :excludes '[:integration]'
