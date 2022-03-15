package ee.priit.pall.tuum.repository;

import ee.priit.pall.tuum.entity.Account;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AccountRepository {

    @Insert("INSERT INTO account (customer_id, country) VALUES (#{customerId}, #{country})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int save(Account account);

    @Select("SELECT * FROM account WHERE id = #{id}")
    @Results(value = {
      @Result(property = "id", column = "id"),
      @Result(property = "customerId", column = "customer_id"),
      @Result(property = "country", column = "country"),
      @Result(property = "balances", column = "id", javaType = List.class,
        many = @Many(select = "ee.priit.pall.tuum.repository.BalanceRepository.findByAccountId")),
    })
    Account findById(long id);
}
