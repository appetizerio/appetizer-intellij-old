import argparse
import requests

def usage():
    print("Usage:")
    print("python main.py [-p <port>] --id <applicaionid> -g <groupId> -f <fileName> -l <line>")
    print("python main.py [-p <port>] --id <applicaionid> --hl -g <groupId> -f <fileName> -l <line>")
    print("python main.py [-p <port>] --id <applicaionid> -j -f <fileName> -l <line>")
    print("python main.py [-p <port>] --id <applicaionid> --rg <removeGroupId>")
    print("python main.py [-p <port>] --id <applicaionid> --tw <taggedWords> --rf <relatedFileName> --rl <relatedline>")
    print("python main.py [-p <port>] --id <applicaionid> --qg <querygroupId>")

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
    parser.add_argument('--version', action='version', version='appetizer plugin 1.0.0')

    args = parser.parse_args()
    if args.hlflag:
        r  = requests.get( 'http://localhost:%s?id=%s&Operation=%s&fileName=%s&groupId=%s&lines=%s' %
                           (args.port, args.applicationid, "HightLight",args.fileName, args.groupId, "-".join(args.lines)))
    elif args.navigateflag:
        r  = requests.get( 'http://localhost:%s?id=%s&Operation=%s&fileName=%s&lines=%s' %
                           (args.port, args.applicationid, "Navigate",args.fileName, "-".join(args.lines)))
    elif args.groupId != -1:
        r  = requests.get( 'http://localhost:%s?id=%s&Operation=%s&fileName=%s&groupId=%s&lines=%s' %
                           (args.port, args.applicationid, "HightLightAndNavigate",args.fileName, args.groupId, "-".join(args.lines)))
    elif args.removeGroupId != -1:
        r  = requests.get( 'http://localhost:%s?id=%s&Operation=%s&removeGroupId=%s' %
                           (args.port, args.applicationid, "RemoveHightLight", args.removeGroupId))
    elif args.taggedWords != "":
        r  = requests.get( 'http://localhost:%s?id=%s&Operation=%s&taggedWords=%s&relatedFileName=%s&relatedline=%s'
                           % (args.port, args.applicationid, "Tag", args.taggedWords, args.relatedFileName, args.relatedline))
    elif args.querygroupId != "":
        r  = requests.get( 'http://localhost:%s?id=%s&Operation=%s&querygroupId=%s'
                           % (args.port, args.applicationid, "Query",args.querygroupId))
    print(r.url)
    print(r.content)

if __name__ == "__main__":
    main()

