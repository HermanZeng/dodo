literature = {'小说', '外国文学', '文学', '随笔', '中国文学', '经典', '日本文学', '散文', '村上春树', '童话', '诗歌', '杂文', '王小波', '儿童文学', '古典文学',
              '张爱玲', '余华', '名著', '当代文学', '钱钟书', '鲁迅', '外国名著', '诗词', '茨威格', '米兰', '昆德拉', '杜拉斯', '港台'}

popular = {'漫画', '绘本', '推理', '青春', '言情', '科幻', '东野圭吾', '悬疑', '武侠', '奇幻', '韩寒', '日本漫画', '耽美', '亦舒', '三毛', '安妮宝贝', '网络小说',
           '推理小说', '郭敬明', '穿越', '金庸', '轻小说', '阿加莎', '克里斯蒂', '几米', '张小娴', '魔幻', '幾米', '青春文学', '科幻小说', '罗琳', '高木直子', '古龙',
           '沧月', '落落', '张悦然', '蔡康永'}

culture = {'历史', '心理学', '哲学', '传记', '文化', '社会学', '艺术', '设计', '政治', '社会', '建筑', '宗教', '电影', '数学', '政治学', '回忆录', '思想',
           '中国历史', '国学', '音乐', '人文', '戏剧', '人物传记', '绘画', '艺术史', '佛教', '军事', '西方哲学', '近代史', '二战', '考古', '自由主义', '美术'}

life = {'爱情', '旅行', '生活', '成长', '励志', '心理', '摄影', '女性', '职场', '美食', '教育', '游记', '灵修', '健康', '情感', '手工', '养生', '两性',
        '人际关系', '家居', '自助游'}

economics = {'经济学', '管理', '经济', '金融', '商业', '投资', '营销', '创业', '理财', '广告', '股票', '企业史', '策划'}

technology = {'科普', '互联网', '编程', '科学', '交互设计', '用户体验', '算法', '科技', '通信', '交互', '神经网络', '程序'}

learning = {'学习', '英语', '词汇', '教材', '工具书', '算法', '编程'}

CATEGORIES = {1: literature, 2: popular, 3: culture, 4: life, 5: economics, 6: technology, 7: learning}


def parse_category(tags):
    '''

    :param tags:
    :return: a list of ref
    '''
    ret = []

    for tag in tags:
        for category_id in CATEGORIES:
            if tag['name'] in CATEGORIES[category_id] and category_id not in ret:
                ret.append(category_id)
    return ret

if __name__ == '__main__':
    data = '''

科普(390504)	互联网(179122)	编程(120109)	科学(88335)
交互设计(57037)	用户体验(45272)	算法(39518)	web(19680)
科技(14229)	UE(4433)	通信(4009)	交互(3857)
UCD(3407)	神经网络(1691)	程序(1093)
    '''

    import re

    items = re.findall(r"([\u4e00-\u9fa5]+)", data)
    print(items)

    tags =[
    {
      "count": 21,
      "name": "工具书",
      "title": "工具书"
    },
    {
      "count": 17,
      "name": "英语",
      "title": "英语"
    },
    {
      "count": 11,
      "name": "图解",
      "title": "图解"
    },
    {
      "count": 10,
      "name": "英语学习",
      "title": "英语学习"
    },
    {
      "count": 8,
      "name": "英语词典",
      "title": "英语词典"
    },
    {
      "count": 4,
      "name": "词典",
      "title": "词典"
    },
    {
      "count": 4,
      "name": "牛津",
      "title": "牛津"
    },
    {
      "count": 3,
      "name": "英国",
      "title": "英国"
    }
  ]
    type(CATEGORIES)
    print(parse_category(tags))
