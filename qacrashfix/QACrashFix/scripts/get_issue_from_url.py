#
# @author Hansheng Zhang
#
# collect urls of issues from '*.xls' and fetch the issue formatted 'json' 
#
import getpass
import urllib2
import base64
import json
import os
import xlrd

username = "landhero" # github account
password = getpass.getpass("Enter your github password:") # password
header_authorization = base64.encodestring("%s:%s" %(username, password))[:-1] # authorization header to be added in request

def validate():
	print "validating...",
	header_authorization = base64.encodestring("%s:%s" %(username,password))[:-1]
	request = urllib2.Request("https://api.github.com/user");
	request.add_header("Authorization", "Basic %s" % header_authorization)
	try:
		result = urllib2.urlopen(request)
		if u"login" in json.loads(result.read()):
			print "[Success]"
		else :
			print "[Error]"
			exit(1)
	except:
		print "[Error]"
		exit(1)

validate()

root_dir = "../runtime-cache/issues"
failed_file = open("%s/failed_urls" % root_dir, 'w')
success_file = open("%s/success_urls" % root_dir, 'w')
all_issue_file = open("%s/issue_urls" % root_dir, 'w')
def process_url (url):
	tokens = url.split('/')
	repo = "%s/%s" %(tokens[3], tokens[4])
	index = int(tokens[6])
	parent_path = "%s/%s" % (root_dir, repo)
	if not os.path.exists(parent_path):
		os.makedirs(parent_path)
	path = "%s/issue_%d.json" %(parent_path, index)
	all_issue_file.write("https://github.com/%s/issues/%d\n" %(repo, index))
	print "%s:%d" % (repo, index),
	if os.path.exists(path):
		print "[Cached]"
		success_file.write("https://github.com/%s/issues/%d\n" %(repo, index))
	else:
		try:
			request = urllib2.Request("https://api.github.com/repos/"+repo+"/issues/"+str(index))
			request.add_header("Authorization", "Basic %s" % header_authorization)
			result = urllib2.urlopen(request)
			file_issue = open(path, "w")
			file_issue.write(result.read())
			file_issue.close()
			print "[Fetched]"
			success_file.write("https://github.com/%s/issues/%d\n" %(repo, index))
		except:
			failed_file.write("https://github.com/%s/issues/%d\n" %(repo, index))
			print "[Failed]"

sheet = xlrd.open_workbook("../experiment/android_issues.xls").sheet_by_index(0)
for row_index in range(sheet.nrows):
	url = sheet.cell_value(row_index, 0)
	if url.find('github.com') != -1:
		process_url(url)

failed_file.close()
success_file.close()
all_issue_file.close()
