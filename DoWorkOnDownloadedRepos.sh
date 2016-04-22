#!/bin/bash

#It receives one parameter: the number of projects it should download from the link below
#param 1 can only be a multiple of 10. Ex:10,20,100,120, etc.

#This program reads a list of git respositories and clones each respository in the #list in the current directory

# Example usage: ./DoWorkOnDownloadedRepos.sh /home/mau/Research/replacements/ReplacementsEmpiricalStudy/qacrashfix/QACrashFix/


QACRASHFIXDIRECTORY="$1"

#The link is hardcoded, and it is the link of the java projects in github sorted by the number of stars
#pythonArgs="https://github.com/search?utf8=%E2%9C%93&q=language%3AJava+stars%3A%3E1000&type=Repositories&ref=searchresults $NUMBEROFPROJECTSTODOWNLOAD"

#echo "Calling ExtractGitHubRepos"
#python ExtractGitHubRepos.py $pythonArgs > sampleRepos.txt
#echo "Finishing ExtractGitHubRepos"


#rm -rf GitRepos
#echo "Creating GitRepos"
#mkdir GitRepos
cd GitRepos/
repoFile=../sampleRepos.txt
while read projectFolder
do
  #echo "git clone $projectFolder"
  #CLONECOMMAND="git clone $projectFolder"
  #eval $CLONECOMMAND
  #echo ""  	
  #sleep 1


#REMOVE THIS LINE
#cd GitRepos/

#create a file with all the project folders
#rm -f folders.txt
#ls > foldersTmp.txt
#grep -vwE "foldersTmp.txt" foldersTmp.txt > foldersTmp2.txt
#rm foldersTmp.txt
#grep -vwE "projectFolders.txt" foldersTmp2.txt > foldersTmp3.txt
#rm foldersTmp2.txt
#grep -vwE "logResult.txt" foldersTmp3.txt > projectFolders.txt
#rm foldersTmp3.txt

#creates commitList.txt with pairs of the hashs of the bug fixing commits
#projectFolders=projectFolders.txt
#while read projectFolder
#do

  folderNameTmp=$(echo $projectFolder | sed 's#.*/##g')
  folderName=$(echo "${folderNameTmp::-4}") #take the folder name from the url
  echo "Working on project $folderName"
  cd $folderName
  git log > logResult.txt
  python ../../readLogData.py logResult.txt commitList.txt
  echo "commitList.txt created"

  #THIS IS A TEMPORAL SOLUTION TO REDUCE THE TIME IT TAKES TO RUN THE SCRIPT. IT TAKES ONLY THE LAST 10 COMMITS. REMOVE IT
  head -10 commitList.txt > commitListSmall.txt 
  #REMOVE WHATS ABOVE. ALSO CHANGE BELOW commitListSmall.txt FOR commitList.txt

  rm -rf BugFixingCommitVersions
  mkdir BugFixingCommitVersions
  cd BugFixingCommitVersions
  commitNumber=0

  #saves the before and after versions for each of the modified files
  commitList=../commitListSmall.txt
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
	  /usr/lib/jvm/java-7-oracle/bin/java -Dfile.encoding=UTF-8 -classpath $QACRASHFIXDIRECTORY/test/Test/bin:$QACRASHFIXDIRECTORY/target/exception-fix-0.0.1-SNAPSHOT.jar:$QACRASHFIXDIRECTORY/test/Test/lib/log4j-api-2.5.jar:$QACRASHFIXDIRECTORY/test/Test/lib/log4j-core-2.5.jar:$QACRASHFIXDIRECTORY/test/Test/lib/org.eclipse.core.contenttype_3.5.0.v20150421-2214.jar:$QACRASHFIXDIRECTORY/test/Test/lib/org.eclipse.core.jobs_3.7.0.v20150330-2103.jar:$QACRASHFIXDIRECTORY/test/Test/lib/org.eclipse.core.resources_3.10.1.v20150725-1910.jar:$QACRASHFIXDIRECTORY/test/Test/lib/org.eclipse.core.runtime_3.11.1.v20150903-1804.jar:$QACRASHFIXDIRECTORY/test/Test/lib/org.eclipse.equinox.common_3.7.0.v20150402-1709.jar:$QACRASHFIXDIRECTORY/test/Test/lib/org.eclipse.equinox.preferences_3.5.300.v20150408-1437.jar:$QACRASHFIXDIRECTORY/test/Test/lib/org.eclipse.jdt.core_3.11.1.v20150902-1521.jar:$QACRASHFIXDIRECTORY/test/Test/lib/org.eclipse.osgi_3.10.102.v20160118-1700.jar Test $currentDirectory/before/$nameOfFile $currentDirectory/after/$nameOfFile > "$currentDirectory"$nameOfFileWithoutExtension.txt

        done < $fileNames
      fi
    cd .. #get out from $commitFolderName folder
  done < $commitList

  cd .. #bugFixingCommitVersions
  mv BugFixingCommitVersions ../"$folderName"BugFixingCommitVersions
  cd .. #folderName
  #rm -r $folderName #remove the cloned project

  #rm -f projectFolders.txt
  echo ""
#done < $projectFolders

  #cd "$folderName"BugFixingCommitVersions

done < $repoFile 
















