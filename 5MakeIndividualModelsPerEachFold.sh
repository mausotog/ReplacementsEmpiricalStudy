#!/bin/bash 

#./MakeIndividualModelsPerEachFold.sh

for i in {1..10}
do
  ./CountTotalNumberOfReplacements.sh tier$i

done
