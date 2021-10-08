def call(Map config = [:]) {

    // String config.envName
    // String config.testName
    // int config.lockMilestone
    // String config.gitURL

    lock(resource: config.envName, inversePrecedence: true) {
        milestone config.lockMilestone
        checkoutScmGit(config.gitURL)
        script {
            currentBuild.displayName = "${env.APP_VERSION}.${env.APP_BUILD}"
            env.ENV_NAME = "${config.envName}"
            env.TEST_NAME= "${config.testName}"
        }
        echo "Testing ${env.APP_VERSION}.${env.APP_BUILD} on ${config.envName}"
        sh '''
            echo "[INFO] Testing ${APP_VERSION}.${APP_BUILD} on ${ENV_NAME} ..."
            mkdir -p target/test-results/test
            chmod +x run_tests.sh
            ./run_tests.sh FooBar
        '''
    }
}
