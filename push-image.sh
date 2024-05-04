#!/bin/bash

# Đặt tên image
IMAGE_NAME="backend-demo-service"

# Đặt tên cho Dockerhub repo (username/repo_name)
DOCKERHUB_REPO="luatnq/backend-demo-service"

# Lấy ID commit hiện tại từ git
COMMIT_ID=$(git rev-parse --short HEAD)

# Build Docker image từ Dockerfile
docker build -t $IMAGE_NAME .

# Tag Docker image với ID commit
docker tag $IMAGE_NAME $DOCKERHUB_REPO:$COMMIT_ID

# Push image lên Dockerhub
docker push $DOCKERHUB_REPO:$COMMIT_ID

echo "Image pushed to Dockerhub with tag: $COMMIT_ID"
