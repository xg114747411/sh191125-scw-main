import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JedisTest {
    //测试方法1：  发送验证码
    // 根据手机号码判断该手机号码在redis中是否有未过期的验证码   ， 或者获取验证码的次数超过3次
    // 随机生成6位的字符串，存到redis中(String 带过期时间)  ，发送成功在redis更新该手机号码获取验证码的次数(计数器)


    //测试方法2：   注册验证验证码
    // 获取redis中的验证码和提交的注册请求参数中的验证码 比较是否一致，如果一致则注册，
    //注册成功则删除redis中的缓存的验证码
    @Test
    public void test(){
        //远程连接redis服务
        //1、获取redis连接对象
        Jedis jedis = new Jedis("192.168.200.168", 6379);
        //2、使用连接对象操作redis：发送命令
        String ping = jedis.ping();//jedis将redis的原生命令都封装成了对应的方法
        System.out.println("ping = " + ping);
        //存储String的值，并设置过期时间30秒
        jedis.setex("k1" ,30 , "v1");

        Long ttl = jedis.ttl("k1");
        System.out.println("ttl = " + ttl);

        Boolean exists = jedis.exists("k1");
        System.out.println("k1键存在：exists = " + exists);

        Long setnx = jedis.setnx("k1", "hehe");
        System.out.println("setnx = " + (setnx==1?"设置成功":"设置失败"));

        jedis.del("k1");

        //===========
        System.out.println("========================================");
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("zhenguodegushi" , 1000d);
        map.put("laojidegushi" , 10d);
        map.put("laoshadegushi" , 2000d);
        map.put("fangfangdegushi" , 32d);

        jedis.zadd("topn" , map);

        Set<Tuple> topn = jedis.zrevrangeWithScores("topn", 0, 2);
        for (Tuple tuple : topn) {
            System.out.println("element:"+ tuple.getElement()+" , tuple.getScore() = " + tuple.getScore());
        }
        //3、关闭redis客户端连接
        jedis.close();
    }
}
