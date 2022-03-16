package ee.priit.pall.tuum.repository;

import ee.priit.pall.tuum.entity.Balance;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BalanceRepository {

    @Insert("INSERT INTO balance (account_id, currency_id) values (#{accountId}, #{currencyId})")
    void save(Long accountId, Long currencyId);

    @Update("UPDATE balance SET amount = #{newAmount} WHERE id = #{id}")
    void update(Long id, long newAmount);

    @Select("SELECT * FROM balance WHERE id = #{id}")
    Balance findById(long id);

    @Select("SELECT * FROM balance WHERE account_id = #{accountId}")
    @Results(value = {
      @Result(property = "currency", column = "currency_id",
        one = @One(select = "ee.priit.pall.tuum.repository.CurrencyRepository.findById")),
    })
    List<Balance> findByAccountId(Long accountId);

    @Select("SELECT * FROM balance WHERE account_id = #{accountId} AND currency_id = #{currencyId}")
    @Results(value = {
      @Result(property = "currency", column = "currency_id",
        one = @One(select = "ee.priit.pall.tuum.repository.CurrencyRepository.findById")),
    })
    Balance findByAccountIdAndCurrencyId(Long accountId, Long currencyId);
}
