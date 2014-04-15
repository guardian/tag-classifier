




P(A|B) = P(B|A) P(A)
         -----------
             P(B)
             
             
the probability of A tag given B triple is

probability of B triple given A tag *
probability of A tag

over

probability of B triple






need to be able to look up

* total number of articles seen

* for any given triple, how many articles include that triple
  
* for any given tag, how many articles include that tag
  
* for any given tag, how many times any given triple occurs

* for any given a triple, a set of tags that have occurred for that triple

initially, look up the total set of tags for all the triples ...




--

Ignore the above, we can (and should) just use a Naive Bayes Classifier.

We'll need to use Laplacian correction so that terms that we haven't recorded
for a particular tag previously don't just mean the probability of the whole
is set to zero.

For Laplacian correction see here: http://www.cs.nyu.edu/faculty/davise/ai/bayesText.html
