#!/usr/bin/python

def main(filename):
    with open(filename + 'bin', 'w') as f:
        for line in open(filename):
            k = map(int, line.strip().split(','))
            for i in k:
                for c in xrange(3,-1,-1):
                    f.write(chr((i >> (c * 8)) & 255))
            f.write('\n')

if __name__ == '__main__':
    from sys import argv
    filename = argv[1]
    main(filename)