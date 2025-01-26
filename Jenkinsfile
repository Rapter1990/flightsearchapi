pipeline {
    agent {
        docker {
            image 'maven:3.8.5-openjdk-21'
            args '-v /root/.m2:/root/.m2'
        }
    }

    environment {
        GIT_REPO_URL = 'https://github.com/Rapter1990/flightsearchapi.git'
        BRANCH_NAME = 'development/issue-2/implement-jenkins-for-ci-cd'
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
    }

    post {
        always {
            cleanWs()
        }
    }
}