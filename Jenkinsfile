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
                        //sh 'docker pull pihix/bitvote-app:1.2'
                        sh 'docker push pihix/bitvote-app:1.2'
                    }
                }
            }
        }

        stage("deploy image") {
            steps {
                script {
                    // Specify the port you want to check (e.g., 8080)
                    //def portToCheck = 8080
                    
                    // Check if there's a running container on the specified port
                    //sh "docker ps -q --filter \"publish=8080/tcp\""
                    
                    
                    //echo "A container is running on port ${portToCheck}. Stopping it..."
                    //sh 'docker stop $(docker ps -q --filter "publish=8080/tcp")'
                    sh '''
                        docker ps -q --filter "publish=8080/tcp"
                        if [ $? -eq 8080 ]; then
                            echo "Found running containers on port 8080. Stopping them..."
                            docker stop $(docker ps -q --filter "publish=8080/tcp")
                        else
                            echo "No containers found on port 8080."
                        fi
                        '''

                    
                    
                    echo "deploy the image ..."
                    echo "push event ..."
                    // Start the new Docker container
                    def dockerCmd = "docker run -p 8080:8080 -d pihix/bitvote-app:1.2"
                    //On doit se connecter à dockerhub dans le serveur
                    sshagent(['ec2-dev-server']) {
                        sh "ssh -o StrictHostKeyChecking=no ubuntu@13.39.82.122 ${dockerCmd}"
                }
            }
        }
    }   
}
    }
