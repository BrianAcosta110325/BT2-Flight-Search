FROM node:22.14 AS fe-builder
WORKDIR /app
COPY package*.json .
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=fe-builder /app/build /usr/share/nginx/html