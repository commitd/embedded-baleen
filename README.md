# embedded-baleen

Library to allow embedding of Baleen in other applications (http://github.com/dstl/baleen)

This include a both a single threaded and pooled Baleens. 

The pool options is useful in the case you which you are servicing multiple requests (e.g. through a web interface) and wish to have 

Each Baleen in the pool has its own copy of all resources, etc. There is not shared knowledge. This also means that that a pool of N Baleens takes roughly N times more memory than a single Baleen. 

## Usage example

```
// A consumer which will return the text from Baleen
// Use consumer to convert the jCas into something you want to deal with in your application
BaleenOutputConverter<String> consumer = (context, jCas) -> Optional.of(jCas.getDocumentText());

// Provide Baleen Pipeline YAML as a string  
String yaml = ...; // read YAML from file or as a constant

// Initialise Baleen, a poolSize of 1 means it will be standalone
// any number higher will create multiple Baleen instances
int poolSize = 1;
EmbeddableBaleen baleen = EmbeddableBaleen.create("my-baleen", poolSize);
baleen.setup(yaml);

// Push documents through
// You must provide a source (string),
// the inputstream
// and the consumer as above
InputStream is = ...; 
Optional<String> text = baleen.process("file.txt", is, consumer)

// If optional is present then acces
if(text.isPresent()) {
   System.out.println(text.get());
}

// NOTE: handle BaleenException to process errors

```

## Health warning

Baleen was design to run a standalone application.

Baleen has an *enormous* set of dependencies, including artifacts such as Elasticsearch and Tika which have enormous number of dependencies in their own right.

Whilst this library will allow use to to use Baleen in another application's you might find that you have version clashes.
