package com.xlh.crawler.mapper;

import com.xlh.crawler.dto.CdmEntDtoCorpInfo;
import com.xlh.crawler.dto.SqlLimit;
import com.xlh.crawler.utils.MyMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface CdmEntDtoCorpInfoMapper extends MyMapper<CdmEntDtoCorpInfo>{

    @Results({
            @Result(column="enterprise_name", property="enterpriseName", jdbcType= JdbcType.VARCHAR),
            @Result(column="credit_no", property="creditNo", jdbcType=JdbcType.VARCHAR),
    })
    @Select("select * from cdm_ent_dto_corp_info where  status=0 and enterprise_name like '%上海%'  limit #{start,jdbcType=INTEGER} ,#{limit,jdbcType=INTEGER} ")
    public List<CdmEntDtoCorpInfo>  selectByPage(SqlLimit sqlLimit);


}
