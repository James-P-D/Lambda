# λ-parser

import sys
from FileParser import parser

"""documentation"""
def usage():
    print("Usage: python lambda.py source-file [options]")
    print("[options]:  /d - debug mode")
    print();
    print("Example: python lambda.py factorial.lbd /d")
    sys.exit(1)

def main():
    #if (len(sys.argv) <= 1):
    #    usage()

    my_parser = parser.stringparser()
    #inputFilename = sys.argv[1]
    #tokens = my_parser.parse(inputFilename)
    tokens = my_parser.parse("c:\test.lbd")
    # for somearg in sys.argv[1:]:
    #    print(somearg)

if __name__ == '__main__':
    main()