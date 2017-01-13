import argparse
import requests
import urllib

def usage():
    print("Usage:")
    print("python main.py [-p <port>] --id <applicaionid> -g <groupId> -f <packagePath/fileName> -l <line>")
    print("python main.py [-p <port>] --id <applicaionid> --hl -g <groupId> -f <packagePath/fileName> -l <line>")
    print("python main.py [-p <port>] --id <applicaionid> -j -f <packagePath/fileName> -l <line>")
    print("python main.py [-p <port>] --id <applicaionid> --rg <removeGroupId>")
    print("python main.py [-p <port>] --id <applicaionid> --tw <taggedWords> --rf <packagePath/fileName> --rl <relatedline>")
    print("python main.py [-p <port>] --id <applicaionid> --qg <querygroupId>")
    print("python main.py [-p <port>] --id <applicaionid> -i")
    print("python main.py [-p <port>] -v")

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("-p", dest="port", action="store", default=8097, help="")
    parser.add_argument("--id", dest="applicationid", action="store")
    parser.add_argument("-f", dest="fileName", action="store")
    parser.add_argument("-g", dest="groupId", action="store", default=-1)
    parser.add_argument("-l", dest="lines", action="append", help="Add line" )
    parser.add_argument("--hl", dest="hlflag", action="store_true", default=False, help="Just highlight" )
    parser.add_argument("--rg", dest="removeGroupId", action="store", default=-1)
    parser.add_argument("-j", dest="navigateflag", action="store_true", default=False, help="Just navigate")
    parser.add_argument("--tw", dest="taggedWords", action="store", default="")
    parser.add_argument("--rf", dest="relatedFileName", action="store")
    parser.add_argument("--rl", dest="relatedline", action="store")
    parser.add_argument("--qg", dest="querygroupId", action="store")
    parser.add_argument("-i", dest="projectInfo", action="store_true", default=False, help="Query Project Information")
    parser.add_argument('-v', dest='version', action= "store_true", default=False)
    args = parser.parse_args()
    url = 'http://localhost:%s?' % (args.port)
    if args.hlflag:
        parameter = {"id" : args.applicationid, "Operation":"HightLight", "fileName": args.fileName, "groupId":args.groupId, "lines": "-".join(args.lines)}
    elif args.navigateflag:
        parameter = {"id" : args.applicationid, "Operation":"Navigate", "fileName": args.fileName, "lines": "-".join(args.lines)}
    elif args.projectInfo:
        parameter = {"id" : args.applicationid, "Operation": "QueryProjectInfo"}
    elif args.groupId != -1:
        parameter = {"id" : args.applicationid, "Operation":"HightLightAndNavigate", "fileName": args.fileName, "groupId":args.groupId, "lines": "-".join(args.lines)}
    elif args.removeGroupId != -1:
        parameter = {"id" : args.applicationid, "Operation":"RemoveHightLight", "removeGroupId": args.removeGroupId}
    elif args.taggedWords != "":
        parameter = {"id" : args.applicationid, "Operation":"Tag", "taggedWords": args.taggedWords, "relatedFileName": args.relatedFileName, "relatedline": args.relatedline }
    elif args.version:
        parameter = {"Operation":"Version"}
    elif args.querygroupId != "":
        parameter = {"id" : args.applicationid, "Operation":"Query", "querygroupId": args.querygroupId}

    r  = requests.get(url + urllib.urlencode(parameter), timeout=10)
    print(r.url)
    print(r.content)

if __name__ == "__main__":
    main()