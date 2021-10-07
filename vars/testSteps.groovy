def call(Map config = [:]) {

  // String config.envName
  // String config.testName
  // int config.lockMilestone

  lock(resource: config.envName, inversePrecedence: true) {
      milestone config.lockMilestone
      script { currentBuild.displayName = "${env.APP_VERSION}.${env.APP_BUILD}" }
      // checkout changelog: false, scm: [$class: 'GitSCM',
      //     branches: [[name: "refs/heads/master"]],
      //     doGenerateSubmoduleConfigurations: false,
      //     extensions: [[$class: 'WipeWorkspace']],
      //     submoduleCfg: [],
      //     userRemoteConfigs: [[
      //         url: 'git@github.com:Calavista/wifi-indigo.git',
      //         refspec: '+refs/heads/master:refs/remotes/origin/master',
      //     ]]
      // ]
      echo "Testing on ${config.envName}"
      sh '''
          echo "[INFO] Testing ${APP_VERSION}.${APP_BUILD} on ${config.envName} ..."
      '''
      writeFile file: "target/test-results/test/TEST-${config.testName}.xml", text: """
          <testsuite tests="3">
              <testcase classname="${config.testName}-1" name="ASuccessfulTest"/>
              <testcase classname="${config.testName}-2" name="AnotherSuccessfulTest"/>
              <testcase classname="${config.testName}-3" name="AFailingTest">
              <failure type="NotEnough${config.testName}"> details about failure </failure>
              </testcase>
          </testsuite>
      """
  }

}
