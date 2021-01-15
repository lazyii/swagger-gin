package org.rainday.swagger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

/**
 * Created by admin on 2021/1/15 14:09:05.
 */
public class JacksonMixinTest {
    
    // static 保证全局使用一个mapper对象
    static ObjectMapper mapper = new ObjectMapper().addMixIn(Address.class, AddressMixin.class);
    
    @Test
    public void multiThreadJacksonTest() throws Exception {
        int count = 100000;
        
        AtomicInteger test = new AtomicInteger();
        AtomicInteger test1 = new AtomicInteger();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            final int m = i;
            CompletableFuture.runAsync(() -> {
                String result;
                if (m % 2 == 0) {
                    result = mixinTest();
                    if (result.contains("name")) {
                        test.incrementAndGet();
                    }
                } else {
                    result = mixinTest1();
                    if (result.contains("pp")) {
                        test1.incrementAndGet();
                    }
                }
                latch.countDown();
            }, executor);
        }
        latch.await();
        System.out.println(String.format("testCounter: %s , testCounter1: %s", test.get(), test1.get()));
    }
    
    //https://sq.163yun.com/blog/article/200740005351452672
    @Test
    public void jacksonMixinTest() {
        System.out.println(mixinTest());
        System.out.println(mixinTest1());
    }
    
    @Test
    public void jacksonMixinTest1() {
        System.out.println(mixinTest());
        System.out.println(mixinTest1());
    }
    
    public String mixinTest() {
        try {
            Address address = new Address();
            address.setSt("mixin");
            address.setName("种花家");
            address.setPp("pp");
            return mapper.writerWithView(NameView.class).writeValueAsString(address);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String mixinTest1() {
        try {
            Address address = new Address();
            address.setSt("mixin1");
            address.setName("种花家1");
            address.setPp("pp1");
            return mapper.writerWithView(PpView.class).writeValueAsString(address);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Test
    public void jsonNodeTest() throws JsonProcessingException {
        JsonNode node = mapper.readTree("{\"a\":\"1ss23\"}");
        System.out.println(node.get("a").isTextual() + " " + node.get("a").textValue());
    }
    
    
    class Address {
        
        private String st;
        private String name;
        private String pp;
        
        public String getSt() {
            return st;
        }
        
        public void setSt(String st) {
            this.st = st;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getPp() {
            return pp;
        }
        
        public void setPp(String pp) {
            this.pp = pp;
        }
    }
    
    
    class AddressMixin {
        @JsonProperty("state")
        String st;
        
        @JsonView({NameView.class})
        String name;
        
        @JsonView({PpView.class})
        String pp;
    }
    
    interface NameView {}
    interface PpView {}
    
}
