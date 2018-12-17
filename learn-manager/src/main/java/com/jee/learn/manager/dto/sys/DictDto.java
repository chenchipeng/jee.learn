package com.jee.learn.manager.dto.sys;

import java.util.List;

import com.jee.learn.manager.domain.sys.SysDict;
import com.jee.learn.manager.dto.base.DDto;

public class DictDto extends DDto {

    private static final long serialVersionUID = 1L;
    private List<SysDict> l;

    public List<SysDict> getL() {
        return l;
    }

    public void setL(List<SysDict> l) {
        this.l = l;
    }

    @Override
    public String toString() {
        return "DictDto [l=" + l + "]";
    }

}
