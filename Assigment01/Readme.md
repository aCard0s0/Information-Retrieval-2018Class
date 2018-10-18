# STROAM Frontend

This is the Authentication Proxy Server of the STROAM system. It was build to protect the STROAM services.
Built with Springboot Security, using MySQL database.

## What is already done
This section will be updated as soon as development goes on.
* 
* implement interface for tokenizer;
* 

## TODO
This section will be updated as soon as development goes on.
* Document Result: create stucture associate docId to a customer_id review_id from sample;
* Read files Doc by Doc;
* run program with arg to chose tokenizer type;
* Chose rules to "new" tokenizer;
* Desacopular "indexer" function;
* Implement Stemmer and Stopwords;
* Write segments in disk;
* Merge posting list segments;

### How to run (locally)

* Install Java 1.8

* Install Maven or use the wrapper "mvnw" file

* Install Java libraries
```
$ mvn install
```

* Start Project
```
$ mvn spting-boot:run
```

* Build

* Publish

* Go to http://localhost:65069 or http://127.0.0.1:65069

### Author
* **André Cardoso** - [GitHub](https://github.com/aCard0s0)