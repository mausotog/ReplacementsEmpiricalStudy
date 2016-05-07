#!/bin/bash

#This program reads a list of git respositories and clones each respository in the #list in the current directory

# Example usage: ./DoWorkOnDownloadedRepos.sh


QACRASHFIXDIRECTORY=$(pwd)

cd GitRepos/
repoFile=../sampleRepos.txt
while read projectFolder
do

  folderNameTmp=$(echo $projectFolder | sed 's#.*/##g') #take the folder name from the url with extension
  folderName=$(echo "${folderNameTmp::-4}") # remove extension
  echo "Working on project $folderName"
  cd $folderName
  git log --all -- '*.java' > logResult.txt # look only for commits that look into java files
  python ../../readLogData.py logResult.txt commitList.txt
  echo "commitList.txt created"





  #reads the commit hashes and filters the ones with more than 3 files, and stores it in a file caaled commitList3FilesModifiedOrLess
  commitList=../commitList.txt
  while read commitHashs
  do
      
     >> commitList3FilesModifiedOrLess.txt
  done < $commitList








  #It takes the last n commits
  head -100 commitList3FilesModifiedOrLess.txt > commitListSmall.txt 
  
  rm -rf BugFixingCommitVersions
  mkdir BugFixingCommitVersions
  cd BugFixingCommitVersions
  commitNumber=0

  #saves the before and after versions for each of the modified files
  commitListSmall=../commitListSmall.txt
  while read commitHashs
  do
      beforeHash=$(echo $commitHashs| cut -d' ' -f 1)
      afterHash=$(echo $commitHashs| cut -d' ' -f 2)
      #beforeHash=${commitHashs:0:40}
      commitNumber=$[$commitNumber+1]
      commitFolderName="Commit"$commitNumber
      echo "Working on $commitFolderName"
      echo ""
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

        fileNames=./filesModifiedInThisCommit.txt
        while read fileName
        do
	  nameOfFile=${fileName##*/} #name of the file with the .java extension. Example: example.java
          currentDirectory=$(pwd)
          nameOfFileWithoutExtension="${nameOfFile%.*}"
          echo "Calling QACrashFix for project $folderName in commit $commitNumber with file $nameOfFile"
	  timeout -k 1m 5m /usr/lib/jvm/java-7-oracle/bin/java -Dfile.encoding=UTF-8 -classpath $QACRASHFIXDIRECTORY/qacrashfix/QACrashFix/test/Test/bin:$QACRASHFIXDIRECTORY/qacrashfix/QACrashFix/target/exception-fix-0.0.1-SNAPSHOT.jar:$QACRASHFIXDIRECTORY/qacrashfix/QACrashFix/test/Test/lib/log4j-api-2.5.jar:$QACRASHFIXDIRECTORY/qacrashfix/QACrashFix/test/Test/lib/log4j-core-2.5.jar:$QACRASHFIXDIRECTORY/qacrashfix/QACrashFix/test/Test/lib/org.eclipse.core.contenttype_3.5.0.v20150421-2214.jar:$QACRASHFIXDIRECTORY/qacrashfix/QACrashFix/test/Test/lib/org.eclipse.core.jobs_3.7.0.v20150330-2103.jar:$QACRASHFIXDIRECTORY/qacrashfix/QACrashFix/test/Test/lib/org.eclipse.core.resources_3.10.1.v20150725-1910.jar:$QACRASHFIXDIRECTORY/qacrashfix/QACrashFix/test/Test/lib/org.eclipse.core.runtime_3.11.1.v20150903-1804.jar:$QACRASHFIXDIRECTORY/qacrashfix/QACrashFix/test/Test/lib/org.eclipse.equinox.common_3.7.0.v20150402-1709.jar:$QACRASHFIXDIRECTORY/qacrashfix/QACrashFix/test/Test/lib/org.eclipse.equinox.preferences_3.5.300.v20150408-1437.jar:$QACRASHFIXDIRECTORY/qacrashfix/QACrashFix/test/Test/lib/org.eclipse.jdt.core_3.11.1.v20150902-1521.jar:$QACRASHFIXDIRECTORY/qacrashfix/QACrashFix/test/Test/lib/org.eclipse.osgi_3.10.102.v20160118-1700.jar Test $currentDirectory/before/$nameOfFile $currentDirectory/after/$nameOfFile > "$currentDirectory"$nameOfFileWithoutExtension.txt

        done < $fileNames
      fi
    cd .. #get out from $commitFolderName folder
  done < $commitListSmall

  cd .. #bugFixingCommitVersions
  mv BugFixingCommitVersions ../"$folderName"BugFixingCommitVersions
  cd .. #folderName
  #rm -r $folderName #remove the cloned project

  #rm -f projectFolders.txt
  echo ""
#done < $projectFolders

  #cd "$folderName"BugFixingCommitVersions

done < $repoFile 
















