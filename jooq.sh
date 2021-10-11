RESULT=$(mvn help:evaluate -Dexpression=settings.localRepository -q -DforceStdout)
echo RESULT