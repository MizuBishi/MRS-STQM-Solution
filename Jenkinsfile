pipeline {
  agent any
  tools {
    maven 'Maven 3.6.0'
    jdk 'JDK10'
  }
  stages {
    stage('Checkout') {
      steps {
        checkout scm
        sh 'mvn clean'
      }
    }
    stage('Build') {
      steps {
        sh 'mvn package'
      }
    }
    stage('Integration Testing') {
      steps {
        sh 'mvn -f ./pom-integrationTesting.xml  integration-test'
      }
    }
    stage('Deploy') {
      steps {
        sh 'mkdir -p /var/jenkins_home/download'
        sh 'cp ./target/*.jar /var/jenkins_home/download' 
      }
    }
  }
}
