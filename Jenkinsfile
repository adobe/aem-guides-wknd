pipeline {
    agent any
    stages {
        stage("Gather Advance Parameters") {
            steps {
                timeout(time: 30, unit: 'SECONDS') {
                    script {
                        // Show the select input modal
                        def INPUT_PARAMS = input message: 'Override metrics', ok: 'Override',
                                parameters: [
                                        choice(name: 'METRIC_1', choices: ['Yes','No'].join('\n'), description: 'Override metric 1'),
                                        choice(name: 'METRIC_2', choices: ['Yes','No'].join('\n'), description: 'Override metric 2')]
                        env.OVERRIDE = [INPUT_PARAMS.METRIC_1, INPUT_PARAMS.METRIC_2].join('\n')
                    }
                }
            }
        }
        stage("Use Advance Parameters") {
            steps {
                script {
                    echo "Overreide: ${env.OVERRIDE}"
                }
            }
        }
    }
}