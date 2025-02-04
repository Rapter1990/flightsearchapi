FROM jenkins/jenkins:lts

# Plugin list
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

# For Groovy scripts, init.d directory
COPY init.groovy.d/ /var/jenkins_home/init.groovy.d/

# Install Docker CLI
USER root
RUN apt-get update && apt-get install -y docker.io

RUN curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
RUN install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl