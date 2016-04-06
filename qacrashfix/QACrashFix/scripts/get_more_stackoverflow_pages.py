import urllib
import urllib2
import re
from xml.etree import ElementTree
import os
def search_with_keywords(keyword):
	query = {
		'q': '%s site:stackoverflow.com' % keyword
	}
	
	url = "https://www.google.com.hk/search?newwindow=1&safe=strict&%s" % urllib.urlencode(query)

	req = urllib2.Request(url, headers={'User-Agent' : "Magic Browser"}) 
	response = urllib2.urlopen(req)
	text = response.read()
	m = re.findall(r"href=\"/url\?q=http://stackoverflow.com/questions/[^\"&]+", text, flags=re.UNICODE)
	size = 10
	if len(m) < size:
		size = len(m)
	pages  = []
	for i in range(size):
		url = m[i][13:]
		print url
		req = urllib2.Request(url) 
		response = urllib2.urlopen(req)
		pageText = response.read()
		pages.append((url, pageText))
	return pages

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
		previous_urls = []
		for line in open("../runtime-cache/projects/%s/%s/%d/stackoverflow/urls" %(repo_user, repo_name, index) ):
			previous_urls.append(line[:-1])
		keyword_path = "../runtime-cache/projects/%s/%s/%d/.keywords"% (repo_user, repo_name, index)
		keywords = open(keyword_path, 'r').readline()
		print "keywords: "+keywords
		saved_path = "../runtime-cache/hstag_projects/%s/%s/%d/stackoverflow" % (repo_user, repo_name, index)
		if os.path.exists(saved_path):
			
		pages = search_with_keywords(keywords)
		for i in range(len(pages)):
			url = pages[i][0]
			if url in previous_urls:
				continue
			previous_urls.append(url)
			open ("%s/%d.html" %(saved_path, previous_urls.length), 'w').write(pages[i][1])
			if previous_urls.length >= 10:
				break
		log = open (save_path+"/urls", 'w')
		for url in previous_urls:
			log.write(url+"\n")
		break
			
get_stack_overflow_pages()