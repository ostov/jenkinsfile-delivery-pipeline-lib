def call(Map config [:]) {
  // String config.envName
  // int config.timeOut
  // int config.lockMilestone
  // int config.doneMilestone
  def stageName = "Deploy to ${config.envName}"
  stage(stageName) {
    options {
      skipDefaultCheckout()
      timeout(time: config.timeOut, unit: 'MINUTES')
    }
    agent { label config.envName }
    steps {
      lock(resource: config.envName, inversePrecedence: true) {
        milestone 2
        script { currentBuild.displayName = "${env.APP_VERSION}.${env.APP_BUILD}" }
        cleanWs()
        copyArtifacts filter: 'artifact.txt', fingerprintArtifacts: true, projectName: env.JOB_NAME, selector: specific(env.BUILD_NUMBER)
        echo "Deploying ${env.APP_VERSION}.${env.APP_BUILD} to ${config.envName}"
        sh '''
          echo "[INFO] Deploying to ${config.envName} ..."
          cat artifact.txt
        '''
      }
      milestone 3
    }
  }
}
