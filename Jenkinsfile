pipeline {
    agent any // Utilise n'importe quel agent disponible

    stages {
        stage('Clone Repository') {
            steps {
                // Cloner le projet depuis GitHub
                git url: 'https://github.com/BrodyGaudel/backend.git', branch: 'main' // Remplacez par votre URL et branche
                dir('backend') {
                  sh 'pwd'
                }
            }
        }

        stage('Maven Clean and Install') {
            steps {
                script {
                    // Exécuter mvn clean install
                    sh 'mvn clean install'
                }
            }
        }

        stage('Maven Package') {
            steps {
                script {
                    // Exécuter mvn package
                    sh 'mvn package'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    // Exécuter mvn sonar:sonar
                    sh 'mvn sonar:sonar'
                }
            }
        }
    }

    post {
        success {
            echo 'Build and SonarQube analysis completed successfully!'
        }
        failure {
            echo 'Build or SonarQube analysis failed.'
        }
    }
}