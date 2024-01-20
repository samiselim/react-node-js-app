def gv

pipeline {
    agent any
    tools{
        maven 'Maven'
    }
    stages {
        stage("init") {
            steps {
                script { 
                    gv = load "script.groovy"
                }
            }
        }
        stage("build image") {
            steps {
                script {
                    echo "building image"
                    gv.buildImage()
                }
            }
        }
        stage("deploy") {
            steps {
                script {
                    echo "deploying"
                    gv.deployApp()
                }
            }
        }

    }
  
}