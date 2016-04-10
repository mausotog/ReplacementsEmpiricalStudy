#!/bin/bash

#It receives one parameter: the number of projects it should download from the link below
#param 1 can only be a multiple of 10. Ex:10,20,100,120, etc.

#This program reads a list of git respositories and clones each respository in the #list in the current directory

# Example usage: ./DownLoadRepoList.bash 10


NUMBEROFPROJECTSTODOWNLOAD="$1"

#The link is hardcoded, and it is the link of the java projects in github sorted by the number of stars
pythonArgs="https://github.com/search?utf8=%E2%9C%93&q=language%3AJava+stars%3A%3E1000&type=Repositories&ref=searchresults $NUMBEROFPROJECTSTODOWNLOAD"
#python ExtractGitHubRepos.py $pythonArgs > sampleRepos.txt

#repoFile=../sampleRepos.txt
#rm -rf GitRepos
#mkdir GitRepos
#cd GitRepos/
#while read line
#do
#	echo "git clone $line"
#	CLONECOMMAND="git clone $line"
#	eval $CLONECOMMAND
#	echo ""  	
	#sleep 1
#done < $repoFile

#REMOVE THIS LINE
cd GitRepos/

#create a file with all the project folders
rm -f folders.txt
ls > foldersTmp.txt
grep -vwE "foldersTmp.txt" foldersTmp.txt > foldersTmp2.txt
rm foldersTmp.txt
grep -vwE "projectFolders.txt" foldersTmp2.txt > foldersTmp3.txt
rm foldersTmp2.txt
grep -vwE "logResult.txt" foldersTmp3.txt >projectFolders.txt
rm foldersTmp3.txt

#creates commitList.txt with pairs of the hashs of the bug fixing commits
projectFolders=projectFolders.txt
while read projectFolder
do
  echo "Working on project $projectFolder"
  cd $projectFolder
  git log > logResult.txt
  python ../../readLogData.py logResult.txt commitList.txt

  rm -rf BugFixingCommitVersions
  mkdir BugFixingCommitVersions
  cd BugFixingCommitVersions
  commitNumber=0

  #saves the before and after versions for each of the modified files
  commitList=../commitList.txt
  while read commitHashs
    do
      beforeHash=$(echo $commitHashs| cut -d' ' -f 1)
      afterHash=$(echo $commitHashs| cut -d' ' -f 2)
      #beforeHash=${commitHashs:0:40}
      commitNumber=$[$commitNumber+1]
      commitFolderName="Commit"$commitNumber
      mkdir $commitFolderName
      cd $commitFolderName

      #git filters http://stackoverflow.com/questions/6879501/filter-git-diff-by-type-of-change
      allFilesModified=$(git diff --diff-filter=M --name-only $commitHashs) 

      stringFilesModifiedInThisCommit=""
      for currentFile in $allFilesModified; do
	#echo "Checking file: $currentFile"
	if [[ "$currentFile" == *.java ]]
	then
	  #echo $currentFile
	  stringFilesModifiedInThisCommit="$stringFilesModifiedInThisCommit $currentFile"
        fi
      done

      IFS=' ' read -r -a arrayOfModifiedFiles <<< "$stringFilesModifiedInThisCommit"
      for oneJavaFile in "${arrayOfModifiedFiles[@]}"
      do
        echo $oneJavaFile >> filesModifiedInThisCommit.txt
      done
   
      if [ -n "$stringFilesModifiedInThisCommit" ]; then
        mkdir before
        cd before
          fileNames=../filesModifiedInThisCommit.txt
         while read fileName
          do
	    nameOfFile=${fileName##*/}
	    git show $beforeHash:$fileName > $nameOfFile
          done < $fileNames
        cd .. #get out from before folder

        mkdir after
        cd after
          fileNames=../filesModifiedInThisCommit.txt
          while read fileName
          do
	    nameOfFile=${fileName##*/}
            git show $afterHash:$fileName > $nameOfFile
          done < $fileNames
        cd .. #get out from after folder
      fi
    cd .. #get out from $commitFolderName folder
  done < $commitList

  cd .. #bugFixingCommitVersions
  cd .. #projectFolder
  rm -f projectFolders.txt
  echo ""
done < $projectFolders















