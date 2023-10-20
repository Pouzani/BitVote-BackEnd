pipeline {
    agent any
    tools {
        // Utilisation de l'outil Maven
        maven 'Maven'
        dockerTool 'Docker'
    }
    stages {

        stage("test"){
            steps {
                script {
                    echo "testing the app ..."
                    sh 'mvn test'
                }
            }
        }

        stage("build jar") {
            steps {
                script {
                    echo "building the app ..."
                    sh 'mvn package -DskipTests'
                }
            }
        }

        stage("build image") {
            steps {
                script {
                    echo "building the docker image ..."
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        sh 'docker build -t pihix/bitvote-app:1.2 .'
                        sh "echo $PASS | docker login -u $USER --password-stdin"
                        sh 'docker push pihix/bitvote-app:1.2'
                    }
                }
            }
        }

        stage("deploy image") {
            steps {
                script {
                    echo "deploy the image ..."

                    // Define the name of the previous Docker container
                    def containerName = 'bitvote-container'
                    
                    // Stop the previous container if it's running
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@13.39.82.122 docker ps -q --filter name=${containerName} | xargs -r docker stop"

                    
                    // Start the new Docker container
                    def dockerCmd = "docker run -p 8082:8082 -d --name ${containerName} pihix/bitvote-app:1.2"
                    //On doit se connecter à dockerhub dans le serveur
                    sshagent(['ec2-dev-server']) {
                        sh "ssh -o StrictHostKeyChecking=no ubuntu@13.39.82.122 ${dockerCmd}"
                }
            }
        }
    }   
}
    }
