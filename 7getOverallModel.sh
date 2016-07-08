#!/bin/bash 

#./getOverallModel.sh

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
    echo "Done with tier $tierNumber"
done




#print the replacementCounter array into a file here: Model.txt
#printf "%s\n" "${replacementCounter[@]}" > OVERALLModel.txt
rm OVERALLModel.txt
index=0
for i in `seq 1 22`; # 484 = 22*22 all possible combinations of replacements
do
  for e in `seq 1 22`;
  do
    (( index+=1 ))
    line+="${replacementCounter[$index]} "
   
  done
  echo $line >> OVERALLModel.txt
  line=""
done
  echo $replacementCount >> OVERALLModel.txt
  echo $appendCount >> OVERALLModel.txt
  echo $deleteCount >> OVERALLModel.txt

#touch summaryOfModel.txt
rm summaryOfOVERALLModel.txt
for i in `seq 1 484`; # 484 = 22*22 all possible combinations of replacements
do
  if [ ${replacementCounter[$i]} -ne 0 ]; then
    echo "Total counter for iterator $i: ${replacementCounter[$i]}" >> summaryOfOVERALLModel.txt
  fi
done 
if [ $replacementCount -ne 0 ]; then
    echo "Total counter for replacements: $replacementCount" >> summaryOfOVERALLModel.txt
fi
if [ $appendCount -ne 0 ]; then
    echo "Total counter for append: $appendCount" >> summaryOfOVERALLModel.txt
fi
if [ $deleteCount -ne 0 ]; then
    echo "Total counter for delete: $deleteCount" >> summaryOfOVERALLModel.txt
fi

cd .. # out of GitRepos

