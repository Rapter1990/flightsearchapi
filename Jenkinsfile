pipeline {
    agent any

    environment {
        GIT_REPO_URL = 'https://github.com/Rapter1990/flightsearchapi.git'
        BRANCH_NAME = 'development/issue-2/implement-jenkins-for-ci-cd'
        DOCKERHUB_USERNAME = 'noyandocker'
        DOCKER_IMAGE_NAME = 'flightsearchapi-jenkins'
        KIND_CLUSTER_NAME = 'jenkins-cluster'
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
            agent {
                    docker {
                        image 'maven:3.9.9-amazoncorretto-21-alpine'
                    }
                }
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Build Docker Image') {
            agent {
                docker {
                    image 'docker:27.5.1'
                }
            }
            steps {
                sh "docker build -t ${env.DOCKERHUB_USERNAME}/${env.DOCKER_IMAGE_NAME}:latest ."
            }
        }

        stage('Push Docker Image') {
            agent {
                docker {
                    image 'docker:27.5.1'
                }
            }
            steps {
                withDockerRegistry([credentialsId: 'docker-hub-credentials', url: '']) {
                    sh "docker push ${env.DOCKERHUB_USERNAME}/${env.DOCKER_IMAGE_NAME}:latest"
                }
            }
        }

        stage('Setup Kind Cluster') {
            steps {
                script {
                    sh """
                    if ! command -v kind &> /dev/null; then
                        echo 'Installing Kind...'
                        curl -Lo ./kind https://kind.sigs.k8s.io/dl/latest/kind-linux-amd64
                        chmod +x ./kind
                        mv ./kind /usr/local/bin/kind
                    fi

                    if ! command -v kubectl &> /dev/null; then
                        echo 'Installing kubectl...'
                        curl -LO "https://dl.k8s.io/release/v1.28.0/bin/linux/amd64/kubectl"
                        chmod +x kubectl
                        mv kubectl /usr/local/bin/kubectl
                    fi

                    echo 'Checking if Kind cluster exists...'
                    if ! kind get clusters | grep -q "${env.KIND_CLUSTER_NAME}"; then
                        echo 'Creating Kind Cluster...'
                        kind create cluster --name "${env.KIND_CLUSTER_NAME}"
                    else
                        echo 'Kind cluster already exists.'
                    fi

                    echo 'Setting up kubeconfig...'
                    export KUBECONFIG="\$(kind get kubeconfig-path --name="${env.KIND_CLUSTER_NAME}")"
                    kind export kubeconfig --name="${env.KIND_CLUSTER_NAME}"

                    echo 'Verifying cluster connectivity...'
                    kubectl cluster-info || echo 'Failed to retrieve cluster info'

                    echo 'Dumping cluster info for debugging...'
                    kubectl cluster-info dump || echo 'Failed to dump cluster info'

                    echo 'Listing nodes...'
                    kubectl get nodes || echo 'Failed to list nodes'

                    """
                }
            }
        }

        stage('Deploy to Kind') {
            steps {
                script {
                    sh """
                    echo 'Applying Kubernetes manifests...'
                    kubectl apply -f k8s
                    """
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