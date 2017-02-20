import json
import os
import requests
import time


class Hunter:

    def __init__(self):
        config_file = open('book_hunter.config', 'r')
        self.config = json.loads(config_file.read())
        config_file.close()

    def update_book_id(self):
        self.config['book_id'] += 1
        config_file = open('book_hunter.config', 'w')
        config_file.write(json.dumps(self.config))
        config_file.close()

    def do(self):
        while True:

            cache_path = self.config['base_path'] + self.get_new_dir_name()
            log_path = cache_path + '/log'

            self.create_directory(cache_path)
            self.create_directory(log_path)

            log_f = open(log_path + '/log.txt', 'a')
            url = self.config['base_url'] + str(self.config['book_id'])
            r = requests.get(url)

            data = json.loads(r.text)
            try:
                rating = float(data['rating']['average'])
                if rating > 8:
                    f = open(cache_path + '/' + str(self.config['book_id']) + '.json', 'w', encoding='utf-8')
                    f.write(r.text)
                    f.close()
                    msg = 'done ' + str(self.config['book_id'])
                    # print(msg)
                    log_f.write(time.asctime() + '\t' + msg + '\n')
                else:
                    msg = 'pass ' + str(self.config['book_id'])
                    # print(msg)
                    log_f.write(time.asctime() + '\t' + msg + '\n')
            except KeyError as e:
                msg = 'error: ' + r.text
                print(msg)
                log_f.write(time.asctime() + '\t' + msg + '\n')
            finally:
                log_f.close()
                self.update_book_id()
                time.sleep(self.config['period_second'])

    @staticmethod
    def create_directory(path):
        try:
            os.makedirs(path)
        except FileExistsError as e:
            pass

    @staticmethod
    def get_new_dir_name():
        return time.strftime("%Y-%m-%d/%Y-%m-%d %H")

if __name__ == '__main__':
    hunter = Hunter()
    hunter.do()