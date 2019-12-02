tnxId=$(awk '/file: \[{0}\]/{print $4}' log-file-path/log-file.log);
echo $tnxId;
awk -v pattern=$tnxId 'BEGIN {$4==$pattern} {print $0}' log-file-path/log-file.log > {1};