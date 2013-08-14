JavaPOS

this package is configured for maven [http://maven.apache.org].

Build jar, source.jar and javadoc.jar :
  $ mvn package javadoc:jar source:jar

Install in your repository
  $ mvn install

Help :
  $ mvn help:describe -Dplugin=org.apache.maven.plugins:maven-`[plugin]`-plugin

  $ mvn help:describe -Dplugin=org.apache.maven.plugins:maven-source-plugin
  $ mvn help:describe -Dplugin=org.apache.maven.plugins:maven-javadoc-plugin
  $ mvn help:describe -Dplugin=org.apache.maven.plugins:maven-assembly-plugin


