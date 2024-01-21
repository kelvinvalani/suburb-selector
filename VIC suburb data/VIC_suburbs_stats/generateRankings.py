# This file will create the ordered list of candidate suburbs
# for each university in Victoria. The list is ordered based
# on the data contained in the previous csv files. The data
# will be abstracted in to scores and the sum of these scores
# will determine the ordering of the candidate suburbs.

import csv


def generateRankings():
    # Get sal data data from csv
    with open('suburb_qs_crime_stops.csv', newline='') as f:
        reader = csv.reader(f)
        sal_stats = list(reader)
    f.close()

    # Remove header row
    sal_stats.pop(0)

    num_sal = len(sal_stats)

    # Rounding constant, can be changed to change level of score accuracy
    ROUNDING = 2

    # Create sal rankings dictionary
    rankings = {}
    rent = {}

    # Sort based on median weekly rent
    sal_stats.sort(key=lambda x: int(x[3]), reverse=True)

    # Initialise the dictionary and add rent ranking
    for i in range(len(sal_stats)):
        sal = sal_stats[i]
        rankings[sal_stats[i][1].lower()] = [round(i/num_sal, ROUNDING), 0, 0]
        rent[sal_stats[i][1].lower()] = sal_stats[i][3]

    # Sort based on crime rate
    sal_stats.sort(key=lambda x: float(x[5]), reverse=True)

    # Add crime rate ranking
    for i in range(len(sal_stats)):
        sal = sal_stats[i]
        rankings[sal_stats[i][1].lower()][1] = round(i/num_sal, ROUNDING)

    # Sort based on transport frequency score, highest is best
    sal_stats.sort(key=lambda x: float(x[8]))

    # Add transport frequency ranking
    for i in range(len(sal_stats)):
        sal = sal_stats[i]
        rankings[sal[1].lower()][2] = round(i/num_sal, ROUNDING)

    # Go through universities csv to generate rankings csv
    # Get university data from csv
    with open('unis.csv', newline='') as f:
        reader = csv.reader(f)
        unis = list(reader)
    f.close()

    header_row = ['coordinates', 'university', 'campus', 'suburb', 'candidate_ranking', 'rent_ranking', 'crime_ranking', 'transport_ranking', 'rent']

    # create new csv
    f = open('rankings.csv', 'w')
    writer = csv.writer(f)
    writer.writerow(header_row)

    # for each uni, get list of candidates, create ordered list based on summed rankings
    # and write the row to the final csv
    unis.pop(0)  # remove header
    for uni in unis:
        candidates = []
        for i in range(3, 23):
            if uni[i].lower() in rankings and sum(rankings[uni[i].lower()]) > 0:
                candidates.append(uni[i])
        candidates.sort(key=lambda x: sum(rankings[x.lower()]) if x.lower() in rankings else float('inf'), reverse=True)
        for rank, candidate in enumerate(candidates):
            row = [uni[2], uni[0], uni[1], candidate, rank+1]
            if candidate.lower() in rankings:
                row += rankings[candidate.lower()]
                row += [rent[candidate.lower()]]
                print(rent[candidate.lower()])
            writer.writerow(row)

    f.close


generateRankings()
