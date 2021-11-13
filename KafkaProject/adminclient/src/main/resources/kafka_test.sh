#!/bin/sh

echo "Starting minikube.....waiting"
minikube start --memory=4096
echo "Collecting minikube status.....waiting"
minikube status

echo "Creating kafka namespace..."
kubectl create ns kafka
sleep 20s

echo "Installing Strimzi Custom Resource Definitions..."
kubectl create -f 'https://strimzi.io/install/latest?namespace=kafka' -n kafka
sleep 1m

echo "Applying base cluster configuration..."
kubectl apply -f https://strimzi.io/examples/latest/kafka/kafka-persistent-single.yaml -n kafka 
echo "Waiting..."
kubectl wait kafka/my-cluster --for=condition=Ready --timeout=300s -n kafka

echo "Applying custom MTLS cluster configuration..."
cd strimzi/custom_cr
kubectl apply -f new_cluster.yaml -n kafka
echo "Waiting..."
kubectl wait kafka/my-cluster --for=condition=Ready --timeout=300s -n kafka

echo "Fetching cluster connection information..."
kubectl get kafka my-cluster -n kafka -o jsonpath='{.status.listeners[?(@.type=="external")].bootstrapServers}'
echo "Starting minikube dashboard..."
minikube dashboard
