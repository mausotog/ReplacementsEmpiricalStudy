#!/bin/bash 

#./addAllButOneFold.sh


for FOLDTOOMIT in {1..10}
do


declare -ia replacementCounter
for i in `seq 1 484`; # 484 = 22*22 all possible combinations of replacements
do
  replacementCounter[$i]=0
done

cd GitRepos/

for tierNumber in {1..10}
do
  if [ $tierNumber -ne $FOLDTOOMIT ]; then
    cd tier"$tierNumber"
    for i in `seq 1 484`; # 484 = 22*22 all possible combinations of replacements
      do
          commandToGetSpecificLine="sed '$i!d' tier"$tierNumber"Model.txt" # take number in line i
        numberOfReplacementsOfThisLine=$(eval $commandToGetSpecificLine)
        (( replacementCounter[$i]+=$numberOfReplacementsOfThisLine ))
        #if [ ${replacementCounter[$i]} -ne 0 ]; then
          #echo "Total counter for iterator $i: ${replacementCounter[$i]}"
        #fi
    done 
    cd .. # out of tier folder
  fi
done




#print the replacementCounter array into a file here: Model.txt
printf "%s\n" "${replacementCounter[@]}" > AllButTier"$FOLDTOOMIT"Model.txt

#touch summaryOfModel.txt
for i in `seq 1 484`; # 484 = 22*22 all possible combinations of replacements
do
  if [ ${replacementCounter[$i]} -ne 0 ]; then
    echo "Total counter for iterator $i: ${replacementCounter[$i]}" >> summaryOfAllButTier"$FOLDTOOMIT"Model.txt
  fi
done 

cd .. # out of GitRepos
done
