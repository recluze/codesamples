#!/bin/bash 

SRC_DIR=/media/Documents/Teaching/
DEST_DIR=/home/nam/Dropbox/Teaching/

SRCS[1]=FAST/CS\ 303\ -\ Software\ Engineering/lectures/
SRCS[2]=FAST/CS\ 303\ -\ Software\ Engineering/quizzes/
SRCS[3]=FAST/CS\ 303\ -\ Software\ Engineering/assignments/

SRCS[4]=FAST/CS\ 5XX\ -\ Secure\ Systems/lectures/
SRCS[5]=FAST/CS\ 5XX\ -\ Secure\ Systems/quizzes/
SRCS[6]=FAST/CS\ 5XX\ -\ Secure\ Systems/assignments/

SRCS[7]=FAST/CS\ 314\ -\ Computer\ Networks\ Lab/lectures/
SRCS[8]=FAST/CS\ 314\ -\ Computer\ Networks\ Lab/lab-tasks/
SRCS[9]=FAST/CS\ 314\ -\ Computer\ Networks\ Lab/quizzes/

SRCS[10]=FAST/admin-documents/

echo "----------------------------------------"
echo "Synching from ${SRC_DIR} to ${DEST_DIR}"
echo "----------------------------------------"
for index in $(seq 1 $((${#SRCS[*]}))) 
do 
	echo "Sync: ${SRCS[index]} ... "
	unison -auto -perms 0 "${SRC_DIR}${SRCS[index]}" "${DEST_DIR}${SRCS[index]}"
	echo "done synching ${SRCS[index]}."
	echo;echo;
done  
