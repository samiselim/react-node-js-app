
def buildImage() {
    echo "building the docker image..."
    int ver = 0
    withCredentials([usernamePassword(credentialsId: 'sami_docker_hub_credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh "docker build -t samiselim/node-app:v1.0 ."
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh "docker push samiselim/node-app:v1.0"
    }
} 

def deployApp() {
    echo 'deploying the application...'
    def dockerCmd = "docker run -d -p 3080:3080 samiselim/node-app:v1.0"
    sshagent(['ec2-server-cred']) {
        sh "ssh -o StrictHostKeyChecking=no ec2-user@54.93.142.184 ${dockerCmd}"
    }
} 

return this