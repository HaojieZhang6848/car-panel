mkdir -p build_output
docker run --platform linux/amd64 \
    --rm -w /car-panel -v $(pwd):/car-panel \
    -v $(pwd)/limo:/root/limo \
    -v "$HOME"/.m2:/root/.m2 \
    -e CAR_MODEL=limo ghcr.io/graalvm/graalvm-community:17-ol7 \
    bash -c "cd /car-panel && chmod +x ./mvnw && ./mvnw clean -P native native:compile && mv target/car-panel build_output/car-panel-amd64"
