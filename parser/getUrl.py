#-*- coding: utf-8 -*-
import os, os.path, sys, re, time, datetime, urllib.request
from html.parser import HTMLParser
#<a href="/homedetails/198-Cornell-St-New-Bedford-MA-02740/55998508_zpid/" class="zsg-photo-card-overlay-link routable hdp-link routable mask hdp-link" id="yui_3_18_1_1_1475656807922_8825"></a>
class factParser(HTMLParser):
    def __init__(self):
        self.taglevels = []
        self.handledtags = ['a']
        self.processing = None
        HTMLParser.__init__(self)
    def handle_starttag(self, tag, attrs):
        if tag in self.handledtags:
            for name, value in attrs:
                # if name == 'href':
                #     print(name)
                #     print(value)
                #     print(dict(attrs)['href'])
                if name == 'class' and value == 'zsg-photo-card-overlay-link routable hdp-link routable mask hdp-link':
                    self.data = ''
                    self.processing = tag
                    print('http://www.zillow.com' + dict(attrs)['href'])
    def handle_data(self, data):
        if self.processing:
            self.data += data
            # print(data)
    def handle_endtag(self, tag):
        if tag == self.processing:
            self.processing = None

leftUrl = 'http://www.zillow.com/homes/recently_sold/'
middleUrl = 'Town-of-Sharon-MA-02067'
rightUrl = '/house,condo,apartment_duplex,townhouse_type/11_zm/'


for num in range(1, 20):
    url = leftUrl + middleUrl + rightUrl + str(num) + '_p'
    fh = urllib.request.urlopen(url)
    html = fh.read().decode("utf8")
    tp = factParser()
    tp.feed(html)
