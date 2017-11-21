package test.com.xlh.crawler;

import com.xlh.crawler.dto.CdmEntDtoCorpInfo;
import com.xlh.crawler.dto.ProxyDaXiang;
import com.xlh.crawler.dto.SqlLimit;
import com.xlh.crawler.mapper.CdmEntDtoCorpInfoMapper;
import com.xlh.crawler.mapper.CraCorpInfoMapper;
import com.xlh.crawler.utils.ProxyUtil;
import com.xlh.crawler.utils.ShZhenXinThread;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.concurrent.Semaphore;



public class ThreadTestSemaphore {
    private static Logger logger = LoggerFactory.getLogger(SHTest.class);
    @Autowired
    private CdmEntDtoCorpInfoMapper cdmEntDtoCorpInfoMapper;
    @Autowired
    private CraCorpInfoMapper craCorpInfoMapper;

    @Test
    public void mysql() throws Exception {
        Example example = new Example(CdmEntDtoCorpInfo.class);
        example.createCriteria().andLike("enterpriseName", "%上海%").andEqualTo("status", 0);
        int count = cdmEntDtoCorpInfoMapper.selectCountByExample(example);

        SqlLimit sqlLimit = new SqlLimit();
        int page = 0;
        int totalPage = count / 10;
        ProxyDaXiang proxyDaXiang;
        int returnsize = 0;
        while (page <= totalPage) {
            proxyDaXiang = ProxyUtil.getProxy();
            sqlLimit = new SqlLimit();
            sqlLimit.setStart(page * 10);
            sqlLimit.setLimit(10);
            List<CdmEntDtoCorpInfo> list = cdmEntDtoCorpInfoMapper.selectByPage(sqlLimit);
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setStatus(1);
                try {
                    returnsize = test(list.get(i).getEnterpriseName(), proxyDaXiang);
                } catch (Exception e) {
                    logger.info("exception={}", e.getMessage());
                    try {
                        proxyDaXiang = ProxyUtil.getProxy();
                        returnsize = test(list.get(i).getEnterpriseName(), proxyDaXiang);
                    } catch (Exception e2) {
                        proxyDaXiang = ProxyUtil.getProxy();
                        list.get(i).setStatus(0);
                        logger.info("exception={}", e.getMessage());
                        continue;
                    }
                }
                if (returnsize == 0) {
                    list.get(i).setStatus(2);
                }
                cdmEntDtoCorpInfoMapper.updateByPrimaryKey(list.get(i));
                logger.info("更新了数据id为{}", list.get(i).getId());
            }
            page++;
        }


    }

    @Test
    public void threadTest() throws InterruptedException {
        Semaphore semaphore=new Semaphore(1);//总共有5个许可
        for(int i=0;i<100;i++){//定义七个吃的线程
            semaphore.acquire();//获取一个许可，当然也可以调用acquire(int)，这样一个线程就能拿到多个许可
            new ShZhenXinThread(semaphore).start();
        }
    }
}
