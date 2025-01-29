pipeline {
    agent {
        docker {
            image 'maven:3.9.9-amazoncorretto-21-alpine'
            args '-v /root/.m2:/root/.m2'
        }
    }

    environment {
        GIT_REPO_URL = 'https://github.com/Rapter1990/flightsearchapi.git'
        BRANCH_NAME = 'development/issue-2/implement-jenkins-for-ci-cd'
        DOCKERHUB_USERNAME = 'noyandocker'
        DOCKER_IMAGE_NAME = 'flightsearchapi-jenkins'
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: "*/${env.BRANCH_NAME}"]],
                        userRemoteConfigs: [[url: "${env.GIT_REPO_URL}"]]
                    ])
                }
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${env.DOCKERHUB_USERNAME}/${env.DOCKER_IMAGE_NAME}:latest ."
            }
        }

        stage('Push Docker Image') {
            steps {
                withDockerRegistry([credentialsId: 'docker-hub-credentials', url: '']) {
                    sh "docker push ${env.DOCKERHUB_USERNAME}/${env.DOCKER_IMAGE_NAME}:latest"
                }
            }
        }

    }

    post {
            always {
                cleanWs(cleanWhenNotBuilt: false,
                        deleteDirs: true,
                        disableDeferredWipeout: true,
                        notFailBuild: true,
                        patterns: [[pattern: '.gitignore', type: 'INCLUDE'],
                                   [pattern: '.propsfile', type: 'EXCLUDE']])
            }
        }
}