#!/bin/bash 

for FOLDTOTEST in {1..10}
do

  cd GitRepos200projects100commits3Files/

    #load model for all but FOLDTOTEST
    declare -ia model
    readarray model < AllButTier"$FOLDTOTEST"Model.txt

    cd tier"$FOLDTOTEST"

      declare -ia testingData
      readarray testingData < tier"$FOLDTOTEST"Model.txt

      counterModel=1
      counterTesting=1
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
        lineNumberOfMostLikely3=$(($counterModel+2)
        lineNumberOfMostLikely4=$(($counterModel+3))
        lineNumberOfMostLikely5=$(($counterModel+4))

        for modelReplacer in `seq 1 22;
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

        #see how many can the model correctly predict
        for testingReplacer in `seq 1 22;
        do
          #to get the data with less guesses comment the remaining OR's from this condition
          if [ $counterTesting -eq $lineNumberOfMostLikely1 ] || [ $counterTesting -eq $lineNumberOfMostLikely2 ] || [ $counterTesting -eq $lineNumberOfMostLikely3 ] || [ $counterTesting -eq $lineNumberOfMostLikely4 ] || [ $counterTesting -eq $lineNumberOfMostLikely5 ]
          then
            didPredictIt=$(($didPredictIt+${testingData[$counterTesting]}))   
	    echo "predicted so far: $didPredictIt"
          else
            didNotPredictIt=$(($didNotPredictIt+${testingData[$counterTesting]})) 
	    echo "NOT predicted so far: $didNotPredictIt"
          fi
          counterTesting=$(($counterTesting+1))
        done

      done

    cd .. #out of tier$FOLDTOTEST
  cd .. # out of GitRepos
done

