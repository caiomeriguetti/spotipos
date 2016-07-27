import redis
import json

r = redis.StrictRedis(host='localhost', port=6379, db=0)

#loading provinces

provincesData = json.loads(open('provinces.json').read())
provincesKey = "province-data:"
pipe = r.pipeline()
for provinceName in provincesData:
	provinceData = provincesData[provinceName]
	provinceKey = provincesKey + provinceName
	pipe.delete(provinceKey)
	pipe.hset(provinceKey, 'ulx', provinceData['boundaries']['upperLeft']['x'])
	pipe.hset(provinceKey, 'uly', provinceData['boundaries']['upperLeft']['y'])
	pipe.hset(provinceKey, 'brx', provinceData['boundaries']['bottomRight']['x'])
	pipe.hset(provinceKey, 'bry', provinceData['boundaries']['bottomRight']['y'])
pipe.execute()


#loading properties

propertiesData = json.loads(open('properties.json').read())
propertiesKey = "properties-data:"
pipe = r.pipeline()
properties = propertiesData['properties']
for propertie in properties:
	propertieKey = propertiesKey + str(propertie['id'])
	pipe.delete(propertieKey)
	pipe.hset(propertieKey, 'id', propertie['id'])
	pipe.hset(propertieKey, 'x', propertie['x'])
	pipe.hset(propertieKey, 'y', propertie['y'])
	pipe.hset(propertieKey, 'beds', propertie['beds'])
	pipe.hset(propertieKey, 'baths', propertie['baths'])
	pipe.hset(propertieKey, 'squareMeters', propertie['squareMeters'])

pipe.execute()

