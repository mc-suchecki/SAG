""" This is the main file of web server that is simulating stack exchange market. """
__author__ = 'mc'

from server.web.server import WebServer
import logging
import sys


def main():
    """ Main function of stack exchange simulation. """
    logging.basicConfig(level=logging.INFO)
    server = WebServer()
    server.run()

if __name__ == '__main__':
    sys.exit(main())
