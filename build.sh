javac src/*.java -cp src -d build
echo "source compiled to build/ directory";
if [[ $# -gt 0 && $1 -eq "-jar" ]]; then
    pushd build > /dev/null
    jar -cmf ../Manifest.txt encryptor.jar *.class
    mv encryptor.jar ../
    popd  > /dev/null
    echo "encryptor.jar created";
else
    echo "run with -jar flag to generate jar";
fi
