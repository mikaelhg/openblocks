#
# Run script for development. Use the class directory instead of the final jar
#

DIR=`dirname $0`
DIR=`cd $DIR; pwd`

CP=${DIR}/../target/classes:${DIR}/../lib/jcommon-1.0.0-rc1.jar:${DIR}/../lib/jfreechart-1.0.0-rc1.jar:${DIR}/../lib/TableLayout-bin-jdk1.5-2009-08-26.jar

echo $*
java -cp ${CP} edu.mit.blocks.controller.WorkspaceController $*

