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
                if name == href:
                if name == 'class' and value == 'zsg-photo-card-overlay-link routable hdp-link routable mask hdp-link':
                    self.data = '' 
                    self.processing = tag 
    def handle_data(self, data): 
        if self.processing:
            self.data += data
            print(data)
    def handle_endtag(self, tag): 
        if tag == self.processing:
            self.processing = None

url = 'http://www.zillow.com/homes/for_sale/New-Bedford-MA-02740/pmf,pf_pt/house_type/58880_rid/4-_beds/globalrelevanceex_sort/41.644729,-70.914559,41.618041,-70.971551_rect/13_zm/'

fh = urllib.request.urlopen(url)
html = fh.read().decode("utf8")
tp = factParser()
tp.feed(html)
