package ee.priit.pall.tuum.repository;

import ee.priit.pall.tuum.entity.Transaction;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TransactionRepository {

    @Insert("INSERT INTO transaction (direction, account_id, amount, currency_id, description) "
      + "VALUES (#{direction}::direction, #{accountId}, #{amount}, #{currency.id}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(Transaction transaction);

    @Select("SELECT * FROM transaction WHERE account_id = #{accountId}")
    @Results(value = {
      @Result(property = "currency", column = "currency_id",
        one = @One(select = "ee.priit.pall.tuum.repository.CurrencyRepository.findById")),
    })
    List<Transaction> findByAccountId(long accountId);
}
