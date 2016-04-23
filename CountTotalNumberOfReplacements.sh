#!/bin/bash 

containerFolderName="$1"

declare -ia replacementCounter
for i in `seq 1 484`; # 484 = 22*22 all possible combinations of replacements
do
  replacementCounter[$i]=0
done

cd $containerFolderName

ls -d *BugFixingCommitVersions > GithubProjects.txt

projectNames=./GithubProjects.txt
while read projectName 
do

  echo "current directory:"
  pwd
  cd $projectName
#  cd BugFixingCommitVersions

  ls -d Commit[1234567890]*.txt | grep -v / > CommitFiles.txt

  if [ -e "CommitFiles.txt" ] 
    then
    commitFilesNames=./CommitFiles.txt
    while read commitFileName 
    do
      if [ -s $commitFileName ]
      then
        echo "Looking at file $commitFileName"
        for i in `seq 1 484`; # 484 = 22*22 all possible combinations of replacements
        do
          commandToGetSpecificLine="sed '$i!d' $commitFileName | cut -f1 -d':'"
          numberOfReplacementsOfThisLine=$(eval $commandToGetSpecificLine)
          (( replacementCounter[$i]+=$numberOfReplacementsOfThisLine ))
          if [ ${replacementCounter[$i]} -ne 0 ]; then
            echo "Total counter for iterator $i: ${replacementCounter[$i]}"
          fi
        done 
      fi
    done < $commitFilesNames
  fi
#cd .. # get out of BugFixingCommitVersions
cd .. # get out of $projectName
done < $projectNames

#print the replacementCounter array into a file here: Model.txt
for i in `seq 1 484`; # 484 = 22*22 all possible combinations of replacements
do
  #echo ${replacementCounter[$i] } > Model.txt
done

