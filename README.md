This is a simple library that can retrieve the list of installed applications in Windows.

It is based on [ListPrograms](https://github.com/mavenlin/ListPrograms) which is in turn based on a 
[VB Script](http://www.vbforums.com/showthread.php?598355-Example-of-how-to-get-a-list-of-installed-programs-(like-Add-and-Remove-Programs))
linked from a 
[StackOverflow Post](https://stackoverflow.com/questions/802499/how-can-i-enumerate-list-all-installed-applications-in-windows-xp/9757013#comment22371865_9757013)

My purpose is fairly limited, so haven't replicated all of the functionality. In particular, the following is still to 
be done
* `GetUserInstallerKeyPrograms` has not been migrated
* Not all the programs will be detected if the JVM is 32 bit running on a 64bit windows 
(http://symbolthree.blogspot.com/2016/11/access-windows-registry-using-jna.html)

# Installation

This module is not in the maven repositories, but it should be very easy to install it locally

    $ git clone https://github.com/drone-ah/JavaListApps
    $ cd JavaListApps
    $ mvn install

You can then include it in your pom.xml

    <dependency>
        <groupId>com.droneah</groupId>
        <artifactId>list-apps</artifactId>
        <version>0.1.0-SNAPSHOT</version>
    </dependency>

# Usage

The usage is very simple
    
    Map<String, Software> list = ListApps.getInstalledApps(false);

    for(Software soft: list.values()) {
        System.out.println(soft.getDisplayName() + ": " + soft.getPublisher());
    }
