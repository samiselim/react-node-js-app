
def buildImage() {
    echo "building the docker image..."
    def ver = 0
    withCredentials([usernamePassword(credentialsId: 'sami_docker_hub_credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh "docker build -t samiselim/node-app:v${ver} ."
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh "docker push samiselim/node-app:v${ver}"
    }
} 
def incVersion(){
    ver +=1
}

def deployApp() {
    echo 'deploying the application...'
    def dockerCmd = "docker run -d -p 3080:3080 samiselim/node-app:v${ver}"
    sshagent(['ec2-server-cred']) {
        sh "ssh -o StrictHostKeyChecking=no ec2-user@54.93.142.184 ${dockerCmd}"
    }
} 


def commitChanges(){
    echo "Committing changes to github repository"
     withCredentials([usernamePassword(credentialsId: 'sami_githubAcess', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        
        sh "git remote set-url origin https://${USER}:${PASS}@github.com/samiselim/react-node-js-app.git"
        sh 'git add .'
        sh 'git commit -m "this commit from jenkins "'
        sh 'git push origin HEAD:update'

    }
}

return this