import requests

dict = {"port":8097, "fileName": "TestActivity.java", "line":5, "col":5, "offsetline":4}
r  = requests.get( 'http://localhost:%s?message=%s:%d:%d:%d' % (dict['port'], dict['fileName'], dict['line'], dict['col'], dict['offsetline']))
print(r.status_code)
