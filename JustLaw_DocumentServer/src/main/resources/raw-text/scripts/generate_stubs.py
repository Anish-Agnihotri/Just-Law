# -*- coding: utf-8 -*-
import os
import re

filenames = os.listdir("..\\text")
filenames = [x for x in filenames if x[-4:] == ".txt"]

for filename in filenames:
    print(filename)
    with open("..\\summary\\" + filename, "w+") as x:
        x.write("")

print("done")
#print(filenames)
