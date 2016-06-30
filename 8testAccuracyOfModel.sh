#!/bin/bash 

declare -ia successRates

for FOLDTOTEST in {1..10}
do

  cd GitRepos200projects100commits3Files/

    #load model for all but FOLDTOTEST
    declare -ia model
    readarray model < AllButTier"$FOLDTOTEST"Model.txt

    cd tier"$FOLDTOTEST"

      declare -ia testingData
      readarray testingData < tier"$FOLDTOTEST"Model.txt

      counterModel=0
      counterTesting=0
      didPredictIt=0
      didNotPredictIt=0
      for replacee in `seq 1 22`; # 484 = 22*22 all possible combinations of replacements
      do
        mostLikely1=0
        mostLikely2=0
        mostLikely3=0
        mostLikely4=0
        mostLikely5=0
        lineNumberOfMostLikely1=$counterModel
        lineNumberOfMostLikely2=$(($counterModel+1))
        lineNumberOfMostLikely3=$(($counterModel+2))
        lineNumberOfMostLikely4=$(($counterModel+3))
        lineNumberOfMostLikely5=$(($counterModel+4))

        for modelReplacer in `seq 1 22`;
        do
          if [ ${model[$counterModel]} -ge $mostLikely1 ]
          then
            mostLikely5=$mostLikely4
            lineNumberOfMostLikely5=$lineNumberOfMostLikely4
            mostLikely4=$mostLikely3
            lineNumberOfMostLikely4=$lineNumberOfMostLikely3
            mostLikely3=$mostLikely2
            lineNumberOfMostLikely3=$lineNumberOfMostLikely2
            mostLikely2=$mostLikely1
            lineNumberOfMostLikely2=$lineNumberOfMostLikely1
            mostLikely1=${model[$counterModel]}
            lineNumberOfMostLikely1=$counterModel
          elif [ ${model[$counterModel]} -ge $mostLikely2 ]
          then
            mostLikely5=$mostLikely4
            lineNumberOfMostLikely5=$lineNumberOfMostLikely4
            mostLikely4=$mostLikely3
            lineNumberOfMostLikely4=$lineNumberOfMostLikely3
            mostLikely3=$mostLikely2
            lineNumberOfMostLikely3=$lineNumberOfMostLikely2
            mostLikely2=${model[$counterModel]}
            lineNumberOfMostLikely2=$counterModel
          elif [ ${model[$counterModel]} -ge $mostLikely3 ]
          then
            mostLikely5=$mostLikely4
            lineNumberOfMostLikely5=$lineNumberOfMostLikely4
            mostLikely4=$mostLikely3
            lineNumberOfMostLikely4=$lineNumberOfMostLikely3
            mostLikely3=${model[$counterModel]}
            lineNumberOfMostLikely3=$counterModel
          elif [ ${model[$counterModel]} -ge $mostLikely4 ]
          then
            mostLikely5=$mostLikely4
            lineNumberOfMostLikely5=$lineNumberOfMostLikely4
            mostLikely4=${model[$counterModel]}
            lineNumberOfMostLikely4=$counterModel
          elif [ ${model[$counterModel]} -ge $mostLikely5 ]
          then
            mostLikely5=${model[$counterModel]}
            lineNumberOfMostLikely5=$counterModel
          fi
          counterModel=$(($counterModel+1))
        done
        #echo "Most likelies in row $replacee are: $mostLikely1 in line $(($lineNumberOfMostLikely1+1)), $mostLikely2 in line $(($lineNumberOfMostLikely2+1)), $mostLikely3 in line $(($lineNumberOfMostLikely3+1)), $mostLikely4 in line $(($lineNumberOfMostLikely4+1)), $mostLikely5 in line $(($lineNumberOfMostLikely5+1))"

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
