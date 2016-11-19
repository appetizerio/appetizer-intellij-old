import requests

dict = {"port":8097, "fileName": "MainActivity.java", "groupId":"0","removeGroupId":"0","lines":"4-10-12" ,"taggedWords": "apetizerName", "relatedFileName": "Appetizer.java", "relatedline":3, "cancelAll":"True"}
#r  = requests.get( 'http://localhost:%s?Operation=%s&fileName=%s&groupId=%s&lines=%s' % (dict['port'], "HightLight",dict['fileName'], dict['groupId'],dict['lines']))
#r  = requests.get( 'http://localhost:%s?Operation=%s&fileName=%s&removeGroupId=%s' % (dict['port'], "RemoveHightLight", dict['fileName'], dict['removeGroupId']))
#r  = requests.get( 'http://localhost:%s?Operation=%s&taggedWords=%s&relatedFileName=%s&relatedline=%d' % (dict['port'], "Tag", dict['taggedWords'], dict['relatedFileName'], dict['relatedline']))
print(r.status_code)

