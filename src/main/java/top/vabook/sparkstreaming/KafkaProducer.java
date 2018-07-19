package top.vabook.sparkstreaming;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

/**
 * @author vabook@163.com
 * @version 2018/7/19 16:49
 * 流程
 * 1.prop
 * 2.conf
 * 3.producer
 * 4.send
 */
public class KafkaProducer {
    public static void main(String args[]){
        Properties prop = new Properties();
        prop.setProperty("zk.connect","Centos-01:2181");
        prop.setProperty("metadata.broker.list","Cenots-03:9092");
        ProducerConfig config = new ProducerConfig(prop);

        //由配置文件创建一个生产者
        Producer<String, String> producer = new Producer<String, String>(config);
        for (int i = 100 ; i <= 100 ; i ++ ){
            producer.send(new KeyedMessage<String, String>("vabook","it" + i + ""));
        }

    }
}
