#!/bin/bash 

for FOLDTOTEST in {1..10}
do

  cd GitRepos200projects100commits3Files/

  #load model for all but FOLDTOTEST
  declare -ia replacementCounter
  readarray replacementCounter < AllButTier"$FOLDTOTEST"Model.txt
  for i in `seq 1 484`; # 484 = 22*22 all possible combinations of replacements
  do
    echo replacementCounter[$i]
  done

  










  #for tierNumber in {1..10}
  #do
  #  if [ $tierNumber -ne $FOLDTOOMIT ]; then
  #    cd tier"$tierNumber"
  #    for i in `seq 1 484`; # 484 = 22*22 all possible combinations of replacements
  #      do
  #        commandToGetSpecificLine="sed '$i!d' tier"$tierNumber"Model.txt" # take number in line i
  #        numberOfReplacementsOfThisLine=$(eval $commandToGetSpecificLine)
  #        (( replacementCounter[$i]+=$numberOfReplacementsOfThisLine ))
  #        #if [ ${replacementCounter[$i]} -ne 0 ]; then
  #          #echo "Total counter for iterator $i: ${replacementCounter[$i]}"
  #        #fi
  #    done 
  #    cd .. # out of tier folder
  #  fi
  #done

  #print the replacementCounter array into a file here: Model.txt
  #printf "%s\n" "${replacementCounter[@]}" > AllButTier"$FOLDTOOMIT"Model.txt

  #touch summaryOfModel.txt
  #for i in `seq 1 484`; # 484 = 22*22 all possible combinations of replacements
  #do
  #  if [ ${replacementCounter[$i]} -ne 0 ]; then
  #    echo "Total counter for iterator $i: ${replacementCounter[$i]}" >> summaryOfAllButTier"$FOLDTOOMIT"Model.txt
  #  fi
  #done 










  cd .. # out of GitRepos
done
