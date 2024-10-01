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
        stage('ANALYSIS') {
             steps {
                 dir('backend') {
                     withSonarQubeEnv('SonarQube') {
                        sh 'mvn sonar:sonar'
                     }
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
        stage('DEPLOY') {
            steps {
                dir('backend') {
                    sh 'mvn deploy -DskipTests -Dusername=admin -Dpassword=devops'
                }
            }
        }
        stage('BUILD IMAGE') {
            steps {
                dir('backend') {
                    sh 'docker build -t backend-image:latest .'
                }
            }
        }
        stage('RUN CONTAINER') {
            steps {
                dir('backend') {
                    sh 'docker stop backend-container || true && docker rm backend-container || true'

                    sh 'docker run -d --name backend-container --network devops-network -p 8888:8888 backend-image:latest'
                }
            }
        }
    }

    post {
        // Checking the SonarQube result after the analysis
        always {
            script {
                // Wait for the SonarQube analysis to complete
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }
}