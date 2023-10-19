pipeline {
    agent any
    tools {
        // Utilisation de l'outil Maven
        maven 'Maven'
        docker 'Docker'
    }
    stages {

        stage("build jar") {
            steps {
                script {
                    echo "building the app ..."
                    sh 'mvn package'
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
