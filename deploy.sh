#!/bin/bash

# Build the frontend
npm run build

# Copy the built files to the Spring Boot static directory
cp -r dist/* ../src/main/resources/static/

echo "Frontend build and deployment completed" 