import requests

dict = {"port":8097, "fileName": "AndroidManifest.xml", "line":1, "col":7, "offsetline":8}
r  = requests.get( 'http://localhost:%s?fileName=%s&line=%s&col=%d&offsetline=%d' % (dict['port'], dict['fileName'], dict['line'], dict['col'], dict['offsetline']))
print(r.status_code)
