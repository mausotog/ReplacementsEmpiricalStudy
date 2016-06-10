#!/bin/bash 

declare -ia successRates

for FOLDTOTEST in {1..10}
do

  cd GitRepos200projects100commits3Files/

    cd tier"$FOLDTOTEST"

      declare -ia testingData
      readarray testingData < tier"$FOLDTOTEST"Model.txt

      counterTesting=0
      didPredictIt=0
      didNotPredictIt=0
      for replacee in `seq 1 22`; # 484 = 22*22 all possible combinations of replacements
      do

        lineNumberOfMostLikely1=-1
        lineNumberOfMostLikely2=-1
        lineNumberOfMostLikely3=-1
        lineNumberOfMostLikely4=-1
        lineNumberOfMostLikely5=-1
	BetweenOneAndTwentyTwo=$[ ( $RANDOM % 22 ) ]
	RAND=$((($replacee - 1) * 22 + $BetweenOneAndTwentyTwo))
	while [[ $RAND -eq $lineNumberOfMostLikely1 || $RAND -eq $lineNumberOfMostLikely2 || $RAND -eq $lineNumberOfMostLikely3 || $RAND -eq $lineNumberOfMostLikely4 || $RAND -eq $lineNumberOfMostLikely5 ]]
	do
	  BetweenOneAndTwentyTwo=$[ ( $RANDOM % 22 ) ]
          RAND=$((($replacee - 1) * 22 + $BetweenOneAndTwentyTwo))
	done
	lineNumberOfMostLikely1=$RAND
	while [[ $RAND -eq $lineNumberOfMostLikely1 || $RAND -eq $lineNumberOfMostLikely2 || $RAND -eq $lineNumberOfMostLikely3 || $RAND -eq $lineNumberOfMostLikely4 || $RAND -eq $lineNumberOfMostLikely5 ]]
        do
          BetweenOneAndTwentyTwo=$[ ( $RANDOM % 22 ) ]
          RAND=$((($replacee - 1) * 22 + $BetweenOneAndTwentyTwo))
        done
        lineNumberOfMostLikely2=$RAND
        while [[ $RAND -eq $lineNumberOfMostLikely1 || $RAND -eq $lineNumberOfMostLikely2 || $RAND -eq $lineNumberOfMostLikely3 || $RAND -eq $lineNumberOfMostLikely4 || $RAND -eq $lineNumberOfMostLikely5 ]]
        do
          BetweenOneAndTwentyTwo=$[ ( $RANDOM % 22 ) ]
          RAND=$((($replacee - 1) * 22 + $BetweenOneAndTwentyTwo))
        done
        lineNumberOfMostLikely3=$RAND
	while [[ $RAND -eq $lineNumberOfMostLikely1 || $RAND -eq $lineNumberOfMostLikely2 || $RAND -eq $lineNumberOfMostLikely3 || $RAND -eq $lineNumberOfMostLikely4 || $RAND -eq $lineNumberOfMostLikely5 ]]
        do
          BetweenOneAndTwentyTwo=$[ ( $RANDOM % 22 ) ]
          RAND=$((($replacee - 1) * 22 + $BetweenOneAndTwentyTwo))
        done
        lineNumberOfMostLikely4=$RAND
	while [[ $RAND -eq $lineNumberOfMostLikely1 || $RAND -eq $lineNumberOfMostLikely2 || $RAND -eq $lineNumberOfMostLikely3 || $RAND -eq $lineNumberOfMostLikely4 || $RAND -eq $lineNumberOfMostLikely5 ]]
        do
          BetweenOneAndTwentyTwo=$[ ( $RANDOM % 22 ) ]
          RAND=$((($replacee - 1) * 22 + $BetweenOneAndTwentyTwo))
        done
        lineNumberOfMostLikely5=$RAND



        #echo "Most likelies in row $replacee are: lines: $(($lineNumberOfMostLikely1+1)), $(($lineNumberOfMostLikely2+1)), $(($lineNumberOfMostLikely3+1)), $(($lineNumberOfMostLikely4+1)), $(($lineNumberOfMostLikely5+1))"

        #see how many can the model correctly predict
        for testingReplacer in `seq 1 22`;
        do
          #to get the data with less guesses comment the remaining OR's from this condition
          if [ $counterTesting -eq $lineNumberOfMostLikely1 ] || [ $counterTesting -eq $lineNumberOfMostLikely2 ] || [ $counterTesting -eq $lineNumberOfMostLikely3 ] || [ $counterTesting -eq $lineNumberOfMostLikely4 ] || [ $counterTesting -eq $lineNumberOfMostLikely5 ]
          then
            didPredictIt=$(($didPredictIt+${testingData[$counterTesting]}))
          else
            didNotPredictIt=$(($didNotPredictIt+${testingData[$counterTesting]})) 
          fi
          counterTesting=$(($counterTesting+1))
        done
        #echo "Testing Fold: $FOLDTOTEST, For line: $replacee, we have $didPredictIt predicted, and $didNotPredictIt NOT predicted." 

      done
      totalInstances=$(($didPredictIt + $didNotPredictIt))
      successRate=$(($didPredictIt*100/$totalInstances))
      successRates[$FOLDTOTEST]=$successRate
      echo "tier$FOLDTOTEST as testing data: $didPredictIt instances predicted, and $didNotPredictIt instances NOT predicted. Success Rate: $successRate%"
    cd .. #out of tier$FOLDTOTEST
  cd .. # out of GitRepos
done

mean=$(echo ${successRates[@]} | awk '{for(i=1;i<=NF;i++){sum+=$i};print sum/NF}')
echo "Mean: $mean"
variance=$(echo ${successRates[@]} | awk -vM=5 '{for(i=1;i<=NF;i++){sum+=($i-'"$mean"')*($i-'"$mean"')};print sum/NF}')
echo "Variance: $variance"
stdDev=$(echo ${successRates[@]} | awk -vM=5 '{for(i=1;i<=NF;i++){sum+=($i-'"$mean"')*($i-'"$mean"')};print sqrt(sum/NF)}')
echo "Standard deviation: $stdDev"
