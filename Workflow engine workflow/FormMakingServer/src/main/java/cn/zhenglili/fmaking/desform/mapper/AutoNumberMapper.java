//package cn.zhenglili.fmaking.desform.mapper;
//
//import cn.zhenglili.fmaking.desform.entity.AutoGerateEntity;
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Param;
//import org.apache.ibatis.annotations.Select;
//
///**
// * @author ：Ethan yan
// * @date ：Created in 2021/1/15 下午4:42
// * @description：自动编码的DAO
// * @modified By：
// * @version: 1.0$
// */
//@Mapper
//public interface AutoNumberMapper extends BaseMapper<AutoGerateEntity> {
//    @Select("SELECT ${tableField} from ${tableName} order by create_date desc limit 1")
//    public String getTopFieldValue(@Param("tableName") String tableName, @Param("tableField") String tableField);
//
//}
