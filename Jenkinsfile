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
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-repo', passwordVariable: 'PASSWORD', usernameVariable: 'USER')]) {
                        sh 'docker build -t pihix/bitvote-app:1.3 .'
                        sh 'docker login -u $USER -p $PASSWORD'
                        //sh "echo $PASSWORD | docker login -u $USER --password-stdin"
                        sh 'docker push pihix/bitvote-app:1.3'
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
                   // sh "ssh -o StrictHostKeyChecking=no ubuntu@13.39.82.122 docker stop ${containerName}"

                    // Start the new Docker container
                    def dockerCmd = "docker run -p 8080:8080 -d --name bitvote-container pihix/bitvote-app:1.3"
                    //On doit se connecter à dockerhub dans le serveur
                    sshagent(['ec2-dev-server']) {
                        sh "ssh -o StrictHostKeyChecking=no ubuntu@13.39.82.122 ${dockerCmd}"
                }
            }
        }
    }   
}
    }
