srcgen4j-core
=============

Source code generation for Java (Core)

__CAUTION: *Project is in early stage (Work in progress)*__

[![Build Status](https://fuin-org.ci.cloudbees.com/job/srcgen4j-core/badge/icon)](https://fuin-org.ci.cloudbees.com/job/srcgen4j-core/)
[![Coverage Status](https://sonarcloud.io/api/project_badges/measure?project=org.fuin.srcgen4j%3Asrcgen4j-core&metric=coverage)](https://sonarcloud.io/dashboard?id=org.fuin.srcgen4j%3Asrcgen4j-core)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.fuin.srcgen4j/srcgen4j-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.fuin.srcgen4j/srcgen4j-core/)
[![LGPLv3 License](http://img.shields.io/badge/license-LGPLv3-blue.svg)](https://www.gnu.org/licenses/lgpl.html)
[![Java Development Kit 1.8](https://img.shields.io/badge/JDK-1.8-green.svg)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

<a href="https://fuin-org.ci.cloudbees.com/job/srcgen4j-core"><img src="http://www.fuin.org/images/Button-Built-on-CB-1.png" width="213" height="72" border="0" alt="Built on CloudBees"/></a>

What is this?
-------------

The project provides some parsers and generators based on the ([srcgen4j-common](https://github.com/fuinorg/srcgen4j-common/)) project. 

* *XtextParser* Parses [Xtext](https://eclipse.org/Xtext/) models.
* *EMFGenerator* Generates content based on an ECORE ResourceSet (for example created with [Xtext](https://eclipse.org/Xtext/)). 
* *ParameterizedTemplateParser* Parses a given directory for XML files of type [ParameterizedTemplateModel](https://github.com/fuinorg/srcgen4j-core/blob/master/src/main/java/org/fuin/srcgen4j/core/velocity/ParameterizedTemplateModel.java) or [ParameterizedTemplateModels](https://github.com/fuinorg/srcgen4j-core/blob/master/src/main/java/org/fuin/srcgen4j/core/velocity/ParameterizedTemplateModels.java) and combines all files into one model.
* *ParameterizedTemplateGenerator* Generates files for a model from the *ParameterizedTemplateParser* using the [Velocity template engine](http://velocity.apache.org/).

XtextParser
-----------
The parser is configured with the path where the model files with a dedicated extension can be found. 
The setup class attribute is used to instantiate the [Xtext](https://eclipse.org/Xtext/)) parser itself.
```xml
<parser name="ptp" class="org.fuin.srcgen4j.core.xtext.XtextParser">
    <config>
        <xtext:xtext-parser-config modelPath="${testRes}" modelExt="xsdsl"
               setupClass="org.fuin.xsample.XSampleDslStandaloneSetup" />
    </config>
</parser>
```

A full blown example for the Xtext based [DDD DSL](https://github.com/fuinorg/org.fuin.dsl.ddd/) can be found [here](https://github.com/fuinorg/org.fuin.dsl.ddd/tree/master/ddd-dsl-test). 


EMFGenerator
------------
The EMF generator requires setting up the different [artifact factories](https://github.com/fuinorg/srcgen4j-commons/blob/master/src/main/java/org/fuin/srcgen4j/commons/ArtifactFactory.java) that generate code for different EMF model elements.
```xml
<generator name="gen1" class="org.fuin.srcgen4j.core.emf.EMFGenerator" parser="ptp" project="current">
    <config>
        <emf:emf-generator-config>
            <emf:artifact-factory artifact="abstractHello" class="org.fuin.srcgen4j.core.emf.AbstractHelloTstGen">
                <variable name="package" value="a.b.c" />
            </emf:artifact-factory>
        </emf:emf-generator-config>
    </config>
    <artifact name="abstractHello" folder="testGenMainJava" />
</generator>
```
You can also define local variables that will be provided to the artifact factory. 

ParameterizedTemplateParser
---------------------------
The parser is configured with the path where the model files can be found.  
```xml
<parser name="ptp" class="org.fuin.srcgen4j.core.velocity.ParameterizedTemplateParser">
    <config>
        <velo:parameterized-template-parser modelPath="${testRes}" 
                                            modelFilter=".*\.ptg\.xml"
                                            templatePath="${testRes}" 
                                            templateFilter=".*\.ptg\.java" />
    </config>
</parser>
```
A model element always consists of two parts: An XML definition and a velocity template for code generation.

An example template definition ([parameterized-template-1.ptg.xml](https://github.com/fuinorg/srcgen4j-core/blob/master/src/test/resources/parameterized-template-1.ptg.xml)):
```xml
<parameterized-template template="parameterized-template-1.ptg.java" xmlns="http://www.fuin.org/srcgen4j/core/velocity">
    
    <!-- Variables that can be used in the velocity template -->
    
    <arguments>
        <argument key="name" value="-" />
        <argument key="pkg" value="-" />
    </arguments>

    <!-- Files to be generated with constant values for the above defined variables -->
        
    <target-file path="a" name="A.java">
        <argument key="name" value="A" />
        <argument key="pkg" value="a" />
    </target-file>
    
    <target-file path="b" name="B.java">
        <argument key="name" value="B" />
        <argument key="pkg" value="b" />
    </target-file>
    
</parameterized-template>
```

An example velocity template ([parameterized-template-1.ptg.java](https://github.com/fuinorg/srcgen4j-core/blob/master/src/test/resources/parameterized-template-1.ptg.java)):
```java
package ${pkg};

public class ${name} {
    // Whatever
}
```

It's also possible to create the variable values programmatically (See [TestTFLProducer](https://github.com/fuinorg/srcgen4j-core/blob/master/src/test/java/org/fuin/srcgen4j/core/velocity/TestTFLProducer.java)):
```xml
<parameterized-template template="parameterized-template-2.ptg.java" xmlns="http://www.fuin.org/srcgen4j/core/velocity">
    
    <arguments>
        <argument key="name" value="-" />
        <argument key="pkg" value="-" />
    </arguments>

    <target-file-list-producer class="org.fuin.srcgen4j.core.velocity.TestTFLProducer" />
    
</parameterized-template>
```

ParameterizedTemplateGenerator
------------------------------
The generator is simply configured with the path to the to the velocity templates (See topic Resource Management / [file.resource.loader.path](http://velocity.apache.org/engine/2.0/configuration.html)).
xxx
```xml
<generator name="gen1" class="org.fuin.srcgen4j.core.velocity.ParameterizedTemplateGenerator" 
           parser="ptp" project="current" folder="testJava">
    <config>
        <velo:parameterized-template-generator templatePath="${testRes}" />
    </config>
    <artifact name="file" />
</generator>
```

- - - - - - - - -

Snapshots
=========

Snapshots can be found on the [OSS Sonatype Snapshots Repository](http://oss.sonatype.org/content/repositories/snapshots/org/fuin "Snapshot Repository"). 

Add the following to your .m2/settings.xml to enable snapshots in your Maven build:

```xml
<repository>
    <id>sonatype.oss.snapshots</id>
    <name>Sonatype OSS Snapshot Repository</name>
    <url>http://oss.sonatype.org/content/repositories/snapshots</url>
    <releases>
        <enabled>false</enabled>
    </releases>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
```
