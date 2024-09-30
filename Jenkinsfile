pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials'
        DOCKER_HUB_REPO = 'mounanga'
        KUBECONFIG_CREDENTIALS_ID = 'kubeconfig-credentials'
    }

    stages {
        stage('CLONE') {
            steps {
                sh 'git clone https://github.com/BrodyGaudel/backend.git'
            }
        }
        stage('INSTALL') {
            steps {
                dir('backend') {
                    sh 'mvn clean install -DskipTests'
                }
            }
        }
        stage('TEST') {
            steps {
                dir('backend') {
                    sh 'mvn test'
                }
            }
        }
        stage('PACKAGE') {
            steps {
                dir('backend') {
                    sh 'mvn package -DskipTests'
                }
            }
        }
        stage('ANALYSE') {
            steps {
                dir('backend') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
    }
}