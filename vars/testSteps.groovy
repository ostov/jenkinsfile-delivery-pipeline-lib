def call(Map config = [:]) {

    // String config.envName
    // String config.testName
    // int config.lockMilestone

    lock(resource: config.envName, inversePrecedence: true) {
        milestone config.lockMilestone
        checkout changelog: false, scm: [$class: 'GitSCM',
            branches: [[name: "refs/heads/master"]],
            doGenerateSubmoduleConfigurations: false,
            extensions: [[$class: 'WipeWorkspace']],
            submoduleCfg: [],
            userRemoteConfigs: [[
                url: 'https://github.com/ostov/jenkinsfile-delivery-pipeline.git',
                refspec: '+refs/heads/master:refs/remotes/origin/master',
            ]]
        ]
        script {
            currentBuild.displayName = "${env.APP_VERSION}.${env.APP_BUILD}"
            env.ENV_NAME = "${config.envName}"
            env.TEST_NAME= "${config.testName}"
        }

        echo "Testing ${env.APP_VERSION}.${env.APP_BUILD} on ${config.envName}"
        sh '''
            echo "[INFO] Testing ${APP_VERSION}.${APP_BUILD} on ${ENV_NAME} ..."
            ./run_tests.sh ${TEST_NAME}
        '''
    }
}
