#
# @author Hansheng Zhang
# fetch issue description 
#
import json
root_dir = "../runtime-cache/issues"
for url in open("%s/success_urls" % root_dir):
	url = url[:-1]
	tokens = url.split("/")
	repo = "%s/%s" %(tokens[3], tokens[4])
	index = int(tokens[6])
	parent_path = "%s/%s" % (root_dir, repo)
	json_path = "%s/issue_%d.json" %(parent_path, index)
	body_path = "%s/body_%d" % (parent_path, index)
	data = json.load(open(json_path))
	if "body" in data:
		f = open(body_path, 'w')
		f.write(data["body"].encode('utf8'))
		f.close()
