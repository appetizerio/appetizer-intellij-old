import requests

dict = {"port":8097, "fileName": "MainActivity.java", "line":4, "col":8, "offsetline":2,"taggedWords": "apetizerName", "relatedFileName": "Appetizer.java", "relatedline":3, "cancelAll":"True"}
#r  = requests.get( 'http://localhost:%s?fileName=%s&line=%d&col=%d&offsetline=%d' % (dict['port'], dict['fileName'], dict['line'], dict['col'], dict['offsetline']))
r  = requests.get( 'http://localhost:%s?taggedWords=%s&relatedFileName=%s&relatedline=%d' % (dict['port'], dict['taggedWords'], dict['relatedFileName'], dict['relatedline']))
#r  = requests.get( 'http://localhost:%s?fileName=%s&line=%d&col=%d&offsetline=%d&taggedWords=%s&relatedFileName=%s&relatedline=%d'
#                   % (dict['port'], dict['fileName'], dict['line'], dict['col'], dict['offsetline'], dict['taggedWords'], dict['relatedFileName'], dict['relatedline']))
print(r.status_code)

