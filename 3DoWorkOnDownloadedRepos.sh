#!/bin/bash

# Example usage: ./2DoWorkOnDownloadedRepos.sh

SCRIPTSDIRECTORY=$(pwd)
JAVALOCATION=$(which java)
JAVACLOCATION=$(which javac)

cd GitRepos/
repoFile=../sampleRepos.txt
while read projectFolder
do

  folderNameTmp=$(echo $projectFolder | sed 's#.*/##g') #take the folder name from the url with extension
  folderName=$(echo "${folderNameTmp::-4}") # remove extension
  #folderName=$(echo "${folderNameTmp::${#folderNameTmp}-4}") # remove extension
  echo "Working on project $folderName"
  if [ -d "$folderName" ]; then
    cd $folderName
    git log -100 --all -- '*.java' > logResult.txt # look only for commits that look into java files # add -100 to look just for the last 100 commits
    python ../../readLogData.py logResult.txt commitList.txt
    echo "commitList.txt created"
  
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
        commitNumber=$[$commitNumber+1]

        #git filters http://stackoverflow.com/questions/6879501/filter-git-diff-by-type-of-change
        allFilesModified=$(git diff --diff-filter=M --name-only $commitHashs) 

        count=0
        for currentFile in $allFilesModified
        do
    	  #echo "Checking file: $currentFile"
          if [[ "$currentFile" == *.java ]]
          then
            count=$(($count+1))
            #echo $currentFile
          fi
        done

      if [ $count -le 3 ] && [ $count -ge 1 ]; #If the number of files modified is between 1 and 3
      #if [ $count -ge 1 ];
      then
        #echo "before and after, right before introducing it to commitListAfter3FileFilter.txt: $commitHashs"
        echo "$commitHashs" >> ../commitListAfter3FileFilter.txt
        #echo "Line $commitNumber from commitList.txt has between 1-3 java files being modified"
      fi    

      #cd .. #get out from $commitFolderName folder
    done < $commitList

    #It takes the last n commits
    head -100 ../commitListAfter3FileFilter.txt > ../commitListSmall.txt 
    rm ../commitListAfter3FileFilter.txt

    commitNumber=0
    commitListSmall=../commitListSmall.txt
    while read commitHashs
    do
        beforeHash=$(echo $commitHashs| cut -d' ' -f 1)
        afterHash=$(echo $commitHashs| cut -d' ' -f 2)
        #beforeHash=${commitHashs:0:40}
        commitNumber=$[$commitNumber+1]
        commitFolderName="Commit"$commitNumber
        echo ""
        echo "Working on $commitFolderName of commitListSmall.txt"
        mkdir $commitFolderName
        cd $commitFolderName
        rm -f filesModifiedInThisCommit.txt

        #git filters http://stackoverflow.com/questions/6879501/filter-git-diff-by-type-of-change
        allFilesModified=$(git diff --diff-filter=M --name-only $commitHashs) 

        stringFilesModifiedInThisCommit=""
        for currentFile in $allFilesModified
        do
	  #echo "Checking file: $currentFile"
          if [[ "$currentFile" == *.java ]]
          then
          #  echo "Current file: "$currentFile
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
	    
	    rm -f "$currentDirectory""$nameOfFileWithoutExtension"GenProgMutationsCounts.txt
	    #Getting each particular replacement
	        timeout -k 1m 5m $JAVALOCATION -Dfile.encoding=UTF-8 -classpath $SCRIPTSDIRECTORY/qacrashfix/QACrashFix/test/Test/bin:$SCRIPTSDIRECTORY/qacrashfix/QACrashFix/target/exception-fix-0.0.1-SNAPSHOT.jar:$SCRIPTSDIRECTORY/qacrashfix/QACrashFix/test/Test/lib/log4j-api-2.5.jar:$SCRIPTSDIRECTORY/qacrashfix/QACrashFix/test/Test/lib/log4j-core-2.5.jar:$SCRIPTSDIRECTORY/qacrashfix/QACrashFix/test/Test/lib/org.eclipse.core.contenttype_3.5.0.v20150421-2214.jar:$SCRIPTSDIRECTORY/qacrashfix/QACrashFix/test/Test/lib/org.eclipse.core.jobs_3.7.0.v20150330-2103.jar:$SCRIPTSDIRECTORY/qacrashfix/QACrashFix/test/Test/lib/org.eclipse.core.resources_3.10.1.v20150725-1910.jar:$SCRIPTSDIRECTORY/qacrashfix/QACrashFix/test/Test/lib/org.eclipse.core.runtime_3.11.1.v20150903-1804.jar:$SCRIPTSDIRECTORY/qacrashfix/QACrashFix/test/Test/lib/org.eclipse.equinox.common_3.7.0.v20150402-1709.jar:$SCRIPTSDIRECTORY/qacrashfix/QACrashFix/test/Test/lib/org.eclipse.equinox.preferences_3.5.300.v20150408-1437.jar:$SCRIPTSDIRECTORY/qacrashfix/QACrashFix/test/Test/lib/org.eclipse.jdt.core_3.11.1.v20150902-1521.jar:$SCRIPTSDIRECTORY/qacrashfix/QACrashFix/test/Test/lib/org.eclipse.osgi_3.10.102.v20160118-1700.jar CountNumberOfGenProgMutations $currentDirectory/before/$nameOfFile $currentDirectory/after/$nameOfFile  > "$currentDirectory""$nameOfFileWithoutExtension"GenProgMutationsCounts.txt



	    rm -f "$currentDirectory""$nameOfFileWithoutExtension"ParMutationsCounts.txt
	    echo "Calling Gumtree for project $folderName in commit $commitNumber with file $nameOfFile"
	    timeout -k 1m 5m $JAVALOCATION -cp .:/$SCRIPTSDIRECTORY/gumtreeBin/:/$SCRIPTSDIRECTORY/gumtreeBin/lib/dist-2.1.0-SNAPSHOT.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/core-2.1.0-SNAPSHOT.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/client-2.1.0-SNAPSHOT.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/client.diff-2.1.0-SNAPSHOT.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/gen.antlr-2.1.0-SNAPSHOT.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/gen.antlr-antlr-2.1.0-SNAPSHOT.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/gen.antlr-css-2.1.0-SNAPSHOT.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/gen.antlr-json-2.1.0-SNAPSHOT.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/gen.antlr-php-2.1.0-SNAPSHOT.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/gen.antlr-r-2.1.0-SNAPSHOT.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/gen.antlr-xml-2.1.0-SNAPSHOT.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/gen.c-2.1.0-SNAPSHOT.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/gen.jdt-2.1.0-SNAPSHOT.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/gen.js-2.1.0-SNAPSHOT.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/gen.ruby-2.1.0-SNAPSHOT.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/simmetrics-core-3.2.3.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/trove4j-3.0.3.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/gson-2.4.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/reflections-0.9.10.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/nanohttpd-webserver-2.1.1.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/rendersnake-1.9.0.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/antlr-3.5.2.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/org.eclipse.jdt.core-3.11.0.v20150602-1242.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/runtime-3.10.0-v20140318-2214.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/org.eclipse.core.resources-3.10.0.v20150423-0755.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/rhino-1.7.7.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/jrubyparser-0.5.3.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/guava-18.0.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/commons-codec-1.10.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/javassist-3.18.2-GA.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/annotations-2.0.1.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/nanohttpd-2.1.1.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/junit-4.8.2.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/servlet-api-2.4.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/commons-lang3-3.1.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/commons-io-2.0.1.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/spring-webmvc-4.1.6.RELEASE.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/jtidy-r938.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/guice-3.0.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/javax.inject-1.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/antlr-runtime-3.5.2.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/ST4-4.0.8.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/osgi-3.10.0-v20140606-1445.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/common-3.6.200-v20130402-1505.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/jobs-3.6.0-v20140424-0053.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/registry-3.5.400-v20140428-1507.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/preferences-3.5.200-v20140224-1527.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/contenttype-3.4.200-v20140207-1251.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/app-1.3.200-v20130910-1609.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/spring-beans-4.1.6.RELEASE.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/spring-context-4.1.6.RELEASE.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/spring-core-4.1.6.RELEASE.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/spring-expression-4.1.6.RELEASE.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/spring-web-4.1.6.RELEASE.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/aopalliance-1.0.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/cglib-2.2.1-v20090111.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/spring-aop-4.1.6.RELEASE.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/commons-logging-1.2.jar:/$SCRIPTSDIRECTORY/gumtreeBin/lib/asm-3.1.jar CountInstancesOfTemplate1 $currentDirectory/before/$nameOfFile $currentDirectory/after/$nameOfFile > "$currentDirectory""$nameOfFileWithoutExtension"ParMutationsCounts.txt
	

          done < $fileNames
        fi
      cd .. #get out from $commitFolderName folder
    done < $commitListSmall

    cd .. #bugFixingCommitVersions
    rm -r ../"$folderName"BugFixingCommitVersions
    mv BugFixingCommitVersions ../"$folderName"BugFixingCommitVersions
    cd .. #folderName
    #rm -r $folderName #remove the cloned project
  fi
  #rm -f projectFolders.txt
  echo ""
#done < $projectFolders

  #cd "$folderName"BugFixingCommitVersions

done < $repoFile 
















