To facilitate deployment that uses different versions of the controls and JCL
we are now providing a distribution ZIP file that contains the following files
with explanation of their contents:

1) jpos<nnn>.jar --- the .class files for the controls and core JCL only and
   without the jpos.properties file.

2) jpos.properties --- this is the bootstrapping file for the JCL.  Since you
   might decide to have the JCL populated from XML, serialized or your own
   population (see JCL FAQ) then you need to either agree with the default
   (XML) and update the jpos<nnn>.jar file with this file as
   jpos/jpos.properties or make the necessary changes and update the JAR file.

3) jpos<nnn>-controls.jar --- contains .class files for controls ONLY.  You
   need to also put the jcl.jar from the JCL distribution in your CLASSPATH
   (see below for pointers to the JCL and controls distribution).


Using the files above we recommend the following deployment scenarios:

Deployment 1:
Add jpos<nnn>.jar to your CLASSPATH after making necessary update according to
item 1) and 2) above.  The advantage is simplicity (1 JAR) but the disadvantage
is that both the controls and JCL are at a fixed version and making updates to either
or both assumes that your get a jpos<nnn>.jar with the correct updates or create
one yourself from new controls and JCL.

Deployment 2:
Add BOTH jpos<nnn>-controls.jar and jcl.jar (from JCL distribution see Web
site below) to your CLASSPATH.  Disadvantage is that you now have 2 JARs on your
CLASSPATH but the advantage is that you can do drop in JAR replacement to update
either JCL or controls.

NOTE: we strongly recommend that you DO NOT add both jpos<nnn>.jar and jcl.jar
in your CLASSPATH since that may result in weird bootstrapping behaviors especially
if you place the JARs in the Java extension directory (e.g. jre/lib/ext) since
the order of class loading is not deterministic in such a configuration.

JavaPOS Specification and Controls: http://www.javapos.org
JCL: http://oss.software.ibm.com/developerworks/projects/jposloader

Controls Author: Brian Spohn
JCL Author: E. Michael Maximilien

