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
                    sh '''
                        mvn deploy -X -e \
                        -DaltDeploymentRepository=nexus-releases::http://192.168.87.128:8081/repository/maven-releases/ \
                        -DskipTests \
                        -Dusername=admin \
                        -Dpassword=devops
                    '''
                }
            }
        }

    }

    post {
        // Vérification du résultat SonarQube après l'analyse
        always {
            script {
                // Attendre que l'analyse SonarQube soit terminée
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }
}