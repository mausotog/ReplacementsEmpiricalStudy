#!/bin/sh
scalac -cp ".:allLibs/*" -d bin/ src/gumdiff/handlejdt/*.scala src/gumdiff/jdtgum/*.java src/gumdiff/difftemplates/*.scala src/gumdiff/customLib/*.scala src/gumdiff/handlecommits/*.scala
cd bin
scala -classpath .:../allLibs/*:../allLibs/*/*:gumdiff/handlejdt/*:gumdiff/customLib/*:gumdiff/handlecommits/*:gumdiff/difftemplates/* gumdiff.difftemplates.DiffTemplates /home/mau/Research/replacements/ReplacementsEmpiricalStudy/GitRepos/react-nativeBugFixingCommitVersions/Commit1/before/ReactTextView.java /home/mau/Research/replacements/ReplacementsEmpiricalStudy/GitRepos/react-nativeBugFixingCommitVersions/Commit1/after/ReactTextView.java

