

docker start mysql_db

# Run MySQL container
docker run -d \
  --name mysql_db \
  --network my_app_network \
  -e MYSQL_ROOT_PASSWORD=rootpassword \
  -e MYSQL_DATABASE=mydatabase \
  -e MYSQL_USER=user \
  -e MYSQL_PASSWORD=password \
  mysql:latest

# Build Java application container
docker build -t margetis_ps_app .

# Run Java application container
docker run -it \
  --network my_app_network \
  -e DISPLAY=$DISPLAY \
  -v /tmp/.X11-unix:/tmp/.X11-unix \
  margetis_ps_app
