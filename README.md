# Calculator Project Documentation

## Project Overview
Calculator project receives input as an arithmetic expression from the user, performs calculation on it and returns the result back to the user. It is built using Microservice architecture and deployed on the Google Cloud Platform using CI/CD pipeline. 

## Components Involved
 1) **Ingress** - All the incoming traffic is routed by Ingress controller to the Gateway service.
 2) **Gateway Microservice** - This service acts as a Gateway and routes incoming requests to different backend services involved in the architecture. 
3) **Service registry Microservice** - This service acts as look up where we can get information about all the other microservices involved.
 4) **Calculation Microservice** - This is our core service which performs the calculation and returns the response to the user.

## Calculation Service Details

| Sample Request/Response   | Request: </br> http://ip/calculus?query=KDIqMy40KQ==</br> Response:</br> {     "error": false,     "result": 6.8 } </br></br> |
| Error scenarios | Request: </br> http://<hostname:port>/calculus </br> Response: </br> {    "error": true,     "message": "query parameter is missing" }  </br></br> Request: </br>http://<hostname:port>/calculus?query= </br> Response: </br>  {     "error": true,     "message": "Query Input is empty. Please pass the input for calculation" } </br></br> Request: </br> http://<hostname:port>/calculus?query=YWJjZGU= </br> Response: </br> {     "error": true,     "message": "Wrong query expression" } </br></br> Request: </br> http://<hostname:port>/calculus?query=$ </br> Response: </br> {     "error": true,     "message": "Invalid query input. Not a base64 encoded String" } |

## Installation

1) IDE - Developer's choice
2) JDK with Java 11
3) Maven


## Repository
The source code of this project is in the Github. You can access the repository with the below link.
[Calculator Project Repository Link](https://github.com/karthicb92/calculator-project "Calculator Project")

## CI/CD Pipeline
CI/CD Pipeline is built using Github Actions. This pipeline has two steps:
 * Build
 * Deploy

## GKE Deployment Steps
**Build**

1) Go to Git Hub page. Click on Actions Tab. Create the workflow that best suit the project needs. I have taken Java with Maven template.
2) We have to then configure workflow.yml file
    * Create events that we need to trigger the build. I have created configuration to trigger the build for every push commit on develop branch
     * Also, I have added option to trigger manual build when ever needed by developers
3) All the built images are pushed to Docker Hub image registry. For authenticating with the Docker Hub, we have to store our Docker Hub credentials under Secrets.

#### Usage

```
jobs:
  build:

    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3
    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'temurin'
        server-id: docker.io
        server-username: MAVEN_USERNAME
        server-password: MAVEN_PASSWORD
    - name: Build with Maven an push hello-world service
      run: mvn package dockerfile:push --file pom.xml
      env:
          MAVEN_USERNAME: ${{ secrets.DOCKER_USERNAME }}
          MAVEN_PASSWORD: ${{ secrets.DOCKER_PASSWORD }}
```

**Deploy**
1) We have to configure GKE Kubectl action for deploying our image to Google Kubernetes Engine Cluster.
2) Make sure GKE cluster is created in GCP and its service account has necessary permissions to it. Below are the secrets needed.
   * APPLICATION_CREDENTIALS - Base64 encoded service account json file
   * PROJECT_ID - GCP Project ID.
   * GKE_CLUSTER - GKE Cluster name
   * GKE_ZONE - The Zone name in which the cluster is created

```
 Deploy:
    needs: build
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@master
    - name: kubectl - Google Cloud GKE cluster.
      uses: ameydev/gke-kubectl-action@master
      env:
        PROJECT_ID: ${{ secrets.PROJECT_ID }}
        APPLICATION_CREDENTIALS: ${{ secrets.GKE_SA_KEY }}
        CLUSTER_NAME: ${{ secrets.GKE_CLUSTER }}
        ZONE_NAME: ${{ secrets.GKE_ZONE }}
      with:
        args: apply -f k8s/
```

## Steps to verify after deployment
1) Login to Google Console
   * https://console.cloud.google.com/
2) Get credentials from created cluster to interact with it:
   * gcloud container clusters get-credentials cluster-demo-1 --zone 
     us-central1-c --<project-id>
3) Once we are authenticated, then we can use kubectl command which interacts with Kubernetes Master Controller
4) To check the pod status execute below command
   * kubectl get pods -n <namespace>
5) If Pod status is up and running, then we have successfully deployed our application in the cloud!!!