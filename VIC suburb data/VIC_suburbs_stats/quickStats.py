# This file creates a csv file which contains population
# and rent statistics for each SAL in victoria.
# The data is taken from the ABS using a downloaded
# CSV file and web scraped using the beautiful soup
# library and requests.

import csv
from bs4 import BeautifulSoup
import requests


def getQuickStats():
    header_row = ['sal_code', 'sal_name', 'population', 'median_weekly_rent']

    # Create the csv file which will be written to
    f = open('suburb_quick_stats.csv', 'w')
    writer = csv.writer(f)
    writer.writerow(header_row)

    # Get census data from csv. This contains population and median weekly rent
    # for each SAL code in Victoria.
    with open('2021_census_data.csv', newline='') as census:
        reader = csv.reader(census)
        census_data = list(reader)
    census.close()

    # WEB SCRAPING
    for i in census_data:
        # This is the URL containing QuickStats for a given SAL,
        # so the SAL must be appended to the URL
        url = "https://www.abs.gov.au/census/find-census-data/quickstats/2021/" + i[0]

        # Get webpage
        result = requests.get(url)
        doc = BeautifulSoup(result.text, "html.parser")

        # Create row that will be written to csv file
        row = [None, None, i[1], i[2]]    # [SAL code, SAL name, people, median weekly rent]

        # Get suburb name
        header = doc.find_all('h1')[0]  # suburb name is the first h1 header in the webpage
        sal_name = header.string
        if '(' in sal_name:     # removing brackets if there
            sal_name = sal_name[0:sal_name.find('(') - 1]  # remove bracketed info if there
        print(sal_name)   # to see progress
        row[0] = i[0]  # SAL code
        row[1] = sal_name

        writer.writerow(row)

    f.close()


getQuickStats()
