import uuid
import json
import os.path

import pymysql

from category import parse_category

from db import save_to_book, save_to_author, save_to_author_book, save_to_translator, save_to_book_translator, \
    save_to_book_category

config = {
    'host': '127.0.0.1',
    'port': 3306,
    'user': 'root',
    'password': '123456789',
    'db': 'dodo',
    'charset': 'utf8',
    'cursorclass': pymysql.cursors.DictCursor,
}


def do(cache_dir, mute=False):
    for parent, dirnames, filenames in os.walk(cache_dir):
        for filename in filenames:
            if filename.endswith('.json'):
                with open(os.path.join(parent, filename), encoding='utf-8') as f:
                    if not mute:
                        print(filename)
                    connection = pymysql.connect(**config)
                    try:
                        data = json.loads(f.read())
                        bid = str(uuid.uuid1())
                        save_to_book(connection, bid, bid, data['pages'], data['title'], data['publisher'], data['summary'],
                                     data['rating']['average'], data['image'], data['isbn10'], data['isbn13'])

                        if data['author']:
                            for aut in data['author']:
                                aid = str(uuid.uuid1())
                                save_to_author(connection, aid, extract_author_name(aut), extract_nationality(aut),
                                               data['author_intro'], '')
                                save_to_author_book(connection, aid, bid)

                        if data['translator']:
                            for tra in data['translator']:
                                tid = str(uuid.uuid1())
                                save_to_translator(connection, tid, tra, '', '', '')
                                save_to_book_translator(connection, bid, tid)

                        for cat in parse_category(data['tags']):
                            save_to_book_category(connection, bid, cat)
                        connection.commit()
                    except Exception as e:
                        connection.rollback()
                        if not mute:
                            print('error: ' + filename + '\t' + str(e))
                    finally:
                        connection.close()


def extract_nationality(name):
    import re
    items = re.findall(r"[\(\[（【]([\u4e00-\u9fa5]+)[\]\)）】]", name)
    if items:
        return items[0]
    else:
        return ''


def extract_author_name(name):
    import re
    items = re.findall(r"[\(\[（【][\u4e00-\u9fa5]+[\]\)）】](.+)", name)
    if items:
        return items[0]
    else:
        return name


if __name__ == '__main__':
    path = r'E:\cache'
    do(path, mute=False)
