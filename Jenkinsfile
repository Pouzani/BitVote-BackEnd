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
                        sh 'docker build -t pihix/taxi-app:1.0 .'
                        sh "echo $PASS | docker login -u $USER --password-stdin"
                        sh 'docker push pihix/taxi-app:1.0'
                    }
                }
            }
        }

        stage("deploy image") {
            steps {
                script {
                    echo "deploy the image ..."
                }
            }
        }
    }   
}
