FROM jenkins/jenkins:lts

# Plugin listesi
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

# Groovy script'leri için init.d dizini
COPY init.groovy.d/ /var/jenkins_home/init.groovy.d/