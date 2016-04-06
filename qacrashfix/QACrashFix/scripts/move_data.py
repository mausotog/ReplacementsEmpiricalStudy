import os
import shutil

dest_dir = "../runtime-cache/projects"
source_dir = "../runtime-cache/issues"
for url in open("../runtime-cache/filtered_input_urls"):
	url = url[:-1]
	if url.find("github.com") == -1:
		continue
	tokens = url.split("/")
	repo_user = tokens[3]
	repo_name = tokens[4]
	index = int(tokens[6])
	
	copy_to_dir = "%s/%s/%s/%d" %(dest_dir, repo_user, repo_name, index)
	
	print copy_to_dir

	if not os.path.exists (copy_to_dir):
		os.makedirs(copy_to_dir)
	
	source_path = "%s/%s/%s/body_%d" %(source_dir, repo_user, repo_name, index)
	dest_path = "%s/exception.trace" % copy_to_dir
	
	shutil.copyfile(source_path, dest_path)
