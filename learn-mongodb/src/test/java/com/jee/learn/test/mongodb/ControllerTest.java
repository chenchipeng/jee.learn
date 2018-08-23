package com.jee.learn.test.mongodb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.jee.learn.mongodb.LearnMongodbApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnMongodbApplication.class)
@AutoConfigureMockMvc
public class ControllerTest {

    @Autowired
    private MockMvc mvc;

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

}
