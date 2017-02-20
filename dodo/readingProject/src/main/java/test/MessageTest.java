package test;

import com.alibaba.fastjson.JSON;
import entity.Global;
import entity.Message;
import service.MessageService;
import utilities.SpringIocUtil;

import java.util.List;

/**
 * Created by heming on 9/6/2016.
 */
public class MessageTest {
    public static void main(String args[]) {
        MessageService messageService = SpringIocUtil.getBean("messageService", MessageService.class);
        messageService.addMessage("58bc8a7d-469e-11e6-bf08-208984f5a994", "Congratulations! You unlock honor in track踏上佛学这条道儿", Global.Honor, "57d5793b3c7bc0e97ae38a60");
    }
}
