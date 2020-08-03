vn test -DskipTests=false -Dtest=EMailApiUnitTest# Spring-Boot Camel Java DSL

This example demonstrates how to configure Camel routes in Spring Boot via
a Spring XML configuration file.

## Building

The example can be built with

    mvn clean install

##  Running JUnit Test
	mvn test -DskipTests=false -Dtest=*UnitTest
	
### Running the Quickstart standalone on your machine

You can also run this quickstart as a standalone project directly:

Obtain the project and enter the project's directory
Build the project:

```
$ mvn clean package
$ mvn spring-boot:run 
```

### Running the Quickstart on OpenShift Cluster

The following steps assume you already have a Kubernetes / Openshift environment installed and relative tools like `oc`.
If you have a single-node OpenShift cluster, such as `Minishift`, you can also deploy your quickstart there. 
A single-node OpenShift cluster provides you with access to a cloud environment that is similar to a production environment.

**IMPORTANT**: You need to run this example on Container Development Kit 3.3 or OpenShift 3.7.
Both of these products have suitable Fuse images pre-installed. 
If you run it in an environment where those images are not preinstalled follow the steps described below.

+ Log in and create your project / namespace:
```
$ oc login -u developer -p developer
$ oc new-project MY_PROJECT_NAME
```

+ Build and deploy the project to the Kubernetes / OpenShift cluster:
```
$ mvn clean -DskipTests fabric8:deploy -Popenshift
```

### Running the Quickstart on OpenShift Cluster without preinstalled images

Following steps assume you already have a Kubernates / Openshift environment installed and relative tools like `oc`.
If you have a single-node OpenShift cluster, such as `Minishift`, you can also deploy your quickstart there. 
A single-node OpenShift cluster provides you with access to a cloud environment that is similar to a production environment.

+ Log in and create your project / namespace:
```
$ oc login -u developer -p developer
$ oc new-project MY_PROJECT_NAME
```

+ Import base images in your newly created project (MY_PROJECT_NAME):
```
$ oc import-image fis-java-openshift:2.0 --from=registry.access.redhat.com/jboss-fuse-6/fis-java-openshift:2.0 --confirm
```

+ Build and deploy the project to the OpenShift cluster:
```
$ mvn clean -DskipTests fabric8:deploy -Popenshift -Dfabric8.generator.fromMode=istag -Dfabric8.generator.from=MY_PROJECT_NAME/fis-java-openshift:2.0
```

### Payload Example

`*{
  *`"from": "jakspok@gmail.com",`*
  *`"subject": "Prueba",`*
  *`"recipients": [`*
    *`{`*
      *`"name": "Tesy",`*
      *`"email": "jakspok@gmail.com",`*
      *`"cc": "jakspok@gmail.com"`*
    *`},`*
    *`{`*
      *`"name": "Tesy",`*
      *`"email": "axxxaxaxa@gmail.com",`*
      *`"cc": "jakspok@gmail.com"`*
    *`}`*
  *`],`*
  *`"message": "${MAIL_MESSAGE}<p><strong>Pellentesque habitant morbi tristique</strong> senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. <em>Aenean ultricies mi vitae est.</em> Mauris placerat eleifend leo. Quisque sit amet est et sapien ullamcorper pharetra. Vestibulum erat wisi, condimentum sed, <code>commodo vitae</code>, ornare sit amet, wisi. Aenean fermentum, elit eget tincidunt condimentum, eros ipsum rutrum orci, sagittis tempus lacus enim ac dui. <a href=\"#\">Donec non enim</a> in turpis pulvinar facilisis. Ut felis.</p>",`*
  *`"isTemplate": "true",`*
  *`"isHtml": "true",`*
  *`"attachments": null,`*
  *`"parameters" : [{`*
                     *`"key" : "MAIL_MESSAGE",`*
                     *`"value" : "Texto indicado por parametro"`*
                     *`},`*
                     *`{ "key" : "msg",`*
                         *`"value" : "09e809rjewifi90wf"`*
                     *`}]`*
  }*`