#!/bin/bash

# Assuming a data set of n records that starts with n/2 positive records and ends
# with n/2 negative records, prepares training and testing data sets.

data_set_size=$(wc ./data | awk '{ print $1 }')

echo "Processing data set of $data_set_size records"

split -l $(($data_set_size / 2)) ./data

mv xaa positives
mv xab negatives

size=$(($data_set_size / 4))

split -l $size positives

mv xaa training_positives
mv xab testing_positives

split -l $size negatives

mv xaa training_negatives
mv xab testing_negatives

cat training_positives training_negatives >training
cat testing_positives testing_negatives >testing

rm training_positives training_negatives testing_positives testing_negatives
rm negatives positives




