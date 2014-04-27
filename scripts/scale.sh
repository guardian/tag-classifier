#!/bin/bash

svm-scale -l 0 -s scaling_parameters training >training.scale
svm-scale -l 0 -r scaling_parameters testing >testing.scale
