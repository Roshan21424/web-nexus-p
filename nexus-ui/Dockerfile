# Use Node to build the React app
FROM node:20-alpine AS build

WORKDIR /app

# Copy package.json and install dependencies
COPY package*.json ./
RUN npm install

# Copy all React source code and build it
COPY . .
RUN npm run build


# Use Nginx (a simple web server) to serve the built React files
FROM nginx:alpine

# Copy the built React files into Nginx's serving folder
COPY --from=build /app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Nginx runs on port 80
EXPOSE 80