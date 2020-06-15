#!groovy
node {
    withCredentials([usernamePassword(credentialsId: 'cm-creds', passwordVariable: 'pass', usernameVariable: 'user')]) {
        // the code in here can access $pass and $user
        stage('Git Checkout branch') {
            sh """
                         echo "Checking out ${env.BRANCH_NAME}"
                         git checkout -f ${env.BRANCH_NAME}
                 """
        }

        stage('Push to Cloud Manager Repository') {
            sh """
                         echo "Adding CM git repo remote"
                         git remote add cm-repo "https://$user:$pass@git.cloudmanager.adobe.com/emeaaem/Hackathon-EMEAAEMConsultingProgram-p13954"

                         echo "Pushing to CM repo"
                         git push -f cm-repo ${env.BRANCH_NAME}:master

                         echo "Remove CM repo remote reference"
                         git remote rm cm-repo
                 """
        }
        stage("Start Cloud Manager Build") {
            step([$class: 'CloudManagerBuilder', pipeline: '626552', program: '13954'])
        }
        stage("Gather Advance Parameters") {
            timeout(time: 30, unit: 'SECONDS') {
                script {
                    // Show the select input modal
                    def INPUT_PARAMS = input message: 'Override metrics', ok: 'Override',
                            parameters: [
                                    choice(name: 'METRIC_1', choices: ['Yes', 'No'].join('\n'), description: 'Override metric 1'),
                                    choice(name: 'METRIC_2', choices: ['Yes', 'No'].join('\n'), description: 'Override metric 2')]
                    env.OVERRIDE = [INPUT_PARAMS.METRIC_1, INPUT_PARAMS.METRIC_2].join('\n')
                }
            }
        }
        stage("Use Advance Parameters") {
            script {
                echo "Override: ${env.OVERRIDE}"
            }
        }
    }
}
