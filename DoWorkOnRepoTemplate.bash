#!/bin/bash
#DoWorkOnRepoTemplate is the same as the other one I sent you but it has the extra lines to read the log files commented out.

#This file is a template file for perforing some action over all commits in a folder of different GitHub Repositories.  This scripts assumes you have extracted all the commits in the repository to a commitFile

#git log > logResult.txt
#python ../../getCommits.py logResult.txt > commitList.txt

commitFile=commitList.txt
#lists all dirs in current directory
dirList=`ls -d -- */`
for dir in ${dirList[@]}
do
  cd $dir 
  echo "starting on $dir"
  firstCommit=""
  while read line
  do
    if [ -z "$firstCommit" ]
    then
      firstCommit=$line
    fi
    git reset --hard $line
    #do what you want to do with a specific commit here
  done < $commitFile
  git reset --hard $firstCommit
  cd ..
done
