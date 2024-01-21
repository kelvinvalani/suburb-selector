# This file will combine the previously created suburb_quick_stats.csv with
# the crime statistics information taken from https://www.crimestatistics.vic.gov.au/

import csv


def crimeStats():

    # Get crime stats data from csv
    with open('2021_crime_stats.csv', newline='') as f:
        reader = csv.reader(f)
        crime_data = list(reader)
    f.close()

    # Get previous quick stats data from csv
    with open('suburb_quick_stats.csv', newline='') as f:
        reader = csv.reader(f)
        quick_stats = list(reader)
    f.close()

    # Create dictionary that will contain suburb crime data
    # with the suburb name as the key
    # (SAL would be better, but it wasn't provided)
    sal_crime = {}

    for i in range(1, len(crime_data)):
        suburb = crime_data[i][4]  # get suburb name
        crime = crime_data[i][8]  # get number of crime incidents
        if suburb in sal_crime:
            sal_crime[suburb] += int(crime.replace(',', ''))
        else:
            sal_crime[suburb] = int(crime.replace(',', ''))

    header_row = ['sal_code', 'sal_name', 'population', 'median_weekly_rent','crime_incidents', 'crime_rate']

    # Create new csv
    f = open('suburb_crime_and_quick_stats.csv', 'w')
    writer = csv.writer(f)
    writer.writerow(header_row)

    # From quick stats and crime data, write rows to csv
    for i in range(1, len(quick_stats)):
        sal_code = quick_stats[i][0]
        sal_name = quick_stats[i][1]
        population = quick_stats[i][2]
        rent = quick_stats[i][3]
        crime = sal_crime[sal_name] if sal_name in sal_crime else 0
        crime_rate = crime/int(population) if int(population) != 0 else 0
        row = [sal_code, sal_name, population, rent, crime, crime_rate]
        writer.writerow(row)

    f.close()


crimeStats()
