#!/usr/bin/python
import optparse
import sys
import unittest

USAGE = """%prog SDK_PATH TEST_PATH
Run unit tests for App Engine apps.

SDK_PATH    Path to the SDK installation
TEST_PATH   Path to package containing test modules"""


def main(sdk_path, test_path):
    sys.path.insert(0, sdk_path)
    import dev_appserver
    dev_appserver.fix_sys_path()
    suite = unittest.loader.TestLoader().discover(test_path, pattern='*TestCases.py')
    unittest.TextTestRunner(stream=sys.stdout, verbosity=2).run(suite)


if __name__ == '__main__':
    parser = optparse.OptionParser(USAGE)
    options, args = parser.parse_args()
    if len(args) == 0:
        #parser.print_help()

        SDK_PATH = "/usr/local/google_appengine"
        TEST_PATH =  "./UnitTest"
        print ('Using default paths\n    sdk:%s\n    test:%s' % (SDK_PATH, TEST_PATH))
        
    else:
        SDK_PATH = args[0]
        TEST_PATH = args[1]

    main(SDK_PATH, TEST_PATH)
    