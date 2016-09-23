import pycurl
import io
import json

jsonStr = '{"port":8097, "fileName":"plugin.xml", "line":5, "col":5, "offsetline":4}'
jsonStr1 = '{"port":8091, "fileName":"plugin.xml", "line":2, "col":0, "offsetline":4}'
jsonStr2 = '{"port":8091, "fileName":"plugin.xml", "line":2, "col":5, "offsetline":0}'
jsonStr3 = '{"port":8091, "fileName":"plugin.xml", "line":2, "col":0, "offsetline":0}'

cj = json.loads(jsonStr)


buf = io.StringIO()
curl = pycurl.Curl()
curl.setopt(curl.URL, 'http://localhost:%s?message=%s:%d:%d:%d' % (cj['port'], cj['fileName'], cj['line'], cj['col'], cj['offsetline']))
curl.setopt(curl.WRITEFUNCTION, buf.write)
try:
    curl.perform()
    # todo: process return values
except pycurl.error as error:
    pass
buf.close()
