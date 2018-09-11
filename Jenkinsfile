pipeline {
    agent {
        docker {
            image 'maven:3-jdk-8'
            args '--volumes-from $MAVEN_CACHE --network $DOCKER_NETWORK'
        }
    }
    environment {
        NEXUS_PASSWORD = credentials('NEXUS_PASSWORD')
        NEXUS_AUTH = credentials('NEXUS_AUTH')
        SONAR_PASSWORD = credentials('SONAR_PASSWORD')
        GITHUB_TOKEN = credentials('GITHUB_TOKEN')
    }
    options {
        timeout(time: 1, unit: 'HOURS')
    }
    stages {
        stage('env') {
            steps {
                sh 'date'
                sh 'printenv'
            }
        }
        stage('build') {
            steps {
                sh 'mvn -s $MAVEN_SETTINGS -B clean verify'
            }
        }
        stage('install') {
            when {
                anyOf {
                    branch 'develop'
                    branch 'master'
                }
            }
            steps {
                sh 'mvn -s $MAVEN_SETTINGS -B install'
            }
        }
        stage('quality') {

            steps {
                sh 'mvn -s $MAVEN_SETTINGS -B sonar:sonar $SONAR_PR=$GITHUB_TOKEN'
                script {
                    currentBuild.result = 'SUCCESS'
                }
                step([$class: 'CompareCoverageAction', scmVars: [GIT_URL: env.GIT_URL]])
            }
        }
        stage('sonar') {
            steps {
                sh 'mvn -s $MAVEN_SETTINGS -B sonar:sonar'
            }
        }
    }
    post {
        always {
            junit '**/target/surefire-reports/*.xml'
        }
        unstable {
            notifyUnstable()
        }
        failure {
            notifyFailed()
        }
    }
}

def notifyBuild(String buildStatus = 'STARTED', String colorCode = '#5492f7', String notify = '') {

  def project = 'baleen'
  def channel = "${project}_ci"
  def base = "https://github.com/commitd/${project}/commit/"

  def commit = sh(returnStdout: true, script: 'git log -n 1 --format="%H"').trim()
  def link = "${base}${commit}"
  def shortCommit = commit.take(6)
  def title = sh(returnStdout: true, script: 'git log -n 1 --format="%s"').trim()
  def subject = "<${link}|${shortCommit}> ${title}"

  def summary = "${buildStatus}: Job <${env.RUN_DISPLAY_URL}|${env.JOB_NAME} [${env.BUILD_NUMBER}]>\n${subject} ${notify}"

  slackSend (channel: "#${channel}", color: colorCode, message: summary)

}

def author() {
  return sh(returnStdout: true, script: 'git log -n 1 --format="%an" | awk \'{print tolower($1);}\'').trim()
}

def notifyStarted() {
  notifyBuild()
}

def notifySuccess() {
  notifyBuild('SUCCESS', 'good')
}

def notifyUnstable() {
  notifyBuild('UNSTABLE', 'warning', "\nAuthor: @${author()} <${RUN_CHANGES_DISPLAY_URL}|Changelog>")
}

def notifyFailed() {
  notifyBuild('FAILED', 'danger', "\nAuthor: @${author()} <${RUN_CHANGES_DISPLAY_URL}|Changelog>")
}