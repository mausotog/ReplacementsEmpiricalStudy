#This file extracts the commmit names from a git log output. 
#this file is not perfect - sometimes it will pull out things that are 
#not the commit hash - mainly a quick solution that works well overall

import sys
import re

def main(argv=None):
  if len(sys.argv) != 2:
    print "Usage: file_to_read_from"
    print "len: ",len(sys.argv)
  else:
    fin = open(sys.argv[1],'r')
    for line in fin:
      commitSearch = re.search('commit (.*)',line)
      if commitSearch:
        commitName = commitSearch.group(1)
        print(commitName)


if __name__ == "__main__":
  main()    
