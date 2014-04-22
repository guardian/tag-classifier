# Tag Classifier

A supervised learning classifier, which recommends tags for The Guardian articles based on what previous articles with
similar features have been tagged.

A work in progress ...

## TODO

* Finish the miner app, which should be able to create data sets for a given
  tag. The data sets should be feature vectors of all the words seen in the
  training data set that were not stop words and that occurred at least 3
  times;
* Write a logistic regression classifier that uses the data sets, maybe in
  Octave. Support Vector Machines are supposed to be very good at this kind of
  problem and allow you to use vectors with very large dimensions, which is
  good for text classification for obvious reasons;
* Write up some scripts that train all 47000 tags or so that we have on a
  batch job;
* Stick the classifiers onto some boxes on AWS, stick a REST API in front.
