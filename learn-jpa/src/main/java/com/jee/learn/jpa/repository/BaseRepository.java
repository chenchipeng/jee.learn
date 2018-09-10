package com.jee.learn.jpa.repository;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Component;

import com.jee.learn.jpa.support.spec.repository.SpecRepository;

/**
 * 公共基础Repository
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年9月10日 下午1:58:29 ccp 新建
 */
@NoRepositoryBean
@Component
public interface BaseRepository<T, ID extends Serializable> extends SpecRepository<T, ID> {

    /**
     * 根据id查找一条记录<br/>
     * 该方法可由父类的findById代替，findById中增加了{@link Optional}实例，能有效控制空指针
     * 
     * @param id
     * @return
     */
    T findOneById(ID id);

}