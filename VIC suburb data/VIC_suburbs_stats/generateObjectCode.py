import csv

from bs4 import BeautifulSoup
import requests

import re

def generateObjectCode():

    with open('rankings.csv', newline='') as f:
        reader = csv.reader(f)
        rankings = list(reader)
    f.close()
    uni = [rankings[0][1] + ", " + rankings[0][2], rankings[0][0]]

    for row in rankings:
        if row[1] + ", " + row[2] != uni[0]:
            print("uni = new University(\"" + uni[0] +"\", closeSuburbs, new Location(" + uni[1] + "));")
            uni = [row[1] + ", " + row[2], row[0]]
            print("break;")
            print("case \"" + uni[0] + "\":")

        suburbUrlFormat = re.sub('[ ]', '+', row[3].lower())

        url = "https://www.google.com/search?q=" + suburbUrlFormat + "+victoria+coordinates&rlz=1C5CHFA_enAU890AU890&oq=plenty+coordinates&aqs=chrome..69i57j0i13i30j0i22i30j0i5i13i30l3j0i5i13i15i30j0i8i13i30l3.5343j0j7&sourceid=chrome&ie=UTF-8"

        # Get webpage
        result = requests.get(url)
        doc = BeautifulSoup(result.text, "html.parser")

        coordinateDiv = doc.find_all("div", {"class": "BNeawe iBp4i AP7Wnd"})
        if coordinateDiv:
            coordinateDiv = coordinateDiv[0]
            coordinates = re.sub('[° SE]', '', coordinateDiv.string)
        else:
            url = "https://www.google.com/search?q=" + suburbUrlFormat + "+coordinates&rlz=1C5CHFA_enAU890AU890&oq=plenty+coordinates&aqs=chrome..69i57j0i13i30j0i22i30j0i5i13i30l3j0i5i13i15i30j0i8i13i30l3.5343j0j7&sourceid=chrome&ie=UTF-8"
            result = requests.get(url)
            doc = BeautifulSoup(result.text, "html.parser")
            coordinateDiv = doc.find_all("div", {"class": "BNeawe iBp4i AP7Wnd"})
            if coordinateDiv:
                coordinateDiv = coordinateDiv[0]
                coordinates = re.sub('[° SE]', '', coordinateDiv.string)
            else:
                coordinates = ''


        print("closeSuburbs.add(new Suburb(\"" + row[3] + "\", new Location(" + coordinates + "), " + row[5] + ", " + row[6] + ", " + row[7] + "," + row[8] + "));")


generateObjectCode()