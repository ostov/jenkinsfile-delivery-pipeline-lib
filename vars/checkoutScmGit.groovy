def call(String gitURL = "")
{checkout scm: [$class: 'GitSCM',
    branches: [[name: "refs/heads/master"]],
    doGenerateSubmoduleConfigurations: false,
    extensions: [[$class: 'WipeWorkspace']],
    submoduleCfg: [],
    userRemoteConfigs: [[
        url: gitURL,
        refspec: '+refs/heads/master:refs/remotes/origin/master',
    ]]
]}
