# Skaffold + Jib Demo

This project demonstrates how to use Skaffold with Jib to build and deploy a Spring Boot application to Kubernetes.

## Prerequisites

- Java 21 or later
- Docker Desktop
- Kubernetes cluster (Docker Desktop's built-in Kubernetes is sufficient)
- Skaffold CLI
- Docker Hub account

## Project Structure

```
skaffold-jib-demo/
├── src/                    # Source code
├── k8s/                    # Kubernetes manifests
│   └── deployment.yaml     # Deployment and Service configuration
├── build.gradle           # Gradle build configuration with Jib plugin
├── skaffold.yaml          # Skaffold configuration
└── README.md              # This file
```

## Configuration Files

### build.gradle
```gradle
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.2.3'
    id 'io.spring.dependency-management' version '1.1.4'
    id 'com.google.cloud.tools.jib' version '3.4.0'
}

// ... other configurations ...

jib {
    from {
        image = 'eclipse-temurin:21-jre-alpine'
    }
    to {
        image = 'docker.io/yourusername/skaffold-jib-demo'
        tags = ['latest']
    }
    container {
        mainClass = 'com.vibol.skaffold_jib_demo.SkaffoldJibDemoApplication'
        ports = ['8080']
    }
}
```

### skaffold.yaml
```yaml
apiVersion: skaffold/v2beta29
kind: Config
build:
  artifacts:
    - image: docker.io/yourusername/skaffold-jib-demo
      jib: {}
  tagPolicy:
    sha256: {}
  local:
    push: true
deploy:
  kubectl:
    manifests:
      - k8s/deployment.yaml
```

### k8s/deployment.yaml
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: skaffold-jib-demo
spec:
  replicas: 1
  selector:
    matchLabels:
      app: skaffold-jib-demo
  template:
    metadata:
      labels:
        app: skaffold-jib-demo
    spec:
      containers:
      - name: skaffold-jib-demo
        image: docker.io/yourusername/skaffold-jib-demo:latest
        imagePullPolicy: IfNotPresent
        ports:
        - containerPort: 8080
---
apiVersion: v1
kind: Service
metadata:
  name: skaffold-jib-demo
spec:
  type: NodePort
  ports:
  - port: 8080
    targetPort: 8080
    nodePort: 30081
  selector:
    app: skaffold-jib-demo
```

## Getting Started

1. **Login to Docker Hub**
   ```bash
   docker login
   ```

2. **Update Image Names**
   - Replace `yourusername` in all configuration files with your Docker Hub username
   - Update the image name in `build.gradle`, `skaffold.yaml`, and `k8s/deployment.yaml`

3. **Run the Application**
   ```bash
   # For development with hot reload
   skaffold dev

   # For one-time deployment
   skaffold run
   ```

4. **Access the Application**
   - The application will be available at `http://localhost:30081`

## Useful Commands

### View Pods
```bash
kubectl get pods
```

### View Pod Logs
```bash
kubectl logs <pod-name>
# Follow logs
kubectl logs -f <pod-name>
```

### View Services
```bash
kubectl get services
```

### Clean Up
```bash
skaffold delete
```

## Benefits of Using Skaffold with Jib

1. **Faster Builds**: Jib builds container images without requiring a Docker daemon
2. **Layer Optimization**: Jib optimizes the container layers for faster builds and smaller images
3. **Development Workflow**: Skaffold provides a smooth development workflow with hot reloading
4. **No Dockerfile Required**: Jib handles container creation without needing a Dockerfile
5. **Reproducible Builds**: Both tools ensure reproducible builds across different environments

## Troubleshooting

1. **Image Pull Errors**
   - Ensure you're logged in to Docker Hub
   - Check if the image name is correct in all configuration files
   - Verify the image exists in Docker Hub

2. **Build Failures**
   - Check Java version compatibility
   - Verify Gradle configuration
   - Check network connectivity to Docker Hub

3. **Deployment Issues**
   - Check Kubernetes cluster status
   - Verify pod status and logs
   - Check service configuration

## Additional Resources

- [Skaffold Documentation](https://skaffold.dev/docs/)
- [Jib Documentation](https://github.com/GoogleContainerTools/jib)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Kubernetes Documentation](https://kubernetes.io/docs/home/) 