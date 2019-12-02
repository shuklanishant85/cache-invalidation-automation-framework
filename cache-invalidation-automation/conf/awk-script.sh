tnxId=$(awk '/file: \[{0}\]/{print $4}' //tomcat/logs/livesite.runtime.log);
echo $tnxId;
awk -v pattern=$tnxId 'BEGIN {$4==$pattern} {print $0}' //tomcat/logs/livesite.runtime.log > {1};