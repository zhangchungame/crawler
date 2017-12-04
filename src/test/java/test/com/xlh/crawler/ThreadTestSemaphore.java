package test.com.xlh.crawler;

import com.xlh.crawler.Application;
import com.xlh.crawler.dto.CdmEntDtoCorpInfo;
import com.xlh.crawler.dto.ProxyDaXiang;
import com.xlh.crawler.dto.SqlLimit;
import com.xlh.crawler.mapper.CdmEntDtoCorpInfoMapper;
import com.xlh.crawler.mapper.CraCorpInfoMapper;
import com.xlh.crawler.utils.ProxyUtil;
import com.xlh.crawler.utils.ShZhenXinThread;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.concurrent.Semaphore;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration

public class ThreadTestSemaphore {
    private static Logger logger = LoggerFactory.getLogger(SHTest.class);
    @Autowired
    private CdmEntDtoCorpInfoMapper cdmEntDtoCorpInfoMapper;
    @Autowired
    private CraCorpInfoMapper craCorpInfoMapper;

    @Test
    public void mysql() throws Exception {
        while(true){
            Thread.sleep(1000);
            Example example = new Example(CdmEntDtoCorpInfo.class);
            example.createCriteria().andLike("enterpriseName", "%上海%").andEqualTo("status", 0);
            int count = cdmEntDtoCorpInfoMapper.selectCountByExample(example);

            SqlLimit sqlLimit = new SqlLimit();
            int page = 0;
            int totalPage = count / 10;


            Semaphore semaphore=new Semaphore(10);//总共有5个许可
            while (page <= totalPage) {
                sqlLimit.setStart(0);
                sqlLimit.setLimit(5);
                List<CdmEntDtoCorpInfo> list = cdmEntDtoCorpInfoMapper.selectByPage(sqlLimit);
                if(list.size()<1){
                    break;
                }
                ProxyDaXiang daXiang=ProxyUtil.getProxy();
                for (int i = 0; i < list.size(); i++) {
                    semaphore.acquire();
                    ShZhenXinThread shZhenXinThread= new ShZhenXinThread(semaphore,list.get(i),daXiang);
                    shZhenXinThread.setCdmEntDtoCorpInfoMapper(cdmEntDtoCorpInfoMapper);
                    shZhenXinThread.setCraCorpInfoMapper(craCorpInfoMapper);
                    shZhenXinThread.start();
                }
                page++;
            }
        }
    }

//    @Test
//    public void threadTest() throws InterruptedException {
//        Semaphore semaphore=new Semaphore(5);//总共有5个许可
//        for(int i=0;i<100;i++){//定义七个吃的线程
//            semaphore.acquire();//获取一个许可，当然也可以调用acquire(int)，这样一个线程就能拿到多个许可
////            new ShZhenXinThread(semaphore).start();
//        }
//    }
}
