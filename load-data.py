import redis
import json

r = redis.StrictRedis(host='localhost', port=6379, db=0)


#loading provinces
provincesData = json.loads(open('provinces.json').read())
provincesKey = "province-data:"
pipe = r.pipeline()
minX = 0
minY = 0
maxX = 0
maxY = 0
for provinceName in provincesData:
	provinceData = provincesData[provinceName]
	provinceKey = provincesKey + provinceName
	pipe.delete(provinceKey)

	ulx = provinceData['boundaries']['upperLeft']['x']
	uly = provinceData['boundaries']['upperLeft']['y']
	brx = provinceData['boundaries']['bottomRight']['x']
	bry = provinceData['boundaries']['bottomRight']['y']
	
	if brx > maxX: maxX = brx
	if ulx > maxX: maxX = ulx
	if bry > maxY: maxY = bry
	if uly > maxY: maxY = uly

	pipe.hset(provinceKey, 'ulx', ulx)
	pipe.hset(provinceKey, 'uly', uly)
	pipe.hset(provinceKey, 'brx', brx)
	pipe.hset(provinceKey, 'bry', bry)

pipe.execute()

yPartitions = 5
xPartitions = 5
partitionWidth = maxX/xPartitions
partitionHeight = maxY/yPartitions
partitionsKey = "partition:"

r.delete("allpartitions")
partitionsById = {}
partitionIds = []
for i in range(1, yPartitions + 1):
	for j in range(1, xPartitions + 1):
		partitionId = str(i) + "_" + str(j)
		partitionIds.append(partitionId)
		uly = i*partitionHeight
		ulx = (j - 1)*partitionWidth
		bry = (i - 1)*partitionHeight
		brx = j*partitionWidth
		partitionKey = partitionsKey + partitionId
		r.hset(partitionKey, 'ulx', ulx)
		r.hset(partitionKey, 'uly', uly)
		r.hset(partitionKey, 'brx', brx)
		r.hset(partitionKey, 'bry', bry)

		partitionsById[partitionId] = {'ulx': ulx, 'uly': uly, 'brx': brx, 'bry': bry}

		r.rpush("allpartitions", partitionId)
		
print partitionWidth, partitionHeight, maxX, maxY
print r.lrange("allpartitions", 0, -1)

#loading properties

def getPartitionId(x, y):
	for partitionId in partitionIds:
		bounds = partitionsById[partitionId]
		if x >= int(bounds['ulx']) and y <= int(bounds['uly']) and x <= int(bounds['brx']) and y >= int(bounds['bry']):
			return partitionId

	return None

propertiesData = json.loads(open('properties.json').read())
propertiesKey = "properties-data:"
pipe = r.pipeline()
properties = propertiesData['properties']
k = 0
for propertie in properties:
	propertieKey = propertiesKey + str(propertie['id'])
	pipe.delete(propertieKey)
	pipe.sadd('allids', propertie['id'])
	pipe.hset(propertieKey, 'id', propertie['id'])
	pipe.hset(propertieKey, 'x', propertie['x'])
	pipe.hset(propertieKey, 'y', propertie['y'])
	pipe.hset(propertieKey, 'beds', propertie['beds'])
	pipe.hset(propertieKey, 'baths', propertie['baths'])
	pipe.hset(propertieKey, 'squareMeters', propertie['squareMeters'])
	k += 1

	partitionId = getPartitionId(int(propertie['x']), int(propertie['y']))
	
	pipe.sadd("idsByPartition:" + partitionId, propertie['id'])

	#print k, partitionId

pipe.execute()

