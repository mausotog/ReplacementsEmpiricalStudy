import urllib
import urllib2
import re
from xml.etree import ElementTree
import os

def get_page(url):
	print url
	req = urllib2.Request(url) 
	response = urllib2.urlopen(req)
	pageText = response.read()
	return pageText

def get_stack_overflow_pages():
	issue_order = 1
	for line in open("man_fix.txt"):
		line[:-1]
		if line.find("github.com") == -1:
			continue
		tokens = line.split("/")
		repo_user = tokens[3]
		repo_name = tokens[4]
		index = int(tokens[6])
		print "[%d]%s/%s:%d" %(issue_order, repo_user, repo_name, index)
		issue_order = issue_order + 1
		urls = []
		for line in open("../runtime-cache/projects/%s/%s/%d/stackoverflow/urls" %(repo_user, repo_name, index) ):
			urls.append(line[:-1])
		saved_path = "../runtime-cache/projects/%s/%s/%d/stackoverflow" % (repo_user, repo_name, index)
		for i in range(1, len(urls)+1):
			path = "%s/%d.html" %(saved_path, i)
			if os.path.exists(path):
				continue
			page = open (path, 'w')
			page.write(get_page(urls[i-1]))
			page.close()
	
get_stack_overflow_pages()
