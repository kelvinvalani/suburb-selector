# This file will combine the recently created suburb_crime_and_quick_stats.csv
# With transport data from Public Transport Victoria. This new csv will contain
# The number of train stations and bus stops per SAL. As well as the frequency
# of public transport for that SAL.

import csv


def transportScores():
    directions = ["North", "South", "East", "West"]

    # Create 'linking' dictionaries
    sal_to_num_station, sal_to_num_stop, station_to_sal, stop_to_sal = {}, {}, {}, {}
    frequency_sum = {}

    # Get crime and quick stats data from csv
    with open('suburb_crime_and_quick_stats.csv', newline='') as f:
        reader = csv.reader(f)
        sal_stats = list(reader)
    f.close()

    # Remove header row
    sal_stats.pop(0)

    # Get number of train stations and stops
    for i in range(1, 7):
        with open('stops_files/stops_' + str(i) + '.csv', newline='') as f:
            reader = csv.reader(f)
            stops = list(reader)
        f.close()

        for stop in stops:
            sal_name = stop[1][stop[1].find('(')+1:stop[1].find(')')]  # Remove bracketed information
            sal_name = "Melbourne" if sal_name == "Melbourne City" else sal_name
            if "(" in sal_name:  # Case where multiple brackets exist
                sal_name = sal_name[0:sal_name.find('(')]
            if i < 3:  # if train station
                if sal_name in sal_to_num_station:
                    sal_to_num_station[sal_name] += 1
                else:
                    sal_to_num_station[sal_name] = 1
                    station_to_sal[stop[0]] = sal_name
            else:  # i >= 3, i.e. if not train station
                if sal_name in sal_to_num_stop:
                    sal_to_num_stop[sal_name] += 1
                else:
                    sal_to_num_stop[sal_name] = 1
                    stop_to_sal[stop[0]] = sal_name

    # If sal's 'parent' suburb has a station it should get half a point
    for key in sal_to_num_stop.keys():
        if key.split().pop(-1) in ["North", "South", "East", "West"]:
            parent = key.rsplit(' ', 1)[0]  # Remove last word (direction) from key
            if parent in sal_to_num_station and sal_to_num_station[parent] >= 1:
                if key in sal_to_num_station:
                    sal_to_num_station[key] += 0.5
                else:
                    sal_to_num_station[key] = 0.5

    trip_id_to_num_days = {}
    service = {}

    # Get trips from csv (trips contain key link (service_id) to calendar, which contains num. days information)
    for i in range(1, 7):
        # open file
        with open('trip_files/trips_' + str(i) + '.csv', newline='') as f:
            reader = csv.reader(f)
            trips = list(reader)
        f.close()

        # Initialise dictionary (key: trip_id, value: service_id)
        for j in range(1, len(trips)):
            trip_id_to_num_days[trips[j][2]] = trips[j][1]

    # Get calendar information from csv
    for i in range(1, 7):
        # open file
        with open('calendar_files/calendar_' + str(i) + '.csv', newline='') as f:
            reader = csv.reader(f)
            calendar = list(reader)
        f.close()

        for j in range(1, len(calendar)):
            # Initialise dictionary (key: service id, value: num days)
            service[calendar[j][0]] = int(calendar[j][1])
            for k in range(2, 8):
                service[calendar[j][0]] += int(calendar[j][k])

    # Create link from trip id -> service id <- num days
    for Key, Value in trip_id_to_num_days.items():
        trip_id_to_num_days[Key] = service[Value]

    # Get stop times data from csv
    for i in range(1, 7):
        with open('stop_times_files/stop_times_' + str(i) + '.csv', newline='') as f:
            reader = csv.reader(f)
            stop_times = list(reader)
        f.close()

        for stop_time in stop_times:    # For each stop time
            if stop_time[3] in stop_to_sal:
                stop = stop_time[3]
                sal = stop_to_sal[stop]
                if stop_to_sal[stop] in frequency_sum:   # If the stop has been involved in counting yet
                    frequency_sum[sal] += 1 * trip_id_to_num_days[stop_time[0]]
                else:
                    frequency_sum[sal] = 1 * trip_id_to_num_days[stop_time[0]]
            elif stop_time[3] in station_to_sal:
                station = stop_time[3]
                sal = station_to_sal[station]
                if sal in frequency_sum:
                    frequency_sum[sal] += 1 * trip_id_to_num_days[stop_time[0]]
                    for direction in directions:
                        if sal + direction in frequency_sum:
                            frequency_sum[sal + direction] += 0.5 * trip_id_to_num_days[station]
                else:
                    frequency_sum[station_to_sal[stop_time[3]]] = 1 * trip_id_to_num_days[stop_time[0]]
                    for direction in directions:
                        if sal + direction in frequency_sum:
                            frequency_sum[sal + direction] = 0.5 * trip_id_to_num_days[station]

    header_row = ['sal_code', 'sal_name', 'population', 'median_weekly_rent', 'crime_incidents', 'crime_rate',
                  'train_station', 'num_stops', 'transport_frequency']

    # create new csv
    f = open('suburb_qs_crime_stops.csv', 'w')
    writer = csv.writer(f)
    writer.writerow(header_row)

    # from quick stats and crime data, write rows to csv
    for i in range(1, len(sal_stats)):
        sal = sal_stats[i][1]
        if sal in sal_to_num_station:
            sal_stats[i].append(sal_to_num_station[sal])  # add train station information
        else:
            sal_stats[i].append(0)
        if sal in sal_to_num_stop:
            sal_stats[i].append(sal_to_num_stop[sal])  # add stops information
        else:
            sal_stats[i].append(0)
        if sal in frequency_sum:
            sal_stats[i].append(frequency_sum[sal])
        else:
            sal_stats[i].append(0)

        writer.writerow(sal_stats[i])

    f.close()


transportScores()
