cd ../../code || exit

./gradlew runQuarkus -DbenchTimeout=1 -DXms=1024m -DXmx=16G -Djdk.tracePinnedThreads > ../benches/ab/quarkus.log &
PID_GRADLE=$!

cd ../benches/ab || exit

sleep 1
while ! grep -m1 'Starting Quarkus Application' < quarkus.log; do
    sleep 1
done

PID_QUARKUS=$(grep -oP 'on PID \K[0-9]+' quarkus.log)

echo ":::::::::::::::::::::::::::::::     Gradle running PID = $PID_GRADLE"
echo ":::::::::::::::::::::::::::::::     Quarkus running PID = $PID_QUARKUS"

#
# Define routes for benchmark
#
ROUTES=(
  presentations/rocker
  presentations/jstachio
  presentations/pebble
  presentations/freemarker
  presentations/trimou
#  presentations/velocity
  presentations/thymeleaf
  presentations/htmlFlow
  presentations/kotlinx
  stocks/rocker
  stocks/jstachio
  stocks/pebble
  stocks/freemarker
  stocks/trimou
#  stocks/velocity
  stocks/thymeleaf
  stocks/htmlFlow
)

#
# Warm up all paths in 3 iterations each.
#
echo "##########################################"
echo "############# WARM UP ####################"
echo "##########################################"
for path in "${ROUTES[@]}"; do
  ab -n 1000 -c 32 http://localhost:8080/$path
done

#
# Run Bench
#
echo "##########################################"
echo "############# RUN BENCH ##################"
echo "##########################################"
./run-ab.sh "${ROUTES[@]}" | tee quarkus-results.log


# Gracefully terminate the Spring Boot application when running on local machine.
# It will send a SIGTERM corresponding to Exit code 143.
if [ "$GH" != "true" ]; then
  kill $PID_GRADLE
  kill $PID_QUARKUS

  # Wait for the process to exit
  wait $PID_GRADLE
fi

echo ":::::::::::::::::::::::::::::::     Sync Bench Done"

cd ../../code || exit

./gradlew runQuarkusVirtual -DbenchTimeout=1 -DXms=1024m -DXmx=16G > ../benches/ab/quarkus.log &
PID_GRADLE=$!

cd ../benches/ab || exit

sleep 1
while ! grep -m1 'Starting Quarkus Application' < quarkus.log; do
    sleep 1
done

PID_QUARKUS=$(grep -oP 'on PID \K[0-9]+' quarkus.log)

echo ":::::::::::::::::::::::::::::::     Gradle running PID = $PID_GRADLE"
echo ":::::::::::::::::::::::::::::::     Quarkus running PID = $PID_QUARKUS"

#
# Warm up all paths in 3 iterations each.
#
echo "##########################################"
echo "############# WARM UP ####################"
echo "##########################################"
for path in "${ROUTES[@]}"; do
  ab -n 1000 -c 32 http://localhost:8080/$path
done

#
# Run Bench
#
echo "##########################################"
echo "############# RUN BENCH ##################"
echo "##########################################"
./run-ab.sh "${ROUTES[@]}" | tee quarkus-results-virtual.log

if [ "$GH" != "true" ]; then
  kill $PID_GRADLE
  kill $PID_QUARKUS

  # Wait for the process to exit
  wait $PID_GRADLE
fi

echo ":::::::::::::::::::::::::::::::     Virtual Bench Done"