import sys
import getopt
import requests

def usage():
    print("Usage:")
    print("python main.py [-p <port>] -g <groupId> -f <fileName> -l <lines>")
    print("python main.py [-p <port>] --hl -g <groupId> -f <fileName> -l <lines>")
    print("python main.py [-p <port>] -j -f <fileName> -l <lines>")
    print("python main.py [-p <port>] --rg <removeGroupId>")
    print("python main.py [-p <port>] --tw <taggedWords> --rf <relatedFileName> --rl <relatedline>")
    print("python main.py --clear -f <fileName>")

def main():
    fileName, relatedFileName = "", ""
    groupId, removeGroupId = "", ""
    lines, relatedline = "", ""
    taggedWords = ""
    clear = False
    port = 8097
    hlflag = False
    navigateflag = False
    try:
        opts, args = getopt.getopt(sys.argv[1:], "hf:g:l:p:c:j", ["help", "rg=", "tw=",
                                                                "rf=", "rl=", "clear", "hl"])
    except getopt.GetoptError:
        usage()
        sys.exit(2)
    for opt, arg in opts:
        if opt in ("-h", "--help"):
            usage()
            sys.exit(0)
        if opt in ("-c", "--clear"):
            clear = True
        if opt in ("-p"):
            port = arg
        elif opt in ("-f"):
            fileName = arg
        elif opt in ("-g"):
            groupId = arg
        elif opt in ("-l"):
            lines = arg
        elif opt in ("--rg"):
            removeGroupId = arg
        elif opt in ("--tw"):
            taggedWords = arg
        elif opt in ("--rf"):
            relatedFileName = arg
        elif opt in ("--rl"):
            relatedline = arg
        elif opt in ("--hl"):
            hlflag = True
        elif opt in ("-j"):
            navigateflag = True
    if clear:
        r  = requests.get( 'http://localhost:%s?Operation=%s&fileName=%s' % (port, "Clear", fileName))
    elif hlflag:
        r  = requests.get( 'http://localhost:%s?Operation=%s&fileName=%s&groupId=%s&lines=%s' %
                           (port, "HightLight",fileName, groupId, lines))
    elif navigateflag:
        r  = requests.get( 'http://localhost:%s?Operation=%s&fileName=%s&lines=%s' %
                           (port, "Navigate",fileName, lines))
    elif groupId != "":
        r  = requests.get( 'http://localhost:%s?Operation=%s&fileName=%s&groupId=%s&lines=%s' %
                           (port, "HightLightAndNavigate",fileName, groupId, lines))
    elif removeGroupId != "":
        r  = requests.get( 'http://localhost:%s?Operation=%s&removeGroupId=%s' %
                           (port, "RemoveHightLight", removeGroupId))
    elif taggedWords != "":
        r  = requests.get( 'http://localhost:%s?Operation=%s&taggedWords=%s&relatedFileName=%s&relatedline=%s'
                           % (port, "Tag", taggedWords, relatedFileName, relatedline))

if __name__ == "__main__":
    main()
