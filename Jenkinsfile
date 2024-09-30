pipeline {
    agent any

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
    }
}