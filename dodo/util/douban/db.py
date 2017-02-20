import pymysql


config = {
    'host': '127.0.0.1',
    'port': 3306,
    'user': 'root',
    'password': 'helloworld',
    'db': 'dodo',
    'charset': 'utf8',
    'cursorclass': pymysql.cursors.DictCursor,
}

def save_to_book(connection, id, wid, pages, title, publisher, introduction, rate, image, isbn10, isbn13):
    # connection = pymysql.connect(**config)

    with connection.cursor() as cursor:
        sql = 'INSERT INTO book (id, wid, pages, title, publisher, introduction, rate, image, isbn10, isbn13) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)'
        cursor.execute(sql, (id, wid, pages, title, publisher, introduction, rate, image, isbn10, isbn13))




def save_to_book_category(connection, book_id, category_id):
    # connection = pymysql.connect(**config)

    with connection.cursor() as cursor:
        sql = 'INSERT INTO book_category (book_id, category_id) VALUES (%s, %s)'
        cursor.execute(sql, (book_id, category_id))





def save_to_author(connection, id, name, nationality, introduction, image):
    # connection = pymysql.connect(**config)

    with connection.cursor() as cursor:
        sql = 'INSERT INTO author (id, name, nationality, introduction, image) VALUES (%s, %s, %s, %s, %s)'
        cursor.execute(sql, (id, name, nationality, introduction, image))





def save_to_author_book(connection, author_id, book_id):
    # connection = pymysql.connect(**config)

    with connection.cursor() as cursor:
        sql = 'INSERT INTO author_book (book_id, author_id) VALUES (%s, %s)'
        cursor.execute(sql, (book_id, author_id))



def save_to_translator(connection, id, name, nationality, introduction, image):
    # connection = pymysql.connect(**config)

    with connection.cursor() as cursor:
        sql = 'INSERT INTO translator (id, name, nationality, introduction, image) VALUES (%s, %s, %s, %s, %s)'
        cursor.execute(sql, (id, name, nationality, introduction, image))



def save_to_book_translator(connection, book_id, translator_id):
    # connection = pymysql.connect(**config)
    # print(book_id + ', ' + translator_id)

    with connection.cursor() as cursor:
        sql = 'INSERT INTO book_translator (book_id, translator_id) VALUES (%s, %s)'
        cursor.execute(sql, (book_id, translator_id))



if __name__ == '__main__':
    save_to_author_book('asdf', 'asdf42')