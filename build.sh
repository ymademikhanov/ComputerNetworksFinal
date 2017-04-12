if [ $1 == "run" ]; then
	ls *.java
	javac *.java
	clear
	echo "############ Output ###########"
	java Test
elif [ $1 = "compile" ]; then
	ls *.java
	clear
	javac *.java
elif [ $1 = "clean" ]; then
	rm *.class
	clear
	echo "###### Finished Clearing ######"
	ls -l
fi
