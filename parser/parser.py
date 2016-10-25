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

urls = ['http://www.zillow.com/homedetails/6-Lyndon-Rd-Sharon-MA-02067/57506814_zpid/',
'http://www.zillow.com/homedetails/6-Lothrup-Way-Sharon-MA-02067/57506702_zpid/',
'http://www.zillow.com/homedetails/366-East-St-Sharon-MA-02067/57507414_zpid/',
'http://www.zillow.com/homedetails/315-East-St-Sharon-MA-02067/57507352_zpid/',
'http://www.zillow.com/homedetails/23-Wilshire-Dr-Sharon-MA-02067/57506234_zpid/',
'http://www.zillow.com/homedetails/22-Wilshire-Dr-Sharon-MA-02067/57506844_zpid/',
'http://www.zillow.com/homedetails/123-Brook-Rd-Sharon-MA-02067/57507319_zpid/',
'http://www.zillow.com/homedetails/112-Oak-Hill-Dr-Sharon-MA-02067/57505591_zpid/',
'http://www.zillow.com/homedetails/41-Lyndon-Rd-Sharon-MA-02067/57506146_zpid/',
'http://www.zillow.com/homedetails/168-Wilshire-Dr-Sharon-MA-02067/57506256_zpid/',
'http://www.zillow.com/homedetails/14-Lois-Ln-Sharon-MA-02067/57508513_zpid/',
'http://www.zillow.com/homedetails/21-Deborah-Sampson-St-Sharon-MA-02067/57506708_zpid/',
'http://www.zillow.com/homedetails/161-Wilshire-Dr-Sharon-MA-02067/57506261_zpid/',
'http://www.zillow.com/homedetails/15-Robs-Ln-Sharon-MA-02067/57507331_zpid/',
'http://www.zillow.com/homedetails/675-Bay-Rd-Sharon-MA-02067/57507419_zpid/',
'http://www.zillow.com/homedetails/3-Abbott-Ave-Sharon-MA-02067/57506166_zpid/',
'http://www.zillow.com/homedetails/144-Oak-Hill-Dr-Sharon-MA-02067/57508517_zpid/',
'http://www.zillow.com/homedetails/145-Brook-Rd-Sharon-MA-02067/57507327_zpid/',
'http://www.zillow.com/homedetails/22-Juniper-Rd-Sharon-MA-02067/57505539_zpid/',
'http://www.zillow.com/homedetails/8-Manning-Way-Sharon-MA-02067/57506869_zpid/']
for url in urls:
	fh = urllib.request.urlopen(url)
	html = fh.read().decode("utf8")
	tp = factParser()
	tp.feed(html)
	print('')
