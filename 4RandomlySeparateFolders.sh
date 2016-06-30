#!/bin/bash 
#1st parameter: how many projects are there

#./RandomlySeparateFolders.sh

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

projectsLeft=$(cat GithubProjects.txt | wc -l)

for folder in {1..10}
do
  currentFolder="tier$folder"
  mkdir $currentFolder
  tenthOfProjectNumber=$((($1+9)/10))
  for ((project=1; project<=$tenthOfProjectNumber; project++)); do
#FIXME: when the random matches the projectsLeft, and projectsLeft > 9, it behaves weird
    lineNumber=$[ ( $RANDOM % $projectsLeft )  + 1 ]
    echo "Line selected at random from $projectsLeft possible projects: $lineNumber"
    projectName=$(sed -n "${lineNumber}p" < GithubProjects.txt)
    echo "For folder $folder picked project $projectName"
    mv $projectName $currentFolder
    echo "moved $projectName to $currentFolder"
    param="/$projectName/d"
    sed -i $param GithubProjects.txt
    let projectsLeft-=1
    echo ""
  done 
done










