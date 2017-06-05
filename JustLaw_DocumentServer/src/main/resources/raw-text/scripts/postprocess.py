# -*- coding: utf-8 -*-
import os
import re

filenames = os.listdir("..\\in")
filenames = [x for x in filenames if x[-4:] == ".txt"]

for filename in filenames:
    print(filename)
    with open("..\\in\\" + filename, "r") as f:
        content = f.readlines()
        content = "".join(content)
        
        content = content.replace("\f", "")
        content = content.replace("", "")

        content = re.sub("(?<=[a-z\-,;])\n(?=[A-Za-z0-9])", "", content, flags=re.MULTILINE)
        content = re.sub("(?<=(Dr\.|Ms\.|Mr\.))\n(?=[A-Za-z0-9])", "", content, flags=re.MULTILINE)
        content = re.sub("(?<=Mrs\.)\n(?=[A-Za-z0-9])", "", content, flags=re.MULTILINE)
        content = re.sub("\n(?=\[[0-9]+\])", "\n\n", content, flags=re.MULTILINE)
        content = re.sub(r"(?<=\n)\(\n", "", content, flags=re.MULTILINE)
        content = re.sub(r"(?<=\n)\)\n", "", content, flags=re.MULTILINE)
        content = re.sub(r"[Pp]age: +[0-9]+", "", content, flags=re.MULTILINE)
        content = re.sub("\n\n+", "\n\n", content, flags=re.MULTILINE)

        citation = re.search("(?<=CITATION: ).*(?=\n)", content, flags=re.MULTILINE)
        if citation != None:
            citation = citation.group(0)
            citation = re.sub("[,\\\/\.%$\*]", "", citation)

            with open("..\\out\\" + citation + ".txt", "w+") as x:
                x.write(content)

print("done")
#print(filenames)
