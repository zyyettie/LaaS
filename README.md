LaaS - Log as a Service
----------
Powerful log analysis platform.

#### Prerequisites

- JDK 1.7+ [http://www.oracle.com/technetwork/java/javase/overview/index.html](http://www.oracle.com/technetwork/java/javase/overview/index.html)
- Maven 3.0+ [http://maven.apache.org](http://maven.apache.org)

#### Maven build from Source
       $ git clone https://github.com/zyyettie/LaaS.git
       $ mvn clean install

#### Development Environment
##### Intellij IDEA 
       $ mvn clean install
       $ mvn idea:idea
##### Eclipse
       $ mvn clean install
       $ mvn eclipse:eclipse -DdownloadSource=true

#### Detailed steps
1. Register github account and request to join the code contributor list

2. In <maven root folder>\conf

Open settings.xml

Add

    <proxy>
      <id>https</id>
      <active>true</active>
      <protocol>https</protocol>
      <username></username>
      <password></password>
      <host><proxy host></host>
      <port>8080</port>
      <nonProxyHosts>localhost</nonProxyHosts>
    </proxy>
    <proxy>
      <id>http</id>
      <active>true</active>
      <protocol>http</protocol>
      <username></username>
      <password></password>
      <host><proxy host></host>
      <port>8080</port>
      <nonProxyHosts>localhost</nonProxyHosts>
    </proxy>

3. In C:\Users\<NT account>\
1) Remove .m2\settings.xml

2) Create .gitconfig
[http]
	proxy = http://<proxy host>:8080
[https]
	proxy = http://<proxy host>:8080
[user]
	name = <Your preferred name alias>
	email = <github account>

4. In C:\git_root
       $ git clone https://github.com/zyyettie/CATLaaS.git

5. In C:\git_root\CATLaaS
       $ mvn clean install
       $ mvn idea:idea

6. idea.exe.vmoptions or idea64.exe.vmoptions
-Xms512m
-Xmx1024m

7. IDEA (better idea64.exe)
1) File > Setting

Quickly search "github", click Version Control > GitHub
Host: github.com
Login: <github account>
Password: ******

Click Test button

1) File > Setting

Quickly search "encoding", click File Encodings
IDE Encoding: UTF-8
Project Encoding: UTF-8

3) File > Project Structure

New 1.7 (java version "1.7.0_45") Project SDK