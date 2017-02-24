import argparse
import requests
import sys


def main():
    DEFAULT_BASE_URL = 'http://localhost:8097'
    parser = argparse.ArgumentParser()
    subparsers = parser.add_subparsers(help='commands', dest='cmd')
    parser.add_argument('--app', action='store', help='the application ID of the project')
    parser.add_argument('--base', action='store', help='the base URL of the API requests', default=DEFAULT_BASE_URL)

    version_parser = subparsers.add_parser('version', help='communicate with the plugin and retrieve its version')

    jump_parser = subparsers.add_parser('jump', help='jump to a specific line of a given class file of the project')
    jump_parser.add_argument('clazz', action='store', help='the fully qualified Java class name, e.g., com.example.MyExample')
    jump_parser.add_argument('line', action='store', help='the line number', type=int)

    highlight_parser = subparsers.add_parser('highlight', help='highlight some lines of a given class file and add these highlights to a highlight group')
    highlight_parser.add_argument('group', action='store', help='the highlight group ID')
    highlight_parser.add_argument('clazz', action='store', help='the fully qualified Java class name, e.g., com.example.MyExample')
    highlight_parser.add_argument('lines', action='store', help='the line number(s)', type=int, nargs='+')

    unhighlight_parser = subparsers.add_parser('unhighlight', help='remove a highlight group along with all its highlights')
    unhighlight_parser.add_argument('group', action='store', help='the highlight group ID')

    query_parser = subparsers.add_parser('qeury', help='query a highlight group')
    query_parser.add_argument('group', action='store', help='the highlight group ID')

    info_parser = subparsers.add_parser('info', help='get the project info')

    tagwords_parser = subparsers.add_parser('tagwords', help='tag specific word across the project')
    tagwords_parser.add_argument('word', action='store', help='the word')
    tagwords_parser.add_argument('clazz', action='store', help='the fully qualified Java class name, e.g., com.example.MyExample')
    tagwords_parser.add_argument('lines', action='store', help='the line number(s)', type=int, nargs='+')

    args = parser.parse_args().__dict__
    cmd = args['cmd']
    req = {}
    if cmd == 'version':
        req = {"Operation": "Version"}
    elif cmd == 'jump':
        req = {"id" : args['app'], "Operation": "Navigate", "fileName": args['clazz'], "line": str(args['line'])}
    elif cmd == 'highlight':
        req = {"id" : args['app'], "Operation": "HighLight", "fileName": args['clazz'], "groupId": args['group'], "lines": "-".join([str(i) for i in args['lines']]) }
    elif cmd == 'unhighlight':
        req = {"id" : args['app'], "Operation": "RemoveHighLight", "removeGroupId": args['group']}
    elif cmd == 'query':
        req = {"id" : args['app'], "Operation": "Query", "querygroupId": args['group']}
    elif cmd == 'info':
        req = {"id" : args['app'], "Operation": "QueryProjectInfo"}
    elif cmd == 'tagwords':
        req = {"id" : args['app'], "Operation": "Tag", "taggedWords": args['word'], "relatedFileName": args['clazz'], "relatedline": "-".join([str(i) for i in args['lines']]) }
    else:
        print('unknown command')
        return 1
    
    print(req)
    if cmd != 'version' and args['app'] is None:
        print('need to specify the app being operated on')
        print('next version would allow default project')  # TODO
        return 1
    r  = requests.get(args['base'], params=req, timeout=10)
    print(r.content)
    return 0

if __name__ == "__main__":
    sys.exit(main())
