#!/bin/bash

##
## Update the *.s-model and *.ref file contents
##
#
#for source_file in ./tests/tema3/*.cl; do
#	echo "Running with `basename $source_file`"
#	java -jar Tema3.jar $source_file > ./tests/tema3/`basename $source_file .cl`.s-model
#	if [ "$source_file" = "./tests/tema3/32-big.cl" ]; then 
#		echo 5 | spim -exception_file trap.handler.nogc -file tests/tema3/`basename $source_file .cl`.s-model > tests/tema3/`basename $source_file .cl`.ref
#	else
#		spim -exception_file trap.handler.nogc -file tests/tema3/`basename $source_file .cl`.s-model > tests/tema3/`basename $source_file .cl`.ref
#	fi
#done

CLASSPATH=.:"/mnt/c/Users/claw2/Desktop/UPB/Anul IV/CPL/Setup/antlr-4.7.2-complete.jar:/mnt/c/Users/claw2/Desktop/UPB/Anul IV/CPL/Teme/Tema2/bin"

passed=0
for source_file in ./tests/tema3/*.cl; do
	echo -e "\n`basename $source_file`"
	java -cp .:"/mnt/c/Users/claw2/Desktop/UPB/Anul IV/CPL/Setup/antlr-4.7.2-complete.jar":"/mnt/c/Users/claw2/Desktop/UPB/Anul IV/CPL/Teme/Tema2/bin" cool.compiler.Compiler $source_file > ./tests/tema3/`basename $source_file .cl`.s
	if [ "$source_file" = "./tests/tema3/32-big.cl" ]; then
		echo 5 | spim -exception_file trap.handler.nogc -file tests/tema3/`basename $source_file .cl`.s > tests/tema3/`basename $source_file .cl`.out
	else
		spim -exception_file trap.handler.nogc -file tests/tema3/`basename $source_file .cl`.s > tests/tema3/`basename $source_file .cl`.out
	fi

	diff tests/tema3/`basename $source_file .cl`.ref tests/tema3/`basename $source_file .cl`.out

	if [ $? = 0 ]; then
		echo -e "Test passed!\n"
		passed=$(($passed + 1))
	else	
		echo -e "Test failed!\n"
	fi
done

echo -e "\nTotal: $(( $(($passed * 100)) / 32))"
