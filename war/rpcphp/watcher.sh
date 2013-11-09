#!/bin/sh
 while inotifywait lastreq.txt; do
  cat lastreq.txt;
 done
