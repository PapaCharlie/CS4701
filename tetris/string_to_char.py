#!/usr/bin/python

def main(filename):
    with open(filename + 'bin', 'w') as f:
        for line in open(filename):
            line = line.strip()
            if line[-1] == ',':
                line = line[:-1]
            k = map(int, line.split(','))
            for i in k:
                for c in xrange(3,-1,-1):
                    f.write(chr((i >> (c * 8)) & 255))
            f.write('\n')

if __name__ == '__main__':
    from sys import argv
    filename = argv[1]
    main(filename)