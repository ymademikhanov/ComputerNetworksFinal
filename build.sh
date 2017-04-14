if [ $1 == "r" ]; then
	ls *.java
	javac *.java
	clear
	echo "############ Output ###########"
	java Test
elif [ $1 = "c" ]; then
	ls *.java
	clear
	javac *.java
elif [ $1 = "clean" ]; then
	rm *.class
	clear
	echo "###### Finished Clearing ######"
	ls -l
fi
