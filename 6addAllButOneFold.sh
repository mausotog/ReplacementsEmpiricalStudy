#!/bin/bash 

#./addAllButOneFold.sh


for FOLDTOOMIT in {1..10}
do

  declare -ia replacementCounter
  for i in `seq 1 484`; # 484 = 22*22 all possible combinations of replacements
  do
    replacementCounter[$i]=0
  done
  replacementCount=0
  appendCount=0
  deleteCount=0

  cd GitRepos/

  for tierNumber in {1..10}
  do
    if [ $tierNumber -ne $FOLDTOOMIT ]; then
      cd tier"$tierNumber"
      for i in `seq 1 487`; # 484 = 22*22 all possible combinations of replacements
        do
          commandToGetSpecificLine="sed '$i!d' tier"$tierNumber"Model.txt" # take number in line i
          
          numberOfReplacementsOfThisLine=$(eval $commandToGetSpecificLine)
	  if [ $i -le 484 ]; then
            (( replacementCounter[$i]+=$numberOfReplacementsOfThisLine ))
	  fi
	  if [ $i -eq 485 ]; then
            (( replacementCount+=$numberOfReplacementsOfThisLine ))
	  fi
	  if [ $i -eq 486 ]; then
            (( appendCount+=$numberOfReplacementsOfThisLine ))
	  fi
	  if [ $i -eq 487 ]; then
            (( deleteCount+=$numberOfReplacementsOfThisLine ))
	  fi
          #if [ ${replacementCounter[$i]} -ne 0 ]; then
            #echo "Total counter for iterator $i: ${replacementCounter[$i]}"
          #fi
      done 
      cd .. # out of tier folder
    fi
  done

  #print the replacementCounter array into a file here: Model.txt
  printf "%s\n" "${replacementCounter[@]}" > AllButTier"$FOLDTOOMIT"Model.txt
  echo $replacementCount >> AllButTier"$FOLDTOOMIT"Model.txt
  echo $appendCount >> AllButTier"$FOLDTOOMIT"Model.txt
  echo $deleteCount >> AllButTier"$FOLDTOOMIT"Model.txt

  #touch summaryOfModel.txt
  for i in `seq 1 484`; # 484 = 22*22 all possible combinations of replacements
  do
    if [ ${replacementCounter[$i]} -ne 0 ]; then
      echo "Total counter for iterator $i: ${replacementCounter[$i]}" >> summaryOfAllButTier"$FOLDTOOMIT"Model.txt
    fi
  done 
if [ $replacementCount -ne 0 ]; then
    echo "Total counter for replacements: $replacementCount" >> summaryOfAllButTier"$FOLDTOOMIT"Model.txt
fi
if [ $appendCount -ne 0 ]; then
    echo "Total counter for append: $appendCount" >> summaryOfAllButTier"$FOLDTOOMIT"Model.txt
fi
if [ $deleteCount -ne 0 ]; then
    echo "Total counter for delete: $deleteCount" >> summaryOfAllButTier"$FOLDTOOMIT"Model.txt
fi


  cd .. # out of GitRepos
done
