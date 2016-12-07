#-*- coding: utf-8 -*-
import os, os.path, sys, re, time, datetime, urllib.request
from html.parser import HTMLParser

class factParser(HTMLParser):
    def __init__(self):
        self.taglevels = []
        self.handledtags = ['span', 'div', 'a']
        self.processing = None
        self.addrs = ['State', 'City', 'Zipcode']
        HTMLParser.__init__(self)
    def handle_starttag(self, tag, attrs):
        if tag in self.handledtags:
            if tag == 'span':
                for name, value in attrs:
                    if name == 'class' and value == 'addr_bbs':
                        self.data = ''
                        self.processing = tag
                    elif name == 'class' and value == 'zsg-breadcrumbs-text':
                        self.data = ''
                        self.processing = tag
            elif tag == 'div':
                for name, value in attrs:
                    if name == 'class' and value == 'zest-value':
                        self.data = ''
                        self.processing = tag
            elif tag == 'a':
                for name, value in attrs:
                    if name == 'data-ga-label' and value in self.addrs:
                        self.data = ''
                        self.processing = tag

    def handle_data(self, data):
        if self.processing:
            self.data += data
            print(data)
    def handle_endtag(self, tag):
        if tag == self.processing:
            self.processing = None

f = open('url.txt', 'r')
while True:
    url = f.readline()
    if not url:
        break
    else:
    	fh = urllib.request.urlopen(url)
    	html = fh.read().decode("utf8")
    	tp = factParser()
    	tp.feed(html)
    	print('')
