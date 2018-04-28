
def decodeList(data):
    rv = []
    for item in data:
        if isinstance(item, unicode):
            item = item.encode('utf-8')
        elif isinstance(item, list):
            item = decodeList(item)
        elif isinstance(item, dict):
            item = decodeDict(item)
        rv.append(item)
    return rv

def decodeDict(data):
    rv = {}
    for key, value in data.iteritems():
        if isinstance(key, unicode):
           key = key.encode('utf-8')
        if isinstance(value, unicode):
           value = value.encode('utf-8')
        elif isinstance(value, list):
           value = decodeList(value)
        elif isinstance(value, dict):
           value = decodeDict(value)
        rv[key] = value
    return rv
