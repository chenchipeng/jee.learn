package com.jee.learn.test.mongodb;

import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.jee.learn.mongodb.LearnMongodbApplication;
import com.jee.learn.mongodb.domain.MgUser;
import com.jee.learn.mongodb.repository.MgUserRepository;
import com.jee.learn.mongodb.util.JsonMapper;
import com.mongodb.MongoException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnMongodbApplication.class)
@AutoConfigureMockMvc
public class ControllerTest {

    private Logger logger = LoggerFactory.getLogger(ControllerTest.class);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MgUserRepository mgUserRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void helloTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/")).andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void allTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/all")).andDo(MockMvcResultHandlers.print()).andReturn();
    }

    @Test
    public void nameTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/name").param("name", "admin")).andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void queryByNameTest() {
        MgUser user = mgUserRepository.queryByName("admin");
        logger.debug("{}", JsonMapper.toJson(user));
    }

    @Test
    public void save() {
        MgUser user = mgUserRepository.queryByName("admin");
        user.setRemark("OK");
        user = mgUserRepository.save(user);
        logger.debug("{}", JsonMapper.toJson(user));
    }

    @Test
    public void mongoTemplateTest() {

        DocumentCallbackHandler dch = new DocumentCallbackHandler() {

            @Override
            public void processDocument(Document document) throws MongoException, DataAccessException {
                // 处理自己的逻辑，这种为了有特殊需要功能的留的开放接口命令模式
                logger.debug("{}",document.toJson());
            }
        };

        mongoTemplate.executeQuery(new Query(Criteria.where("name").is("admin")), "mg_user", dch);
    }

}
