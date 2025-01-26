pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "noyandocker/flightsearchapi-jenkins"
        DOCKER_TAG = "latest"
        DOCKER_REGISTRY = "docker.io"
        MAVEN_HOME = tool name: 'Maven', type: 'maven' // Specify Maven tool configured in Jenkins
        JAVA_HOME = tool name: 'JDK 21', type: 'jdk'   // Specify JDK 21 tool configured in Jenkins
    }

    triggers {
        pollSCM('* * * * *') // Poll GitHub for changes every minute
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Checking out code...'
                checkout scm
            }
        }

        stage('Set up Maven and JDK') {
            steps {
                script {
                    env.PATH = "${MAVEN_HOME}/bin:${JAVA_HOME}/bin:${env.PATH}"
                }
                echo "Maven and JDK configured."
            }
        }

        stage('Build and Test') {
            steps {
                echo 'Building and testing the application...'
                sh 'mvn -B clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                sh """
                docker build -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG} .
                """
            }
        }

        stage('Push Docker Image') {
            steps {
                echo 'Pushing Docker image to Docker Hub...'
                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh """
                    echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin
                    docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}
                    """
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                echo 'Deploying to Kubernetes...'
                sh 'kubectl apply -f k8s'
            }
        }
    }

    post {
        always {
            node {
                echo 'Cleaning up...'
                sh 'docker rmi ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG} || true'
            }
        }
        success {
            echo 'Pipeline executed successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
