# Tag Classifier

A naive Bayesian classifier, which recommends tags for The Guardian articles based on what previous articles with
similar features have been tagged.

A work in progress ...

## TODO

* Write something that downloads a decent representation of our content from Content API
* Write something that trains the classifier based on a section of that content, then scores it on a test data set
* Get this all working with a database backend (MongoDB maybe)
* Write a micro app that watches Content API for newly published articles (NOT updated) and automatically updates the backend
* Stick a REST API in front