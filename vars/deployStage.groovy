def call(Map config = [:]) {

    // String config.envName
    // int config.timeOut
    // int config.lockMilestone
    // int config.doneMilestone

    stage("Deploy to ${config.envName}") {
        options {
            skipDefaultCheckout()
            timeout(time: config.timeOut, unit: 'MINUTES')
        }
        agent { label config.envName }
        steps {
            lock(resource: config.envName, inversePrecedence: true) {
                milestone config.lockMilestone
                script {
                    currentBuild.displayName = "${env.APP_VERSION}.${env.APP_BUILD}"
                    env.ENV_NAME = "${config.envName}"
                }
                cleanWs()
                copyArtifacts filter: 'artifact.txt', fingerprintArtifacts: true, projectName: env.JOB_NAME, selector: specific(env.BUILD_NUMBER)
                echo "Deploying ${env.APP_VERSION}.${env.APP_BUILD} to ${config.envName}"
                sh '''
                    echo "[INFO] Deploying ${APP_VERSION}.${APP_BUILD} to ${ENV_NAME} ..."
                    cat artifact.txt
                '''
            }
            milestone config.doneMilestone
        }
    }
}
