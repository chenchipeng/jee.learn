package com.jee.learn.interfaces.gen.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.interfaces.config.datasource.dynamic.TargetDataSource;
import com.jee.learn.interfaces.gen.GenConstants;
import com.jee.learn.interfaces.gen.domain.GenTable;
import com.jee.learn.interfaces.gen.dto.GenTableColumnDto;
import com.jee.learn.interfaces.gen.dto.GenTableDto;
import com.jee.learn.interfaces.gen.repository.GenTableRepository;
import com.jee.learn.interfaces.gen.service.GenTableService;
import com.jee.learn.interfaces.gen.service.GenUtils;
import com.jee.learn.interfaces.gen.service.GeneratorService;
import com.jee.learn.interfaces.util.time.ClockUtil;

/**
 * GenTableService
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月6日 上午11:30:17 ccp 新建
 */
@Service
@Transactional(readOnly = true)
public class GenTableServiceImpl implements GenTableService {

    @Autowired
    private GenTableRepository genTableRepository;

    @Autowired
    private GeneratorService generatorService;

    @Override
    public GenTable findOneById(String id) {
        return StringUtils.isBlank(id) ? null : genTableRepository.findOneById(id);
    }

    @Transactional(readOnly = false)
    @Override
    public void save(GenTable entity) {
        if (StringUtils.isBlank(entity.getId())) {
            entity.setCreateDate(ClockUtil.currentDate());
        }
        entity.setUpdateDate(ClockUtil.currentDate());
        genTableRepository.save(entity);
    }

    //////// 获取数据库元数据 ///////

    @TargetDataSource
    @Override
    public List<GenTableDto> selectDataTables() {
        List<GenTableDto> list = generatorService.selectDataTables();
        for (GenTableDto dto : list) {
            dto.setLabel(dto.getName() + ":" + dto.getComments());
        }
        return list;
    }
    
    public GenTableDto getDateTebleInfo(String tableName) {
        if(StringUtils.isBlank(tableName)) {
            return null;
        }
        // 查找表
        List<GenTableDto> tList = generatorService.selectDataTables(tableName);
        if(CollectionUtils.isEmpty(tList)) {
            return null;
        }
        GenTableDto table = tList.get(0);
        table.setClassName(generatorService.toClassName(tableName));
        // 查找列
        List<GenTableColumnDto> cList = generatorService.selectTableColumn(tableName);
        if(CollectionUtils.isEmpty(cList)) {
            return null;
        }
        for (GenTableColumnDto c : cList) {
//            private String queryType; // 查询方式（等于、不等于、大于、小于、范围、左LIKE、右LIKE、左右LIKE）
//            private String showType; // 字段生成方案（文本框、文本域、下拉框、复选框、单选框、字典选择、人员选择、部门选择、区域选择）
//            private String dictType; // 字典类型
//            private String settings; // 其它设置（扩展字段JSON）
//            private BigDecimal sort; // 排序（升序）
            
            c.setIsUuid(GenConstants.N);
            if(c.getIsPk()==GenConstants.Y&&"String".equals(c.getJavaType())) {
                c.setIsUuid(GenConstants.Y);
            }
            
            c.setIsInsert(GenConstants.Y);
            
            c.setIsEdit(GenConstants.Y);
            if(StringUtils.equalsIgnoreCase(c.getName(), GenConstants.ID)
                    ||StringUtils.equalsIgnoreCase(c.getName(), GenConstants.CREATE_BY)
                    ||StringUtils.equalsIgnoreCase(c.getName(), GenConstants.CREATE_DATE)
                    ||StringUtils.equalsIgnoreCase(c.getName(), GenConstants.DEL_FLAG)
                    ) {
                c.setIsEdit(GenConstants.N);
            }
            
            c.setIsList(GenConstants.N);
            if (StringUtils.equalsIgnoreCase(c.getName(), GenConstants.NAME)
                    || StringUtils.equalsIgnoreCase(c.getName(), GenConstants.TITLE)
                    || StringUtils.equalsIgnoreCase(c.getName(), GenConstants.UPDATE_DATE)) {
                c.setIsList(GenConstants.Y);
            }
            
            c.setIsQuery(GenConstants.N);
            if (StringUtils.equalsIgnoreCase(c.getName(), GenConstants.NAME)
                    || StringUtils.equalsIgnoreCase(c.getName(),  GenConstants.TITLE)) {
                c.setIsQuery(GenConstants.Y);
            }

            if (StringUtils.equalsIgnoreCase(c.getName(), GenConstants.NAME)
                    || StringUtils.equalsIgnoreCase(c.getName(),  GenConstants.TITLE)) {
                c.setQueryType("like");
            }
            
            
            
            
        }
        
        return null;
    }

}
