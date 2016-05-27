#!/bin/bash 
#1st parameter: how many projects are there

#./RandomlySeparateFolders.sh 200

cd GitRepos/

# In case the distribution was wrong, this commented code will take out all the projects copied in the folders back to where they were originally
#for folder in {1..10}
#do
#  currentFolder="tier$folder"
#  cd $currentFolder
#  mv ./* ../
#  cd ..
#  rm -r $currentFolder
#done
#rm GithubProjects.txt




ls -d *BugFixingCommitVersions > GithubProjects.txt

projectsLeft=$1

for folder in {1..10}
do
  currentFolder="tier$folder"
  mkdir $currentFolder
  tenthOfProjectNumber=$(($1/10))
  for project in {1..$tenthOfProjectNumber}
  do

    lineNumber=$[ ( $RANDOM % $projectsLeft )  + 1 ]
    echo "Line selected at random from $projectsLeft possible projects: $lineNumber"
    projectName=$(sed -n "${lineNumber}p" < GithubProjects.txt)
    echo "For folder $folder picked project $projectName"
    mv $projectName $currentFolder
    echo "moved $projectName to $currentFolder"
    param="/$projectName/d"
    sed -i $param GithubProjects.txt
    let projectsLeft-=1
  done 
done










