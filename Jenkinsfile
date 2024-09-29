pipeline {
    agent any

    environment {
        // Variables for Nexus and Docker
        MAVEN_HOME = tool 'Maven' // Specify your Maven installation tool name
        DOCKER_REGISTRY = 'your-docker-registry.com'
        DOCKER_IMAGE_NAME = "your-docker-registry.com/your-organization/product-backend"
        DOCKER_CREDENTIALS_ID = 'docker-credentials-id'
        NEXUS_URL = 'http://your-nexus-repository.com'
        NEXUS_REPO = 'your-nexus-repo'
        NEXUS_CREDENTIALS_ID = 'nexus-credentials-id'

        // SonarQube configuration
        SONAR_URL = 'http://your-sonarqube.com'
        SONAR_CREDENTIALS_ID = 'sonar-credentials-id'
        SONAR_PROJECT_KEY = 'your-project-key'
        SONAR_PROJECT_NAME = 'product-backend'

        // Kubernetes configuration
        KUBECONFIG_CREDENTIALS_ID = 'kubeconfig-credentials-id'
    }

    stages {
        stage('Checkout') {
            steps {
                // Clone the GitHub repository
                git credentialsId: 'github-credentials-id', url: 'https://github.com/your-organization/your-repo.git'
            }
        }

        stage('Build') {
            steps {
                // Use Maven to build the project
                sh "${MAVEN_HOME}/bin/mvn clean package -DskipTests"
            }
        }

        stage('Run Unit Tests') {
            steps {
                // Run unit tests using Maven
                sh "${MAVEN_HOME}/bin/mvn test"
            }
        }

        stage('SonarQube Analysis') {
            environment {
                // SonarQube environment variables
                scannerHome = tool 'SonarQube Scanner'
            }
            steps {
                // Run SonarQube code analysis
                withSonarQubeEnv('SonarQube') {
                    sh "${MAVEN_HOME}/bin/mvn sonar:sonar " +
                       "-Dsonar.projectKey=${SONAR_PROJECT_KEY} " +
                       "-Dsonar.projectName=${SONAR_PROJECT_NAME} " +
                       "-Dsonar.host.url=${SONAR_URL} " +
                       "-Dsonar.login=${SONAR_CREDENTIALS_ID}"
                }
            }
        }

        stage('Publish to Nexus') {
            steps {
                // Upload the JAR to Nexus Repository
                sh "${MAVEN_HOME}/bin/mvn deploy -DaltDeploymentRepository=nexus::default::${NEXUS_URL}/repository/${NEXUS_REPO} " +
                   "-DskipTests=true"
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image
                    sh "docker build -t ${DOCKER_IMAGE_NAME}:${env.BUILD_ID} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    // Push Docker image to registry
                    withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh "echo $DOCKER_PASS | docker login ${DOCKER_REGISTRY} -u $DOCKER_USER --password-stdin"
                        sh "docker push ${DOCKER_IMAGE_NAME}:${env.BUILD_ID}"
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Set up Kubernetes environment
                    withCredentials([file(credentialsId: "${KUBECONFIG_CREDENTIALS_ID}", variable: 'KUBECONFIG')]) {
                        // Deploy the Docker image to Kubernetes
                        sh "kubectl set image deployment/product-backend product-backend=${DOCKER_IMAGE_NAME}:${env.BUILD_ID} --record"
                    }
                }
            }
        }
    }

    post {
        always {
            // Clean up any Docker images created during the build
            sh 'docker rmi ${DOCKER_IMAGE_NAME}:${env.BUILD_ID} || true'
        }
        success {
            echo 'Pipeline finished successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}