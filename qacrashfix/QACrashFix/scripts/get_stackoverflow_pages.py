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
	size = 3
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

issue_order = 1

def get_stack_overflow_pages():
	for line in open("../runtime-cache/filtered_input_urls"):
		line[:-1]
		if line.find("github.com") == -1:
			continue
		tokens = line.split("/")
		repo_user = tokens[3]
		repo_name = tokens[4]
		index = int(tokens[6])
		print "[%d]%s/%s:%d" %(issue_order, repo_user, repo_name, index)
		issue_order = issue_order + 1
		keyword_path = "../runtime-cache/projects/%s/%s/%d/.keywords"% (repo_user, repo_name, index)
		keywords = open(keyword_path, 'r').readline()
		print "keywords: "+keywords
		saved_path = "../runtime-cache/projects/%s/%s/%d/stackoverflow" % (repo_user, repo_name, index)
		#	if not os.path.exists(saved_path):
		#		os.makedirs(saved_path)
		if os.path.exists(saved_path + "/3.html"):
			print "[Cached] 3 pages"
			continue
		try:
			pages = search_with_keywords(keywords)
			if os.path.exists("%s/%d.html" %(saved_path, len(pages))) :
				print "[Cached] %d " % len(pages)
				continue
			log = open(saved_path+"/urls", 'w')
			for i in range(len(pages)):
				log.write(pages[i][0]+"\n")
				open ("%s/%d.html" %(saved_path, i+1), 'w').write(pages[i][1])
			log.close()
			print "[Fetched] %d pages" % len(pages)
		except:
			print "[Failed]"
