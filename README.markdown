# Tag Classifier

A supervised learning classifier, which recommends tags for The Guardian articles based on what previous articles with
similar features have been tagged.

A work in progress ...

## TODO

* Get `tag-miner` to constantly process tags, training models with
  `liblinear`, and uploading the models to S3;
* Write new app `classifier-api`, which downloads the models, and is provides
  a REST endpoint that accepts document bodies and returns a list of tags that
  the classifier thinks should be appended.


